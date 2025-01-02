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
import neatlogic.framework.alert.event.AlertEventHandlerFactory;
import neatlogic.framework.alert.event.AlertEventType;
import neatlogic.framework.alert.event.IAlertEventHandler;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlertEventHandlerVo {
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
