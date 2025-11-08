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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

import java.util.List;
import java.util.stream.Collectors;

public class AlertTypeVo extends BaseEditorVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "唯一标识", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "属性类型列表", type = ApiParamType.JSONARRAY)
    private List<AlertAttrTypeVo> attrTypeList;
    @EntityField(name = "属性类型id列表", type = ApiParamType.JSONARRAY)
    private List<Long> attrTypeIdList;
    @EntityField(name = "转换器列表", type = ApiParamType.JSONARRAY)
    private List<AlertTypeAdaptorVo> adaptorList;
    @EntityField(name = "事件处理器列表", type = ApiParamType.JSONARRAY)
    private List<AlertEventHandlerVo> alertEventHandlerList;


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

    public List<AlertEventHandlerVo> getAlertEventHandlerList() {
        return alertEventHandlerList;
    }

    public void setAlertEventHandlerList(List<AlertEventHandlerVo> alertEventHandlerList) {
        this.alertEventHandlerList = alertEventHandlerList;
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


    public List<AlertAttrTypeVo> getAttrTypeList() {
        return attrTypeList;
    }

    public void setAttrTypeList(List<AlertAttrTypeVo> attrTypeList) {
        this.attrTypeList = attrTypeList;
    }

    public List<Long> getAttrTypeIdList() {
        if (attrTypeIdList == null && attrTypeList != null) {
            attrTypeIdList = attrTypeList.stream().map(AlertAttrTypeVo::getId).collect(Collectors.toList());
        }
        return attrTypeIdList;
    }

    public void setAttrTypeIdList(List<Long> attrTypeIdList) {
        this.attrTypeIdList = attrTypeIdList;
    }

    public List<AlertTypeAdaptorVo> getAdaptorList() {
        return adaptorList;
    }

    public void setAdaptorList(List<AlertTypeAdaptorVo> adaptorList) {
        this.adaptorList = adaptorList;
    }
}
