/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.alert.event;

import neatlogic.framework.alert.dao.mapper.AlertEventMapper;
import neatlogic.framework.alert.dto.AlertEventHandlerAuditVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertEventStatusVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.enums.AlertEventStatus;
import neatlogic.framework.alert.exception.alertevent.AlertEventHandlerTriggerException;
import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.asynchronization.threadpool.CachedThreadPool;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;

public abstract class AlertEventHandlerBase implements IAlertEventHandler {
    private final Logger logger = LoggerFactory.getLogger(AlertEventHandlerBase.class);


    @Resource
    protected AlertEventMapper alertEventMapper;

    public final AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo) {
        //重新获取alertVo，避免alertVo缺失了某些关键属性
        alertVo = alertEventMapper.getAlertById(alertVo.getId());
        alertVo = this.executeWithTransaction(alertEventHandlerVo, alertVo, null);
        return alertVo;
    }

    public final AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, Long parentAuditId) {
        //重新获取alertVo，避免alertVo缺失了某些关键属性
        alertVo = alertEventMapper.getAlertById(alertVo.getId());
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
        if (parentAuditId != null) {
            alertEventHandlerAuditVo.setParentId(parentAuditId);
        }
        alertEventMapper.insertAlertEventAudit(alertEventHandlerAuditVo);
        AlertEventStatusVo alertEventStatusVo = new AlertEventStatusVo();
        if (!this.isAsync()) {
            //同步作业，可以修改alertVo信息
            TransactionStatus ts = TransactionUtil.openNewTx();
            try {
                alertVo = myTrigger(alertEventHandlerVo, alertVo, alertEventHandlerAuditVo, alertEventStatusVo);
                TransactionUtil.commitTx(ts);
                if (alertEventStatusVo.isSkipped()) {
                    alertEventHandlerAuditVo.setStatus(AlertEventStatus.SKIPPED.getValue());
                } else {
                    alertEventHandlerAuditVo.setStatus(AlertEventStatus.SUCCEED.getValue());
                }
            } catch (Exception e) {
                TransactionUtil.rollbackTx(ts);
                alertEventHandlerAuditVo.setStatus(AlertEventStatus.FAILED.getValue());
                alertEventHandlerAuditVo.setError(e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
                throw e; // 抛出异常以便上层处理
            } finally {
                alertEventMapper.updateAlertEventHandlerAudit(alertEventHandlerAuditVo);
            }
        } else {
            //异步作业，不能修改alertVo信息
            AlertVo finalAlertVo = alertVo;
            CachedThreadPool.execute(new NeatLogicThread("ALERT-EVENT-HANDLER-" + finalAlertVo.getId()) {
                @Override
                protected void execute() {
                    TransactionStatus ts = TransactionUtil.openNewTx();
                    try {
                        myTrigger(alertEventHandlerVo, finalAlertVo, alertEventHandlerAuditVo, alertEventStatusVo);
                        TransactionUtil.commitTx(ts);
                        if (alertEventStatusVo.isSkipped()) {
                            alertEventHandlerAuditVo.setStatus(AlertEventStatus.SKIPPED.getValue());
                        } else {
                            alertEventHandlerAuditVo.setStatus(AlertEventStatus.SUCCEED.getValue());
                        }
                    } catch (Exception e) {
                        TransactionUtil.rollbackTx(ts);
                        logger.error(e.getMessage(), e);
                        alertEventHandlerAuditVo.setStatus(AlertEventStatus.FAILED.getValue());
                        alertEventHandlerAuditVo.setError(e.getMessage() == null ? ExceptionUtils.getStackTrace(e) : e.getMessage());
                        throw e; // 抛出异常以便上层处理
                    } finally {
                        alertEventMapper.updateAlertEventHandlerAudit(alertEventHandlerAuditVo);
                    }
                }
            });
        }


        return alertVo;
    }

    protected abstract AlertVo myTrigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo, AlertEventStatusVo alertEventStatusVo) throws AlertEventHandlerTriggerException;
}
