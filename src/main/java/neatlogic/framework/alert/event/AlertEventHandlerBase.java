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

package neatlogic.framework.alert.event;

import neatlogic.framework.alert.breaker.AlertBreakerManager;
import neatlogic.framework.alert.crossover.IAlertSuppressionCrossoverService;
import neatlogic.framework.alert.dao.mapper.AlertEventMapper;
import neatlogic.framework.alert.dto.*;
import neatlogic.framework.alert.enums.AlertEventStatus;
import neatlogic.framework.alert.exception.alertevent.AlertEventHandlerTriggerException;
import neatlogic.framework.alert.utils.AlertEventHandlerContextBuilder;
import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.asynchronization.threadpool.CachedThreadPool;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.exception.core.ApiRuntimeException;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

public abstract class AlertEventHandlerBase implements IAlertEventHandler {
    private final Logger logger = LoggerFactory.getLogger(AlertEventHandlerBase.class);
    @Resource
    protected AlertEventMapper alertEventMapper;
    @Resource
    private AlertEventHandlerContextBuilder alertEventHandlerContextBuilder;

    public final AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo) {
        //重新获取alertVo，避免alertVo缺失了某些关键属性
        alertVo = alertEventHandlerContextBuilder.build(alertVo);
        alertVo = this.executeWithTransaction(alertEventHandlerVo, alertVo, null);
        return alertVo;
    }

    public final AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, Long parentAuditId) {
        //重新获取alertVo，避免alertVo缺失了某些关键属性
        alertVo = alertEventHandlerContextBuilder.build(alertVo);
        alertVo = this.executeWithTransaction(alertEventHandlerVo, alertVo, parentAuditId);
        return alertVo;
    }


    private AlertVo executeWithTransaction(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, Long parentAuditId) {
        /*
        由于eventHandler存在嵌套调用的行为，例如condition，因此每次调用trigger都是调用新的事务
         */
        AlertEventHandlerAuditVo alertEventHandlerAuditVo = new AlertEventHandlerAuditVo();
        alertEventHandlerAuditVo.setAlertId(alertVo.getId());
        alertEventHandlerAuditVo.setUniqueKey(alertVo.getUniqueKey());
        alertEventHandlerAuditVo.setEventHandlerId(alertEventHandlerVo.getId());
        alertEventHandlerAuditVo.setEvent(alertEventHandlerVo.getEvent());
        alertEventHandlerAuditVo.setHandler(alertEventHandlerVo.getHandler());
        alertEventHandlerAuditVo.setHandlerName(alertEventHandlerVo.getName());//用事件配的的名称代替handler名称
        alertEventHandlerAuditVo.setStatus(AlertEventStatus.RUNNING.getValue());
        alertEventHandlerAuditVo.setConfig(alertEventHandlerVo.getConfig());
        alertEventHandlerAuditVo.setIsAsync(alertEventHandlerVo.getIsAsync());
        if (parentAuditId != null) {
            alertEventHandlerAuditVo.setParentId(parentAuditId);
        }
        //记录当前时间的真正开始时间，后续可能需要使用
        alertEventHandlerAuditVo.setStartTime(new Date());
        alertEventMapper.insertAlertEventAudit(alertEventHandlerAuditVo);
        AlertEventPluginVo alertEventPluginVo = alertEventMapper.getAlertEventPluginConfigByName(alertEventHandlerVo.getHandler());

        if (alertEventHandlerVo.getIsAsync().equals(0)) {
            alertVo = executeEventHandler(alertEventHandlerVo, alertEventPluginVo, alertVo, alertEventHandlerAuditVo, true);
        } else {
            //异步作业，不能修改alertVo信息
            AlertVo finalAlertVo = alertVo;
            CachedThreadPool.execute(new NeatLogicThread("ALERT-EVENT-HANDLER-" + finalAlertVo.getId()) {
                @Override
                protected void execute() {
                    executeEventHandler(alertEventHandlerVo, alertEventPluginVo, finalAlertVo, alertEventHandlerAuditVo, false);
                }
            });
        }


        return alertVo;
    }

    private AlertVo executeEventHandler(AlertEventHandlerVo alertEventHandlerVo, AlertEventPluginVo alertEventPluginVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo, boolean isThrowException) {
        try {
            PreCheckResult preCheckResult = preCheck(alertEventHandlerVo, alertEventPluginVo, alertVo, alertEventHandlerAuditVo.getId());
            if (preCheckResult.isCompleted()) {
                alertEventHandlerAuditVo.setStatus(preCheckResult.getStatus());
                return alertVo;
            }
            alertVo = executeMainEventTransaction(alertEventHandlerVo, alertEventPluginVo, alertVo, alertEventHandlerAuditVo, isThrowException);
            return alertVo;
        } catch (Exception e) {
            if (e instanceof ApiRuntimeException) {
                logger.warn(e.getMessage(), e);
            } else {
                logger.error(e.getMessage(), e);
            }
            alertEventHandlerAuditVo.setStatus(AlertEventStatus.FAILED.getValue());
            alertEventHandlerAuditVo.setError(e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
            if (isThrowException) {
                throw e;
            }
            return alertVo;
        } finally {
            updateEventHandlerAudit(alertEventHandlerAuditVo);
            afterBreaker(alertEventHandlerVo, alertVo, alertEventHandlerAuditVo);
        }
    }

    private PreCheckResult preCheck(AlertEventHandlerVo alertEventHandlerVo, AlertEventPluginVo alertEventPluginVo, AlertVo alertVo, Long eventHandlerAuditId) {
        // 前置阶段不打开事件主事务，避免熔断、屏蔽等旁路能力污染插件事务。
        if ((alertEventPluginVo != null && Objects.equals(0, alertEventPluginVo.getIsActive())) || Objects.equals(0, alertEventHandlerVo.getIsActive())) {
            return PreCheckResult.completed(AlertEventStatus.DISABLED.getValue());
        }
        IAlertSuppressionCrossoverService suppressionService = CrossoverServiceFactory.tryToGetApi(IAlertSuppressionCrossoverService.class);
        if (suppressionService != null && suppressionService.doSuppression(alertVo, alertEventHandlerVo.getTypeId(), alertEventHandlerVo.getEvent(), getEventHandlerAuditName(alertEventHandlerVo))) {
            return PreCheckResult.completed(AlertEventStatus.SUPPRESS.getValue());
        }
        if (doBreaker(alertEventHandlerVo, alertVo, eventHandlerAuditId)) {
            return PreCheckResult.completed(AlertEventStatus.BREAKED.getValue());
        }
        return PreCheckResult.pass();
    }

    private AlertVo executeMainEventTransaction(AlertEventHandlerVo alertEventHandlerVo, AlertEventPluginVo alertEventPluginVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo, boolean isThrowException) {
        TransactionStatus ts = TransactionUtil.openNewTx();
        AlertEventStatusVo alertEventStatusVo = new AlertEventStatusVo();
        try {
            alertVo = myTrigger(alertEventHandlerVo, alertEventPluginVo, alertVo, alertEventHandlerAuditVo, alertEventStatusVo);
            if (!commitTx(ts)) {
                alertEventHandlerAuditVo.setStatus(AlertEventStatus.FAILED.getValue());
                alertEventHandlerAuditVo.setError("Commit alert event handler transaction failed");
                return alertVo;
            }
            if (alertEventStatusVo.getStatus() != null) {
                alertEventHandlerAuditVo.setStatus(alertEventStatusVo.getStatus());
            } else if (Objects.equals(alertEventHandlerAuditVo.getStatus(), AlertEventStatus.RUNNING.getValue())) {
                alertEventHandlerAuditVo.setStatus(AlertEventStatus.SUCCEED.getValue());
            }
            return alertVo;
        } catch (Exception e) {
            rollbackTx(ts);
            if (e instanceof ApiRuntimeException) {
                logger.warn(e.getMessage(), e);
            } else {
                logger.error(e.getMessage(), e);
            }
            alertEventHandlerAuditVo.setStatus(AlertEventStatus.FAILED.getValue());
            alertEventHandlerAuditVo.setError(e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
            if (isThrowException) {
                throw e;
            }
            return alertVo;
        }
    }

    private String getEventHandlerAuditName(AlertEventHandlerVo alertEventHandlerVo) {
        if (StringUtils.isNotBlank(alertEventHandlerVo.getName())) {
            return alertEventHandlerVo.getName();
        }
        return alertEventHandlerVo.getHandlerName();
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

    private void updateEventHandlerAudit(AlertEventHandlerAuditVo alertEventHandlerAuditVo) {
        try {
            alertEventMapper.updateAlertEventHandlerAudit(alertEventHandlerAuditVo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private boolean doBreaker(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, Long eventHandlerAuditId) {
        try {
            return AlertBreakerManager.doBreak(alertEventHandlerVo, alertVo, eventHandlerAuditId);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
    }

    private void afterBreaker(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo) {
        try {
            AlertBreakerManager.afterBreak(alertEventHandlerVo, alertVo, alertEventHandlerAuditVo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private static class PreCheckResult {
        private final String status;

        private PreCheckResult(String status) {
            this.status = status;
        }

        private static PreCheckResult completed(String status) {
            return new PreCheckResult(status);
        }

        private static PreCheckResult pass() {
            return new PreCheckResult(null);
        }

        private boolean isCompleted() {
            return status != null;
        }

        private String getStatus() {
            return status;
        }
    }

    protected abstract AlertVo myTrigger(AlertEventHandlerVo alertEventHandlerVo, AlertEventPluginVo alertEventPluginVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo, AlertEventStatusVo alertEventStatusVo) throws AlertEventHandlerTriggerException;
}
