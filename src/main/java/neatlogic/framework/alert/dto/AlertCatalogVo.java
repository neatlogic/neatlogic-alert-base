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
