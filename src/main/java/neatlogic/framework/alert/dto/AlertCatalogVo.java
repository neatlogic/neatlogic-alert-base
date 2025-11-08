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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

import java.util.ArrayList;
import java.util.List;

public class AlertCatalogVo extends BaseEditorVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "授权uuid列表", type = ApiParamType.JSONARRAY)
    private List<String> authList;
    @EntityField(name = "授权列表", type = ApiParamType.JSONARRAY)
    private List<AlertCatalogAuthVo> alertCatalogAuthList;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "视图列表", type = ApiParamType.JSONARRAY)
    private List<AlertViewVo> viewList;

    @JSONField(serialize = false)
    private String userId;
    @JSONField(serialize = false)
    private List<String> teamUuidList;
    @JSONField(serialize = false)
    private List<String> roleUuidList;
    @JSONField(serialize = false)
    private boolean isAdmin = false;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
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

    public int getSort() {
        return sort;
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

    public List<AlertViewVo> getViewList() {
        return viewList;
    }

    public void setViewList(List<AlertViewVo> viewList) {
        this.viewList = viewList;
    }

    public List<String> getAuthList() {
        if (authList == null && alertCatalogAuthList != null) {
            authList = new ArrayList<>();
            for (AlertCatalogAuthVo authVo : alertCatalogAuthList) {
                authList.add(authVo.getAuthType() + "#" + authVo.getAuthUuid());
            }
        }
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }

    public List<AlertCatalogAuthVo> getAlertCatalogAuthList() {
        if (alertCatalogAuthList == null && authList != null) {
            alertCatalogAuthList = new ArrayList<>();
            for (String auth : authList) {
                AlertCatalogAuthVo authVo = new AlertCatalogAuthVo();
                authVo.setCatalogId(this.id);
                authVo.setAuthType(auth.split("#")[0]);
                authVo.setAuthUuid(auth.split("#")[1]);
                alertCatalogAuthList.add(authVo);
            }
        }
        return alertCatalogAuthList;
    }

    public void setAlertCatalogAuthList(List<AlertCatalogAuthVo> alertCatalogAuthList) {
        this.alertCatalogAuthList = alertCatalogAuthList;
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
