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
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

public class AlertEventHandlerBreakerPolicyVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "事件插件实例id", type = ApiParamType.LONG)
    private Long eventHandlerId;
    @EntityField(name = "熔断策略id", type = ApiParamType.LONG)
    private Long policyId;
    @EntityField(name = "熔断策略名称", type = ApiParamType.STRING)
    private String policyName;
    @EntityField(name = "熔断策略插件", type = ApiParamType.STRING)
    private String policyHandler;
    @EntityField(name = "熔断策略插件名称", type = ApiParamType.STRING)
    private String policyHandlerLabel;
    @EntityField(name = "熔断策略配置", type = ApiParamType.JSONOBJECT)
    private JSONObject policyConfig;
    @JSONField(serialize = false)
    private String policyConfigStr;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private Integer sort;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventHandlerId() {
        return eventHandlerId;
    }

    public void setEventHandlerId(Long eventHandlerId) {
        this.eventHandlerId = eventHandlerId;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
