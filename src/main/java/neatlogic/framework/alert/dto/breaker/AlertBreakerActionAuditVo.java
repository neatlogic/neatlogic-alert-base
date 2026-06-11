/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the Sustainable Use License (SUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.alert.dto.breaker;

import neatlogic.framework.alert.enums.AlertBreakerActionStatus;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class AlertBreakerActionAuditVo extends BasePageVo {
    private Long id;
    private Long breakerAuditId;
    private Long policyId;
    private Long stateId;
    private String trigger;
    private String actionUuid;
    private String actionName;
    private String actionHandler;
    private Date startTime;
    private Date endTime;
    private String status;
    private String statusText;
    private String error;
    private long timeCost;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBreakerAuditId() {
        return breakerAuditId;
    }

    public void setBreakerAuditId(Long breakerAuditId) {
        this.breakerAuditId = breakerAuditId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getActionUuid() {
        return actionUuid;
    }

    public void setActionUuid(String actionUuid) {
        this.actionUuid = actionUuid;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionHandler() {
        return actionHandler;
    }

    public void setActionHandler(String actionHandler) {
        this.actionHandler = actionHandler;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        if (StringUtils.isBlank(statusText) && StringUtils.isNotBlank(status)) {
            statusText = AlertBreakerActionStatus.getText(status);
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getTimeCost() {
        if (this.endTime != null && this.startTime != null) {
            timeCost = this.endTime.getTime() - this.startTime.getTime();
        }
        return timeCost;
    }
}
