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

import neatlogic.framework.alert.dao.mapper.AlertBreakerMapper;
import neatlogic.framework.alert.dto.AlertEventHandlerAuditVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerResultVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerStateVo;
import neatlogic.framework.alert.dto.breaker.AlertEventHandlerBreakerPolicyVo;
import neatlogic.framework.scheduler.core.IJob;
import neatlogic.framework.scheduler.core.SchedulerManager;
import neatlogic.framework.scheduler.dto.JobObject;
import neatlogic.framework.util.SpringContextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class AlertBreakerManager {
    private static final Logger logger = LoggerFactory.getLogger(AlertBreakerManager.class);
    private static final String ALERT_BREAKER_EXPIRE_JOB_CLASS = "neatlogic.module.alert.schedule.handler.AlertBreakerFlushScheduleJob";

    private AlertBreakerManager() {

    }

    public static boolean doBreak(AlertEventHandlerVo eventHandlerVo, AlertVo alertVo, Long eventHandlerAuditId) {
        AlertBreakerMapper mapper = SpringContextUtil.getBean(AlertBreakerMapper.class);
        List<AlertEventHandlerBreakerPolicyVo> policyRelList = mapper.getBreakerPolicyListByEventHandlerId(eventHandlerVo.getId());
        if (CollectionUtils.isEmpty(policyRelList)) {
            return false;
        }
        for (AlertEventHandlerBreakerPolicyVo policyRel : policyRelList) {
            AlertBreakerPolicyVo policyVo = mapper.getAlertBreakerPolicyById(policyRel.getPolicyId());
            if (policyVo == null || !Objects.equals(policyVo.getIsActive(), 1)) {
                continue;
            }
            IAlertBreakerHandler breakerHandler = AlertBreakerHandlerFactory.getHandler(policyVo.getHandler());
            if (breakerHandler == null) {
                logger.warn("Alert breaker handler not found: {}", policyVo.getHandler());
                continue;
            }
            AlertBreakerResultVo resultVo = breakerHandler.execute(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId);
            if (resultVo != null && resultVo.isOpenStarted()) {
                loadExpireJob(resultVo);
            }
            if (resultVo != null && resultVo.isBreaked()) {
                breakerHandler.collect(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId, resultVo);
                return true;
            }
        }
        return false;
    }

    public static void flush(AlertBreakerPolicyVo policyVo, neatlogic.framework.alert.dto.breaker.AlertBreakerStateVo stateVo) {
        if (policyVo == null || !Objects.equals(policyVo.getIsActive(), 1)) {
            return;
        }
        IAlertBreakerHandler breakerHandler = AlertBreakerHandlerFactory.getHandler(policyVo.getHandler());
        if (breakerHandler == null) {
            logger.warn("Alert breaker handler not found: {}", policyVo.getHandler());
            return;
        }
        breakerHandler.flush(policyVo, stateVo);
    }

    public static void recover(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo) {
        if (policyVo == null || !Objects.equals(policyVo.getIsActive(), 1)) {
            return;
        }
        IAlertBreakerHandler breakerHandler = AlertBreakerHandlerFactory.getHandler(policyVo.getHandler());
        if (breakerHandler == null) {
            logger.warn("Alert breaker handler not found: {}", policyVo.getHandler());
            return;
        }
        breakerHandler.recover(policyVo, stateVo);
    }

    public static void afterBreak(AlertEventHandlerVo eventHandlerVo, AlertVo alertVo, AlertEventHandlerAuditVo eventHandlerAuditVo) {
        AlertBreakerMapper mapper = SpringContextUtil.getBean(AlertBreakerMapper.class);
        List<AlertEventHandlerBreakerPolicyVo> policyRelList = mapper.getBreakerPolicyListByEventHandlerId(eventHandlerVo.getId());
        if (CollectionUtils.isEmpty(policyRelList)) {
            return;
        }
        for (AlertEventHandlerBreakerPolicyVo policyRel : policyRelList) {
            AlertBreakerPolicyVo policyVo = mapper.getAlertBreakerPolicyById(policyRel.getPolicyId());
            if (policyVo == null || !Objects.equals(policyVo.getIsActive(), 1)) {
                continue;
            }
            IAlertBreakerHandler breakerHandler = AlertBreakerHandlerFactory.getHandler(policyVo.getHandler());
            if (breakerHandler == null) {
                logger.warn("Alert breaker handler not found: {}", policyVo.getHandler());
                continue;
            }
            breakerHandler.after(policyVo, alertVo, eventHandlerVo, eventHandlerAuditVo);
        }
    }

    public static String buildExpireJobName(Long stateId) {
        return "ALERT-BREAKER-EXPIRE-" + stateId;
    }

    private static void loadExpireJob(AlertBreakerResultVo resultVo) {
        if (resultVo == null || resultVo.getStateId() == null || resultVo.getPolicyId() == null || resultVo.getOpenUntil() == null) {
            return;
        }
        SchedulerManager schedulerManager = SpringContextUtil.getBean(SchedulerManager.class);
        IJob jobHandler = SchedulerManager.getHandler(ALERT_BREAKER_EXPIRE_JOB_CLASS);
        if (schedulerManager == null || jobHandler == null) {
            return;
        }
        JobObject jobObject = new JobObject.Builder(buildExpireJobName(resultVo.getStateId()), jobHandler.getGroupName(), jobHandler.getClassName())
                .addData("stateId", resultVo.getStateId())
                .addData("policyId", resultVo.getPolicyId())
                .withBeginTime(resultVo.getOpenUntil())
                .build();
        schedulerManager.loadJob(jobObject);
    }
}
