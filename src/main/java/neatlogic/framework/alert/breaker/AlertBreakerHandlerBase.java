/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.alert.breaker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dao.mapper.AlertBreakerMapper;
import neatlogic.framework.alert.dto.AlertEventHandlerAuditVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.*;
import neatlogic.framework.alert.enums.AlertBreakerState;
import neatlogic.framework.alert.enums.AlertBreakerStatus;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Date;

public abstract class AlertBreakerHandlerBase implements IAlertBreakerHandler {
    private static final Logger logger = LoggerFactory.getLogger(AlertBreakerHandlerBase.class);
    @Resource
    protected AlertBreakerMapper alertBreakerMapper;

    @Override
    public final AlertBreakerResultVo execute(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId) {
        TransactionStatus ts = TransactionUtil.openNewTx();
        AlertBreakerAuditVo auditVo = new AlertBreakerAuditVo();
        auditVo.setPolicyId(policyVo.getId());
        auditVo.setAlertId(alertVo.getId());
        auditVo.setEventHandlerAuditId(eventHandlerAuditId);
        auditVo.setStartTime(new Date());
        try {
            String uniqueKey = myMakeUniqueKey(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId);
            AlertBreakerResultVo resultVo = new AlertBreakerResultVo();
            if (StringUtils.isBlank(uniqueKey)) {
                resultVo.setBreaked(false);
                resultVo.setStatus(AlertBreakerStatus.PASS.getValue());
            } else {
                AlertBreakerStateVo stateVo = getOrCreateStateForUpdate(policyVo.getId(), uniqueKey);
                resultVo.setStateId(stateVo.getId());
                Date now = new Date();
                if (isOpenStateValid(stateVo, now.getTime())) {
                    touchOpenState(stateVo, now);
                    resultVo.setBreaked(true);
                    resultVo.setStatus(AlertBreakerStatus.OPEN.getValue());
                } else {
                    AlertBreakerCheckResultVo checkResultVo = myCheck(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId, stateVo, getData(stateVo));
                    applyCheckResult(stateVo, checkResultVo, now);
                    resultVo.setBreaked(checkResultVo != null && checkResultVo.isBreaked());
                    resultVo.setStatus(resultVo.isBreaked() ? AlertBreakerStatus.OPEN.getValue() : AlertBreakerStatus.PASS.getValue());
                }
            }
            auditVo.setStateId(resultVo.getStateId());
            auditVo.setStatus(resultVo.getStatus());
            auditVo.setEndTime(new Date());
            alertBreakerMapper.insertAlertBreakerAudit(auditVo);
            TransactionUtil.commitTx(ts);
            return resultVo;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            auditVo.setStatus(AlertBreakerStatus.FAILED.getValue());
            auditVo.setError(e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
            auditVo.setEndTime(new Date());
            alertBreakerMapper.insertAlertBreakerAudit(auditVo);
            TransactionUtil.commitTx(ts);
            AlertBreakerResultVo resultVo = new AlertBreakerResultVo();
            resultVo.setStatus(AlertBreakerStatus.FAILED.getValue());
            resultVo.setBreaked(false);
            return resultVo;
        }
    }

    private AlertBreakerStateVo getOrCreateStateForUpdate(Long policyId, String uniqueKey) {
        AlertBreakerStateVo stateVo = new AlertBreakerStateVo();
        stateVo.setPolicyId(policyId);
        stateVo.setUniqueKey(uniqueKey);
        stateVo.setState(AlertBreakerState.CLOSED.getValue());
        alertBreakerMapper.insertAlertBreakerStateIfNotExists(stateVo);
        return alertBreakerMapper.getAlertBreakerStateForUpdate(policyId, uniqueKey);
    }

    private boolean isOpenStateValid(AlertBreakerStateVo stateVo, long nowTime) {
        if (AlertBreakerState.COLLECTING.getValue().equals(stateVo.getState()) || AlertBreakerState.FLUSHING.getValue().equals(stateVo.getState())) {
            return true;
        }
        return AlertBreakerState.OPEN.getValue().equals(stateVo.getState()) && stateVo.getOpenUntil() != null && stateVo.getOpenUntil().getTime() > nowTime;
    }

    private void touchOpenState(AlertBreakerStateVo stateVo, Date now) {
        stateVo.setSkipCount((stateVo.getSkipCount() == null ? 0 : stateVo.getSkipCount()) + 1);
        stateVo.setLastTriggerTime(now);
        alertBreakerMapper.updateAlertBreakerState(stateVo);
    }

    private void applyCheckResult(AlertBreakerStateVo stateVo, AlertBreakerCheckResultVo checkResultVo, Date now) {
        if (checkResultVo == null) {
            return;
        }
        stateVo.setWindowStart(checkResultVo.getWindowStart());
        stateVo.setWindowEnd(checkResultVo.getWindowEnd());
        stateVo.setTriggerCount(checkResultVo.getTriggerCount());
        stateVo.setLastTriggerTime(now);
        stateVo.setData(checkResultVo.getData() == null ? null : checkResultVo.getData().toJSONString());
        if (checkResultVo.isBreaked()) {
            if (checkResultVo.isClearCollectItemOnOpen()) {
                alertBreakerMapper.deleteAlertBreakerCollectItemByStateId(stateVo.getId());
            }
            stateVo.setState(AlertBreakerState.OPEN.getValue());
            stateVo.setOpenTime(now);
            stateVo.setOpenUntil(checkResultVo.getOpenUntil());
        } else {
            stateVo.setState(AlertBreakerState.CLOSED.getValue());
            stateVo.setOpenTime(null);
            stateVo.setOpenUntil(null);
            stateVo.setSkipCount(0);
        }
        alertBreakerMapper.updateAlertBreakerState(stateVo);
    }

    @Override
    public final void collect(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId, AlertBreakerResultVo resultVo) {
        if (resultVo == null || resultVo.getStateId() == null) {
            return;
        }
        TransactionStatus ts = TransactionUtil.openNewTx();
        AlertBreakerCollectResultVo collectResultVo;
        try {
            AlertBreakerStateVo stateVo = alertBreakerMapper.getAlertBreakerStateByIdForUpdate(resultVo.getStateId());
            if (stateVo == null || (!AlertBreakerState.OPEN.getValue().equals(stateVo.getState()) && !AlertBreakerState.COLLECTING.getValue().equals(stateVo.getState()))) {
                TransactionUtil.commitTx(ts);
                return;
            }
            boolean isCollectingStarted = AlertBreakerState.OPEN.getValue().equals(stateVo.getState());
            JSONObject data = getData(stateVo);
            collectResultVo = myCollect(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId, resultVo, stateVo, data, isCollectingStarted);
            if (collectResultVo != null) {
                JSONObject newData = collectResultVo.getData() == null ? data : collectResultVo.getData();
                newData.remove("collectError");
                stateVo.setState(AlertBreakerState.COLLECTING.getValue());
                stateVo.setData(newData.toJSONString());
                alertBreakerMapper.updateAlertBreakerState(stateVo);
                collectResultVo.setCollectingStarted(isCollectingStarted);
                collectResultVo.setStateVo(stateVo);
            }
            TransactionUtil.commitTx(ts);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(ts);
            saveCollectError(resultVo, e);
            return;
        }
        if (collectResultVo != null && collectResultVo.isCollectingStarted()) {
            try {
                collectingStarted(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId, resultVo, collectResultVo);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    @Override
    public void collectingStarted(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId, AlertBreakerResultVo resultVo, AlertBreakerCollectResultVo collectResultVo) {

    }

    private void saveCollectError(AlertBreakerResultVo resultVo, Exception e) {
        if (resultVo == null || resultVo.getStateId() == null) {
            return;
        }
        TransactionStatus ts = TransactionUtil.openNewTx();
        try {
            AlertBreakerStateVo stateVo = alertBreakerMapper.getAlertBreakerStateByIdForUpdate(resultVo.getStateId());
            if (stateVo == null || AlertBreakerState.FLUSHING.getValue().equals(stateVo.getState())) {
                TransactionUtil.commitTx(ts);
                return;
            }
            JSONObject data = getData(stateVo);
            data.put("collectError", e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
            stateVo.setData(data.toJSONString());
            alertBreakerMapper.updateAlertBreakerState(stateVo);
            TransactionUtil.commitTx(ts);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            TransactionUtil.rollbackTx(ts);
        }
    }

    private JSONObject getData(AlertBreakerStateVo stateVo) {
        if (stateVo == null || StringUtils.isBlank(stateVo.getData())) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(stateVo.getData());
        } catch (Exception ignored) {
            return new JSONObject();
        }
    }

    @Override
    public final void after(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, AlertEventHandlerAuditVo eventHandlerAuditVo) {
        TransactionStatus ts = TransactionUtil.openNewTx();
        try {
            String uniqueKey = myMakeUniqueKey(policyVo, alertVo, eventHandlerVo, eventHandlerAuditVo == null ? null : eventHandlerAuditVo.getId());
            if (StringUtils.isNotBlank(uniqueKey)) {
                AlertBreakerStateVo stateVo = getOrCreateStateForUpdate(policyVo.getId(), uniqueKey);
                AlertBreakerAfterResultVo afterResultVo = myAfter(policyVo, alertVo, eventHandlerVo, eventHandlerAuditVo, stateVo, getData(stateVo));
                applyAfterResult(stateVo, afterResultVo, new Date());
            }
            TransactionUtil.commitTx(ts);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(ts);
        }
    }

    private void applyAfterResult(AlertBreakerStateVo stateVo, AlertBreakerAfterResultVo afterResultVo, Date now) {
        if (afterResultVo == null) {
            return;
        }
        stateVo.setTriggerCount(afterResultVo.getTriggerCount());
        stateVo.setWindowStart(null);
        stateVo.setWindowEnd(null);
        stateVo.setLastTriggerTime(now);
        stateVo.setData(afterResultVo.getData() == null ? null : afterResultVo.getData().toJSONString());
        if (afterResultVo.isBreaked()) {
            stateVo.setState(AlertBreakerState.OPEN.getValue());
            stateVo.setOpenTime(now);
            stateVo.setOpenUntil(afterResultVo.getOpenUntil());
        } else {
            stateVo.setState(AlertBreakerState.CLOSED.getValue());
            stateVo.setOpenTime(null);
            stateVo.setOpenUntil(null);
            stateVo.setSkipCount(0);
        }
        alertBreakerMapper.updateAlertBreakerState(stateVo);
    }

    @Override
    public final void flush(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo) {
        if (stateVo == null || stateVo.getId() == null) {
            return;
        }
        TransactionStatus claimTs = TransactionUtil.openNewTx();
        try {
            if (alertBreakerMapper.updateCollectingAlertBreakerStateToFlushing(stateVo.getId()) == 0) {
                TransactionUtil.commitTx(claimTs);
                return;
            }
            TransactionUtil.commitTx(claimTs);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(claimTs);
            return;
        }

        TransactionStatus flushTs = TransactionUtil.openNewTx();
        try {
            AlertBreakerStateVo lockedStateVo = alertBreakerMapper.getAlertBreakerStateByIdForUpdate(stateVo.getId());
            if (lockedStateVo == null || !AlertBreakerState.FLUSHING.getValue().equals(lockedStateVo.getState())) {
                TransactionUtil.commitTx(flushTs);
                return;
            }
            JSONObject data = getData(lockedStateVo);
            AlertBreakerFlushResultVo flushResultVo = myFlush(policyVo, lockedStateVo, data);
            JSONObject newData = flushResultVo == null || flushResultVo.getData() == null ? data : flushResultVo.getData();
            newData.remove("flushError");
            closeState(lockedStateVo, newData);
            TransactionUtil.commitTx(flushTs);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(flushTs);
            closeStateWithFlushError(stateVo.getId(), e);
        }
    }

    private void closeState(AlertBreakerStateVo stateVo, JSONObject data) {
        stateVo.setState(AlertBreakerState.CLOSED.getValue());
        stateVo.setWindowStart(null);
        stateVo.setWindowEnd(null);
        stateVo.setTriggerCount(0);
        stateVo.setOpenTime(null);
        stateVo.setOpenUntil(null);
        stateVo.setSkipCount(0);
        stateVo.setLastTriggerTime(new Date());
        stateVo.setData(data == null ? null : data.toJSONString());
        alertBreakerMapper.updateAlertBreakerState(stateVo);
        alertBreakerMapper.deleteAlertBreakerCollectItemByStateId(stateVo.getId());
    }

    private void closeStateWithFlushError(Long stateId, Exception e) {
        if (stateId == null) {
            return;
        }
        TransactionStatus ts = TransactionUtil.openNewTx();
        try {
            AlertBreakerStateVo stateVo = alertBreakerMapper.getAlertBreakerStateByIdForUpdate(stateId);
            if (stateVo == null) {
                TransactionUtil.commitTx(ts);
                return;
            }
            JSONObject data = getData(stateVo);
            data.put("aggregateSent", false);
            data.put("flushError", e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
            closeState(stateVo, data);
            TransactionUtil.commitTx(ts);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            TransactionUtil.rollbackTx(ts);
        }
    }

    protected abstract String myMakeUniqueKey(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId) throws Exception;

    protected abstract AlertBreakerCheckResultVo myCheck(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId, AlertBreakerStateVo stateVo, JSONObject data) throws Exception;

    protected AlertBreakerCollectResultVo myCollect(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId, AlertBreakerResultVo resultVo, AlertBreakerStateVo stateVo, JSONObject data, boolean isCollectingStarted) throws Exception {
        return null;
    }

    protected AlertBreakerAfterResultVo myAfter(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, AlertEventHandlerAuditVo eventHandlerAuditVo, AlertBreakerStateVo stateVo, JSONObject data) throws Exception {
        return null;
    }

    protected AlertBreakerFlushResultVo myFlush(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, JSONObject data) throws Exception {
        return null;
    }
}
