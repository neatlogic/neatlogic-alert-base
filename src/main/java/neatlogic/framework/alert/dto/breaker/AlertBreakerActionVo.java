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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.breaker.action.AlertBreakerActionHandlerFactory;
import neatlogic.framework.alert.breaker.action.IAlertBreakerActionHandler;
import org.apache.commons.lang3.StringUtils;

public class AlertBreakerActionVo {
    private String uuid;
    private String name;
    private String handler;
    private String handlerLabel;
    private Integer isActive;
    private JSONObject config;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        if (StringUtils.isBlank(name)) {
            name = getHandlerLabel();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandlerLabel() {
        if (StringUtils.isBlank(handlerLabel) && StringUtils.isNotBlank(handler)) {
            IAlertBreakerActionHandler actionHandler = AlertBreakerActionHandlerFactory.getHandler(handler);
            if (actionHandler != null) {
                handlerLabel = actionHandler.getLabel();
            }
        }
        return handlerLabel;
    }

    public void setHandlerLabel(String handlerLabel) {
        this.handlerLabel = handlerLabel;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public JSONObject getConfig() {
        if (config == null) {
            config = new JSONObject();
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }
}
