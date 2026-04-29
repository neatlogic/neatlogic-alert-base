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
import neatlogic.framework.alert.dto.breaker.AlertBreakerAuditVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerResultVo;
import neatlogic.framework.alert.enums.AlertBreakerStatus;
import neatlogic.framework.transaction.util.TransactionUtil;
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
            AlertBreakerResultVo resultVo = myCheck(policyVo, alertVo, eventHandlerVo, eventHandlerAuditId);
            if (resultVo != null) {
                auditVo.setStateId(resultVo.getStateId());
                auditVo.setStatus(resultVo.getStatus());
            } else {
                auditVo.setStatus(AlertBreakerStatus.PASS.getValue());
                resultVo = new AlertBreakerResultVo();
                resultVo.setStatus(AlertBreakerStatus.PASS.getValue());
            }
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

    @Override
    public final void after(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, AlertEventHandlerAuditVo eventHandlerAuditVo) {
        TransactionStatus ts = TransactionUtil.openNewTx();
        try {
            myAfter(policyVo, alertVo, eventHandlerVo, eventHandlerAuditVo);
            TransactionUtil.commitTx(ts);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            TransactionUtil.rollbackTx(ts);
        }
    }

    protected abstract AlertBreakerResultVo myCheck(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId) throws Exception;

    protected void myAfter(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, AlertEventHandlerAuditVo eventHandlerAuditVo) throws Exception {

    }
}
