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
    @EntityField(name = "成员数量", type = ApiParamType.INTEGER)
    private int enumCount;
    @EntityField(name = "是否置顶", type = ApiParamType.INTEGER)
    private Integer isTop;
    @EntityField(name = "是否独立一行展示", type = ApiParamType.INTEGER)
    private Integer isRow;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "是否独立页签展示", type = ApiParamType.INTEGER)
    private Integer isTab;
    @EntityField(name = "是否保存到ES", type = ApiParamType.INTEGER)
    private Integer isIndex;

    @JSONField(serialize = false)
    private String configStr;
    private List<String> expressionList;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public int getEnumCount() {
        return enumCount;
    }

    public void setEnumCount(int enumCount) {
        this.enumCount = enumCount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsTab() {
        return isTab;
    }

    public void setIsTab(Integer isTab) {
        this.isTab = isTab;
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

    public Integer getIsRow() {
        return isRow;
    }

    public void setIsRow(Integer isRow) {
        this.isRow = isRow;
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
            config = AlertAttrType.getConfig(type, this);
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

    public Integer getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(Integer isIndex) {
        this.isIndex = isIndex;
    }
}
