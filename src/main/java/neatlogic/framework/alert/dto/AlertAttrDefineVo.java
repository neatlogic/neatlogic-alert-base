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

import java.util.ArrayList;
import java.util.List;

/**
 * 告警属性定义，用于产生搜索条件的属性列表
 */
public class AlertAttrDefineVo {
    private Long id;//扩展属性有id,内置属性没有id
    private String name;
    private String label;
    private String kind = "const";//const或attr
    private String type = "text";
    private List<String> expressionList;
    private JSONObject config;
    private boolean isWholeRow = false;
    private String freemarkerSnippet;

    public boolean isWholeRow() {
        return isWholeRow;
    }

    public AlertAttrDefineVo setWholeRow(boolean wholeRow) {
        isWholeRow = wholeRow;
        return this;
    }

    public AlertAttrDefineVo() {

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

    public void setKind(String kind) {
        this.kind = kind;
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
