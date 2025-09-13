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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警属性定义，用于产生搜索条件的属性列表
 */
public class AlertAttrDefineVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;//扩展属性有id,内置属性没有id
    @EntityField(name = "唯一标识", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "显示名", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "种类", type = ApiParamType.STRING)
    private String kind = "const";//const或attr
    @EntityField(name = "类型", type = ApiParamType.STRING)
    private String type = "text";
    @EntityField(name = "是否置顶", type = ApiParamType.INTEGER)
    private Integer isTop = 0;
    @EntityField(name = "条件表达式列表", type = ApiParamType.JSONARRAY)
    private List<String> expressionList;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @EntityField(name = "是否用整行显示", type = ApiParamType.BOOLEAN)
    private boolean isWholeRow = false;
    @EntityField(name = "freemarker代码片段", type = ApiParamType.STRING)
    private String freemarkerSnippet;
    @JSONField(serialize = false)
    private boolean isCondition = false;//是否能用在条件判断
    @JSONField(serialize = false)
    private boolean isTemplate = false;//是否能用在freemarker模板
    @JSONField(serialize = false)
    private boolean isSearch = false;//是否能用作搜索条件
    @EntityField(name = "是否用单独页签显示", type = ApiParamType.BOOLEAN)
    private boolean isTab = false;

    public boolean isWholeRow() {
        return isWholeRow;
    }

    public boolean getIsTab() {
        return isTab;
    }

    public AlertAttrDefineVo setIsTab(boolean isTab) {
        this.isTab = isTab;
        return this;
    }

    public AlertAttrDefineVo setWholeRow(boolean wholeRow) {
        this.isWholeRow = wholeRow;
        return this;
    }

    public AlertAttrDefineVo() {

    }

    public boolean isCondition() {
        return isCondition;
    }


    public boolean isTemplate() {
        return isTemplate;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public String getFreemarkerSnippet() {
        return freemarkerSnippet;
    }

    public AlertAttrDefineVo setFreemarkerSnippet(String freemarkerSnippet) {
        this.freemarkerSnippet = freemarkerSnippet;
        return this;
    }

    public String getKind() {
        return kind;
    }

    public AlertAttrDefineVo setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public AlertAttrDefineVo(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public AlertAttrDefineVo setId(Long id) {
        this.id = id;
        return this;
    }

    public AlertAttrDefineVo setIsTop(Integer isTop) {
        this.isTop = isTop;
        return this;
    }

    public AlertAttrDefineVo(Long id, String name, String label, String kind, String type, List<String> expressionList, JSONObject config) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.kind = kind;
        this.type = type;
        this.expressionList = expressionList;
        this.config = config;
    }

    public AlertAttrDefineVo(String name, String label, String kind, String type) {
        this.name = name;
        this.label = label;
        this.kind = kind;
        this.type = type;
    }

    public AlertAttrDefineVo(String name, String label, String type, List<String> expressionList, JSONObject config) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.expressionList = expressionList;
        this.config = config;
    }

    public AlertAttrDefineVo(String name, String label, String type, JSONObject config) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public AlertAttrDefineVo setName(String name) {
        this.name = name;
        return this;
    }

    public AlertAttrDefineVo setIsCondition(boolean isCondition) {
        this.isCondition = isCondition;
        return this;
    }

    public AlertAttrDefineVo setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
        return this;
    }

    public AlertAttrDefineVo setIsTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public AlertAttrDefineVo setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getType() {
        return type;
    }

    public AlertAttrDefineVo setType(String type) {
        this.type = type;
        return this;
    }

    public List<String> getExpressionList() {
        if (expressionList == null) {
            expressionList = new ArrayList<String>() {{
                this.add("equal");
                this.add("notequal");
                this.add("like");
                this.add("notlike");
                this.add("is-null");
                this.add("is-not-null");
            }};
        }
        return expressionList;
    }

    public AlertAttrDefineVo setExpressionList(List<String> expressionList) {
        this.expressionList = expressionList;
        return this;
    }

    public JSONObject getConfig() {
        return config;
    }

    public AlertAttrDefineVo setConfig(JSONObject config) {
        this.config = config;
        return this;
    }
}
