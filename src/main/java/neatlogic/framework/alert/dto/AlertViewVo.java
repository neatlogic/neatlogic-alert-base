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
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AlertViewVo extends BaseEditorVo {
    private Long id;
    private String name;
    private String label;
    private Integer isActive;
    private JSONObject config;
    private String configStr;
    private List<String> authList;
    private List<AlertViewAuthVo> alertViewAuthList;
    private String userId;
    private List<String> teamUuidList;
    private List<String> roleUuidList;
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
