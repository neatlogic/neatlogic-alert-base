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
import neatlogic.framework.alert.breaker.action.AlertBreakerActionManager;
import neatlogic.framework.alert.dao.mapper.AlertBreakerMapper;
import neatlogic.framework.alert.dto.AlertEventHandlerAuditVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.*;
import neatlogic.framework.alert.enums.AlertBreakerActionTrigger;
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
import java.util.List;

public abstract class AlertBreakerHandlerBase implements IAlertBreakerHandler {
    private static final Logger logger = LoggerFactory.getLogger(AlertBreakerHandlerBase.class);
    @Resource
    protected AlertBreakerMapper alertBreakerMapper;

    @Override
    public final AlertBreakerResultVo execute(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId) {
        AlertBreakerAuditVo auditVo = new AlertBreakerAuditVo();
        auditVo.setPolicyId(policyVo == null ? null : policyVo.getId());
        auditVo.setAlertId(alertVo == null ? null : alertVo.getId());
        auditVo.setEventHandlerAuditId(eventHandlerAuditId);
        auditVo.setStartTime(new Date());
        AlertBreakerResultVo resultVo = new AlertBreakerResultVo();
        TransactionStatus ts = null;
        try {
            ts = TransactionUtil.openNewTx();
            String uniqueKey = myMakeUniqueKey(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId);
            if (StringUtils.isBlank(uniqueKey)) {
                resultVo.setBreaked(false);
                resultVo.setStatus(AlertBreakerStatus.PASS.getValue());
            } else {
                AlertBreakerStateVo stateVo = getOrCreateStateForUpdate(policyVo.getId(), uniqueKey, eventHandlerVo == null ? null : eventHandlerVo.getId());
                resultVo.setPolicyId(policyVo.getId());
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
                    resultVo.setOpenStarted(resultVo.isBreaked());
                    resultVo.setOpenUntil(stateVo.getOpenUntil());
                }
            }
            auditVo.setStateId(resultVo.getStateId());
            auditVo.setStatus(resultVo.getStatus());
            auditVo.setEndTime(new Date());
            alertBreakerMapper.insertAlertBreakerAudit(auditVo);
            if (!commitTx(ts)) {
                rollbackTx(ts);
                resultVo.setStatus(AlertBreakerStatus.FAILED.getValue());
                resultVo.setBreaked(false);
                resultVo.setOpenStarted(false);
                saveFailedAlertBreakerAudit(auditVo, new RuntimeException("Commit alert breaker transaction failed"));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            rollbackTx(ts);
            saveFailedAlertBreakerAudit(auditVo, e);
            resultVo.setStatus(AlertBreakerStatus.FAILED.getValue());
            resultVo.setBreaked(false);
            resultVo.setOpenStarted(false);
        }
        if (resultVo.isOpenStarted()) {
            try {
                AlertBreakerStateVo stateVo = alertBreakerMapper.getAlertBreakerStateById(resultVo.getStateId());
                AlertBreakerActionManager.execute(policyVo, stateVo, AlertBreakerActionTrigger.OPEN, java.util.Collections.singletonList(alertVo), auditVo.getId());
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return resultVo;
    }

    private void saveFailedAlertBreakerAudit(AlertBreakerAuditVo auditVo, Exception exception) {
        TransactionStatus auditTs = null;
        try {
            auditTs = TransactionUtil.openNewTx();
            auditVo.setStatus(AlertBreakerStatus.FAILED.getValue());
            auditVo.setError(exception.getMessage() == null ? ExceptionUtils.getStackTrace(exception) : exception.getMessage());
            auditVo.setEndTime(new Date());
            alertBreakerMapper.insertAlertBreakerAudit(auditVo);
            if (!commitTx(auditTs)) {
                rollbackTx(auditTs);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            rollbackTx(auditTs);
        }
    }

    private boolean commitTx(TransactionStatus ts) {
        if (ts == null || ts.isCompleted()) {
            return true;
        }
        try {
            TransactionUtil.commitTx(ts);
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
    }

    private void rollbackTx(TransactionStatus ts) {
        if (ts == null || ts.isCompleted()) {
            return;
        }
        try {
            TransactionUtil.rollbackTx(ts);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private AlertBreakerStateVo getOrCreateStateForUpdate(Long policyId, String uniqueKey, Long eventHandlerId) {
        AlertBreakerStateVo stateVo = new AlertBreakerStateVo();
        stateVo.setPolicyId(policyId);
        stateVo.setEventHandlerId(eventHandlerId);
        stateVo.setUniqueKey(uniqueKey);
        stateVo.setState(AlertBreakerState.CLOSED.getValue());
        alertBreakerMapper.insertAlertBreakerStateIfNotExists(stateVo);
        stateVo = alertBreakerMapper.getAlertBreakerStateForUpdate(policyId, uniqueKey);
        stateVo.setEventHandlerId(eventHandlerId);
        return stateVo;
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
                AlertBreakerStateVo stateVo = getOrCreateStateForUpdate(policyVo.getId(), uniqueKey, eventHandlerVo == null ? null : eventHandlerVo.getId());
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

        AlertBreakerStateVo lockedStateVo = null;
        JSONObject data = null;
        AlertBreakerFlushResultVo flushResultVo = null;
        try {
            lockedStateVo = alertBreakerMapper.getAlertBreakerStateById(stateVo.getId());
            if (lockedStateVo == null || !AlertBreakerState.FLUSHING.getValue().equals(lockedStateVo.getState())) {
                return;
            }
            data = getData(lockedStateVo);
            flushResultVo = myFlush(policyVo, lockedStateVo, data);
            AlertBreakerActionManager.execute(policyVo, lockedStateVo, AlertBreakerActionTrigger.AGGREGATE, flushResultVo == null ? null : flushResultVo.getAlertList());
            JSONObject newData = flushResultVo == null || flushResultVo.getData() == null ? data : flushResultVo.getData();
            newData.remove("flushError");
            closeStateWithRecover(policyVo, lockedStateVo, newData, flushResultVo == null ? null : flushResultVo.getAlertList());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            closeStateWithFlushError(stateVo.getId(), e);
            AlertBreakerStateVo currentStateVo = alertBreakerMapper.getAlertBreakerStateById(stateVo.getId());
            AlertBreakerActionManager.execute(policyVo, currentStateVo, AlertBreakerActionTrigger.RECOVER, flushResultVo == null ? null : flushResultVo.getAlertList());
        }
    }

    private void closeStateWithRecover(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, JSONObject data, List<AlertVo> alertList) {
        TransactionStatus ts = TransactionUtil.openNewTx();
        try {
            AlertBreakerStateVo lockedStateVo = alertBreakerMapper.getAlertBreakerStateByIdForUpdate(stateVo.getId());
            if (lockedStateVo == null) {
                TransactionUtil.commitTx(ts);
                return;
            }
            closeState(lockedStateVo, data);
            TransactionUtil.commitTx(ts);
            AlertBreakerActionManager.execute(policyVo, lockedStateVo, AlertBreakerActionTrigger.RECOVER, alertList);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(ts);
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

    @Override
    public final void recover(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo) {
        if (stateVo == null || stateVo.getId() == null) {
            return;
        }
        TransactionStatus ts = TransactionUtil.openNewTx();
        AlertBreakerStateVo lockedStateVo = null;
        try {
            lockedStateVo = alertBreakerMapper.getAlertBreakerStateByIdForUpdate(stateVo.getId());
            if (lockedStateVo == null
                    || !AlertBreakerState.OPEN.getValue().equals(lockedStateVo.getState())
                    || lockedStateVo.getOpenUntil() == null
                    || lockedStateVo.getOpenUntil().getTime() > System.currentTimeMillis()) {
                TransactionUtil.commitTx(ts);
                return;
            }
            closeState(lockedStateVo, getData(lockedStateVo));
            TransactionUtil.commitTx(ts);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(ts);
            return;
        }
        AlertBreakerActionManager.execute(policyVo, lockedStateVo, AlertBreakerActionTrigger.RECOVER, null);
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
