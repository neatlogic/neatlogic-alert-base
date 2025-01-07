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
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.enums.AlertEventStatus;
import neatlogic.framework.alert.exception.alertevent.AlertEventHandlerTriggerException;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;

public abstract class AlertEventHandlerBase implements IAlertEventHandler {

    @Resource
    private AlertEventMapper alertEventMapper;

    public final AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo) {
        alertVo = this.executeWithTransaction(alertEventHandlerVo, alertVo, null);
        return alertVo;
    }

    public final AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo) {
        alertVo = this.executeWithTransaction(alertEventHandlerVo, alertVo, alertEventHandlerAuditVo);
        return alertVo;
    }

    private AlertVo executeWithTransaction(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, AlertEventHandlerAuditVo parentAlertEventHandlerAuditVo) {
        /*
        由于eventhandler存在嵌套调用的行为，例如condition，因此每次调用trigger都是调用新的事务
         */
        AlertEventHandlerAuditVo alertEventHandlerAuditVo = new AlertEventHandlerAuditVo();
        alertEventHandlerAuditVo.setAlertId(alertVo.getId());
        alertEventHandlerAuditVo.setEventHandlerId(alertEventHandlerVo.getId());
        alertEventHandlerAuditVo.setEvent(alertEventHandlerVo.getEvent());
        alertEventHandlerAuditVo.setHandler(alertEventHandlerVo.getHandler());
        alertEventHandlerAuditVo.setHandlerName(alertEventHandlerVo.getHandlerName());
        alertEventHandlerAuditVo.setStatus(AlertEventStatus.RUNNING.getValue());
        if (parentAlertEventHandlerAuditVo != null) {
            alertEventHandlerAuditVo.setParentId(parentAlertEventHandlerAuditVo.getId());
        }
        alertEventMapper.insertAlertEventAudit(alertEventHandlerAuditVo);

        TransactionStatus ts = TransactionUtil.openNewTx();
        try {
            alertVo = myTrigger(alertEventHandlerVo, alertVo, alertEventHandlerAuditVo);
            TransactionUtil.commitTx(ts);
            alertEventHandlerAuditVo.setStatus(AlertEventStatus.SUCCEED.getValue());
        } catch (Exception e) {
            TransactionUtil.rollbackTx(ts);
            alertEventHandlerAuditVo.setStatus(AlertEventStatus.FAILED.getValue());
            alertEventHandlerAuditVo.setError(e.getMessage());
            throw e; // 抛出异常以便上层处理
        } finally {
            alertEventMapper.updateAlertEventHandlerAudit(alertEventHandlerAuditVo);
        }
        return alertVo;
    }

    protected abstract AlertVo myTrigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, AlertEventHandlerAuditVo alertEventHandlerAuditVo) throws AlertEventHandlerTriggerException;
}
