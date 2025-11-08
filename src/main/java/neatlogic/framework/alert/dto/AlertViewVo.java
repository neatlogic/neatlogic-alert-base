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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AlertViewVo extends BaseEditorVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "唯一标识", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;
    @EntityField(name = "授权uuid列表", type = ApiParamType.JSONARRAY)
    private List<String> authList;
    @EntityField(name = "授权列表", type = ApiParamType.JSONARRAY)
    private List<AlertViewAuthVo> alertViewAuthList;
    @JSONField(serialize = false)
    private String userId;
    @JSONField(serialize = false)
    private List<String> teamUuidList;
    @JSONField(serialize = false)
    private List<String> roleUuidList;
    @JSONField(serialize = false)
    private boolean isAdmin = false;
    @EntityField(name = "目录id", type = ApiParamType.LONG)
    private Long catalogId;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private int sort;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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

    public List<String> getAuthList() {
        if (authList == null && alertViewAuthList != null) {
            authList = new ArrayList<>();
            for (AlertViewAuthVo authVo : alertViewAuthList) {
                authList.add(authVo.getAuthType() + "#" + authVo.getAuthUuid());
            }
        }
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }

    public List<AlertViewAuthVo> getAlertViewAuthList() {
        if (alertViewAuthList == null && authList != null) {
            alertViewAuthList = new ArrayList<>();
            for (String auth : authList) {
                AlertViewAuthVo authVo = new AlertViewAuthVo();
                authVo.setViewId(this.id);
                authVo.setAuthType(auth.split("#")[0]);
                authVo.setAuthUuid(auth.split("#")[1]);
                alertViewAuthList.add(authVo);
            }
        }
        return alertViewAuthList;
    }

    public void setAlertViewAuthList(List<AlertViewAuthVo> alertViewAuthList) {
        this.alertViewAuthList = alertViewAuthList;
    }

    public String getUserId() {
        return UserContext.get().getUserUuid(true);
    }

    public List<String> getTeamUuidList() {
        return UserContext.get().getTeamUuidList();
    }


    public List<String> getRoleUuidList() {
        return UserContext.get().getRoleUuidList();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
