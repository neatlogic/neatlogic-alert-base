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

package neatlogic.framework.alert.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.alert.enums.AlertEventStatus;
import neatlogic.framework.alert.event.AlertEventType;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class AlertEventHandlerAuditVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "告警id", type = ApiParamType.LONG)
    private Long alertId;
    @EntityField(name = "父id", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "事件", type = ApiParamType.STRING)
    private String event;
    @EntityField(name = "事件名称", type = ApiParamType.STRING)
    private String eventName;
    @EntityField(name = "事件处理器", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "事件处理器名称", type = ApiParamType.STRING)
    private String handlerName;
    @EntityField(name = "事件id", type = ApiParamType.LONG)
    private Long eventHandlerId;
    @EntityField(name = "开始时间", type = ApiParamType.LONG)
    private Date startTime;
    @EntityField(name = "结束时间", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "异常", type = ApiParamType.STRING)
    private String error;
    @EntityField(name = "结果", type = ApiParamType.JSONOBJECT)
    private JSONObject result;
    @JSONField(serialize = false)
    private String resultStr;
    @EntityField(name = "耗时", type = ApiParamType.LONG)
    private long timeCost;
    @EntityField(name = "子记录")
    private List<AlertEventHandlerAuditVo> childAuditList;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getEventName() {
        if (StringUtils.isNotBlank(event)) {
            eventName = AlertEventType.getLabel(event);
        }
        return eventName;
    }

    public String getResultStr() {
        if (result != null) {
            resultStr = JSON.toJSONString(result);
        }
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public JSONObject getResult() {
        if (result == null && StringUtils.isNotBlank(resultStr)) {
            try {
                result = JSON.parseObject(resultStr);
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
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

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Long getEventHandlerId() {
        return eventHandlerId;
    }

    public void setEventHandlerId(Long eventHandlerId) {
        this.eventHandlerId = eventHandlerId;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatusName() {
        if (StringUtils.isNotBlank(status)) {
            statusName = AlertEventStatus.getText(status);
        }
        return statusName;
    }

    public long getTimeCost() {
        if (this.endTime != null && this.startTime != null) {
            timeCost = this.endTime.getTime() - this.startTime.getTime();
        }
        return timeCost;
    }

    public List<AlertEventHandlerAuditVo> getChildAuditList() {
        return childAuditList;
    }

    public void setChildAuditList(List<AlertEventHandlerAuditVo> childAuditList) {
        this.childAuditList = childAuditList;
    }
}
