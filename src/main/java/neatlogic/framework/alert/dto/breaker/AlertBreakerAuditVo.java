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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.alert.breaker.AlertBreakerHandlerFactory;
import neatlogic.framework.alert.breaker.IAlertBreakerHandler;
import neatlogic.framework.alert.enums.AlertBreakerStatus;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class AlertBreakerAuditVo extends BasePageVo {
    private Long id;
    private Long policyId;
    private String policyName;
    private String policyHandler;
    private String policyHandlerLabel;
    private JSONObject policyConfig;
    @JSONField(serialize = false)
    private String policyConfigStr;
    private Long stateId;
    private JSONObject stateData;
    @JSONField(serialize = false)
    private String stateDataStr;
    private Long alertId;
    private String alertTitle;
    private Long eventHandlerAuditId;
    private Date startTime;
    private Date endTime;
    private String status;
    private String statusText;
    private String error;
    private List<AlertBreakerActionAuditVo> actionAuditList;
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

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyHandler() {
        return policyHandler;
    }

    public void setPolicyHandler(String policyHandler) {
        this.policyHandler = policyHandler;
    }

    public String getPolicyHandlerLabel() {
        if (StringUtils.isBlank(policyHandlerLabel) && StringUtils.isNotBlank(policyHandler)) {
            IAlertBreakerHandler handler = AlertBreakerHandlerFactory.getHandler(policyHandler);
            if (handler != null) {
                policyHandlerLabel = handler.getLabel();
            }
        }
        return policyHandlerLabel;
    }

    public void setPolicyHandlerLabel(String policyHandlerLabel) {
        this.policyHandlerLabel = policyHandlerLabel;
    }

    public JSONObject getPolicyConfig() {
        if (policyConfig == null && StringUtils.isNotBlank(policyConfigStr)) {
            try {
                policyConfig = JSON.parseObject(policyConfigStr);
            } catch (Exception ignored) {

            }
        }
        return policyConfig;
    }

    public void setPolicyConfig(JSONObject policyConfig) {
        this.policyConfig = policyConfig;
    }

    public String getPolicyConfigStr() {
        if (policyConfig != null) {
            policyConfigStr = JSON.toJSONString(policyConfig);
        }
        return policyConfigStr;
    }

    public void setPolicyConfigStr(String policyConfigStr) {
        this.policyConfigStr = policyConfigStr;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public JSONObject getStateData() {
        if (stateData == null && StringUtils.isNotBlank(stateDataStr)) {
            try {
                stateData = JSON.parseObject(stateDataStr);
            } catch (Exception ignored) {

            }
        }
        return stateData;
    }

    public void setStateData(JSONObject stateData) {
        this.stateData = stateData;
    }

    public String getStateDataStr() {
        if (stateData != null) {
            stateDataStr = JSON.toJSONString(stateData);
        }
        return stateDataStr;
    }

    public void setStateDataStr(String stateDataStr) {
        this.stateDataStr = stateDataStr;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public Long getEventHandlerAuditId() {
        return eventHandlerAuditId;
    }

    public void setEventHandlerAuditId(Long eventHandlerAuditId) {
        this.eventHandlerAuditId = eventHandlerAuditId;
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
            statusText = AlertBreakerStatus.getText(status);
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

    public List<AlertBreakerActionAuditVo> getActionAuditList() {
        return actionAuditList;
    }

    public void setActionAuditList(List<AlertBreakerActionAuditVo> actionAuditList) {
        this.actionAuditList = actionAuditList;
    }

    public long getTimeCost() {
        if (this.endTime != null && this.startTime != null) {
            timeCost = this.endTime.getTime() - this.startTime.getTime();
        }
        return timeCost;
    }
}
