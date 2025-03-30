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
import neatlogic.framework.alert.enums.AlertAttrType;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AlertAttrTypeVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "唯一标识", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "类型", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "类型名称", type = ApiParamType.STRING)
    private String typeName;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @EntityField(name = "是否作为普通属性显示", type = ApiParamType.INTEGER)
    private Integer isNormal;

    @JSONField(serialize = false)
    private String configStr;
    private List<String> expressionList;

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

    public String getTypeName() {
        if (StringUtils.isNotBlank(type)) {
            typeName = AlertAttrType.getText(type);
        }
        return typeName;
    }

    public List<String> getExpressionList() {
        if (StringUtils.isNotBlank(type)) {
            expressionList = AlertAttrType.getExpressionList(type);
        }
        return expressionList;
    }


    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public JSONObject getConfig() {
        if (StringUtils.isNotBlank(type)) {
            config = AlertAttrType.getConfig(type,this);
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

    public Integer getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(Integer isNormal) {
        this.isNormal = isNormal;
    }
}
