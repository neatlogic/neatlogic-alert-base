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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.alert.dto.breaker.AlertEventHandlerBreakerPolicyVo;
import neatlogic.framework.alert.event.AlertEventHandlerFactory;
import neatlogic.framework.alert.event.AlertEventType;
import neatlogic.framework.alert.event.IAlertEventHandler;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlertEventHandlerVo implements Serializable {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "父组件uuid", type = ApiParamType.STRING)
    private String parentUuid;
    @EntityField(name = "父组件id", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "告警类型", type = ApiParamType.LONG)
    private Long alertType;
    @EntityField(name = "处理器", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "处理器名称", type = ApiParamType.STRING)
    private String handlerName;
    @EntityField(name = "处理器图标", type = ApiParamType.STRING)
    private String handlerIcon;
    @EntityField(name = "事件", type = ApiParamType.STRING)
    private String event;
    @EntityField(name = "事件名称", type = ApiParamType.STRING)
    private String eventName;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;
    @EntityField(name = "子节点", type = ApiParamType.JSONARRAY)
    private List<AlertEventHandlerVo> handlerList;
    @EntityField(name = "是否异步插件", type = ApiParamType.BOOLEAN)
    private Integer isAsync;
    @EntityField(name = "类型id", type = ApiParamType.LONG)
    private Long typeId;
    @EntityField(name = "类型名称", type = ApiParamType.STRING)
    private String typeLabel;
    @EntityField(name = "熔断策略列表", type = ApiParamType.JSONARRAY)
    private List<AlertEventHandlerBreakerPolicyVo> breakerPolicyList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlertEventHandlerVo)) return false;
        AlertEventHandlerVo that = (AlertEventHandlerVo) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    public Integer getIsAsync() {
        if (isAsync == null && StringUtils.isNotBlank(handler)) {
            IAlertEventHandler h = AlertEventHandlerFactory.getHandler(handler);
            if (h != null) {
                isAsync = h.isAsync() ? 1 : 0;
            }
        }
        return isAsync;
    }

    public void setIsAsync(Integer isAsync) {
        this.isAsync = isAsync;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public List<AlertEventHandlerBreakerPolicyVo> getBreakerPolicyList() {
        return breakerPolicyList;
    }

    public void setBreakerPolicyList(List<AlertEventHandlerBreakerPolicyVo> breakerPolicyList) {
        this.breakerPolicyList = breakerPolicyList;
    }

    public void addHandler(AlertEventHandlerVo handler) {
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        handlerList.add(handler);
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEventName() {
        if (StringUtils.isNotBlank(event)) {
            eventName = AlertEventType.getLabel(event);
        }
        return eventName;
    }

    public String getHandlerIcon() {
        if (StringUtils.isNotBlank(handler)) {
            IAlertEventHandler h = AlertEventHandlerFactory.getHandler(handler);
            if (h != null) {
                handlerIcon = h.getIcon();
            }
        }
        return handlerIcon;
    }

    public String getHandlerName() {
        if (StringUtils.isNotBlank(handler)) {
            IAlertEventHandler h = AlertEventHandlerFactory.getHandler(handler);
            if (h != null) {
                handlerName = h.getLabel();
            }
        }
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getAlertType() {
        return alertType;
    }

    public void setAlertType(Long alertType) {
        this.alertType = alertType;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandler() {
        return handler;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public List<AlertEventHandlerVo> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(List<AlertEventHandlerVo> handlerList) {
        this.handlerList = handlerList;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public JSONObject getConfig() {
        if (config == null && StringUtils.isNotBlank(configStr)) {
            try {
                config = JSON.parseObject(configStr);
            } catch (Exception ignored) {

            }
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public String getConfigStr() {
        if (config != null) {
            configStr = JSON.toJSONString(config);
        }
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}
