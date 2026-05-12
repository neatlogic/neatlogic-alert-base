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

package neatlogic.framework.alert.breaker.action;

import neatlogic.framework.alert.dao.mapper.AlertBreakerMapper;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerActionAuditVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerActionVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerStateVo;
import neatlogic.framework.alert.enums.AlertBreakerActionStatus;
import neatlogic.framework.alert.enums.AlertBreakerActionTrigger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public abstract class AlertBreakerActionHandlerBase implements IAlertBreakerActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AlertBreakerActionHandlerBase.class);

    @Resource
    protected AlertBreakerMapper alertBreakerMapper;

    @Override
    public final void triggerOpen(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertVo alertVo) {
        executeWithAudit(actionVo, policyVo, stateVo, AlertBreakerActionTrigger.OPEN, () -> myTriggerOpen(actionVo, policyVo, stateVo, alertVo));
    }

    @Override
    public final void triggerAggregate(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, List<AlertVo> alertList) {
        executeWithAudit(actionVo, policyVo, stateVo, AlertBreakerActionTrigger.AGGREGATE, () -> myTriggerAggregate(actionVo, policyVo, stateVo, alertList));
    }

    @Override
    public final void triggerRecover(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, List<AlertVo> alertList) {
        executeWithAudit(actionVo, policyVo, stateVo, AlertBreakerActionTrigger.RECOVER, () -> myTriggerRecover(actionVo, policyVo, stateVo, alertList));
    }

    private void executeWithAudit(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertBreakerActionTrigger trigger, ActionExecutor executor) {
        AlertBreakerActionAuditVo auditVo = new AlertBreakerActionAuditVo();
        auditVo.setPolicyId(policyVo == null ? null : policyVo.getId());
        auditVo.setStateId(stateVo == null ? null : stateVo.getId());
        auditVo.setTrigger(trigger == null ? null : trigger.getValue());
        auditVo.setActionUuid(actionVo == null ? null : actionVo.getUuid());
        auditVo.setActionName(actionVo == null ? null : actionVo.getName());
        auditVo.setActionHandler(actionVo == null ? null : actionVo.getHandler());
        auditVo.setStartTime(new Date());
        try {
            executor.execute();
            auditVo.setStatus(AlertBreakerActionStatus.SUCCEED.getValue());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            auditVo.setStatus(AlertBreakerActionStatus.FAILED.getValue());
            auditVo.setError(e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
        } finally {
            auditVo.setEndTime(new Date());
            alertBreakerMapper.insertAlertBreakerActionAudit(auditVo);
        }
    }

    protected void myTriggerOpen(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertVo alertVo) throws Exception {
    }

    protected void myTriggerAggregate(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, List<AlertVo> alertList) throws Exception {
    }

    protected void myTriggerRecover(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, List<AlertVo> alertList) throws Exception {
    }

    private interface ActionExecutor {
        void execute() throws Exception;
    }
}
