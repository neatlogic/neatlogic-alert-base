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
