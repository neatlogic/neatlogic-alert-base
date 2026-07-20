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

package neatlogic.framework.alert.dto;

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.dto.BasePageVo;

import java.util.List;

/**
 * 告警事件执行审计查询条件。
 */
@SuppressWarnings("serial")
public class AlertEventHandlerAuditSearchVo extends BasePageVo {
    private static final long serialVersionUID = 1L;
    private Long auditId;
    @JSONField(serialize = false)
    private Long rootAuditId;
    private Long alertId;
    private String event;
    private String handler;
    private String status;
    private List<String> timeRange;
    private Integer hasChild;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getRootAuditId() {
        return rootAuditId;
    }

    public void setRootAuditId(Long rootAuditId) {
        this.rootAuditId = rootAuditId;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(List<String> timeRange) {
        this.timeRange = timeRange;
    }

    public Integer getHasChild() {
        return hasChild;
    }

    public void setHasChild(Integer hasChild) {
        this.hasChild = hasChild;
    }
}
