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

package neatlogic.framework.alert.enums;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertAttrTypeVo;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;

public enum AlertAttrType implements IEnum<JSONObject> {
    NUMBER("number", "数字", new ArrayList<>() {{
        this.add("equal");
        this.add("notequal");
        this.add("gt");
        this.add("lt");
        this.add("gte");
        this.add("lte");
        this.add("is-null");
        this.add("is-not-null");
    }}, null),
    TEXT("text", "文本", new ArrayList<>() {{
        this.add("equal");
        this.add("notequal");
        this.add("like");
        this.add("notlike");
        this.add("is-null");
        this.add("is-not-null");
    }}, null),

    ENUM("enum", "枚举", new ArrayList<>() {{
        this.add("equal");
        this.add("notequal");
        this.add("like");
        this.add("notlike");
        this.add("is-null");
        this.add("is-not-null");
    }}, new JSONObject() {{
        this.put("transfer", true);
        this.put("multiple", true);
        this.put("dynamicUrl", "/api/rest/alert/attrenum/search");
        this.put("params", new JSONObject() {{
            this.put("attrType", "#{alertAttrTypeVo.id}");
        }});
        this.put("rootName", "tbodyList");
        this.put("valueName", "value");
        this.put("textName", "text");
    }}),
    DATETIME("datetime", "日期时间", new ArrayList<>() {{
        this.add("range");
        this.add("is-null");
        this.add("is-not-null");
    }}, null),
    HTML("html", "html内容", new ArrayList<>() {
        {
            this.add("equal");
            this.add("notequal");
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }
    }, null),
    JSON("json", "json内容", new ArrayList<>() {{
        this.add("equal");
        this.add("notequal");
        this.add("like");
        this.add("notlike");
        this.add("is-null");
        this.add("is-not-null");
    }}, null),
    CSV("csv", "CSV内容", new ArrayList<>() {{
        this.add("equal");
        this.add("notequal");
        this.add("like");
        this.add("notlike");
        this.add("is-null");
        this.add("is-not-null");
    }}, null);

    private final String value;
    private final String text;
    private final List<String> expressionList;
    private final JSONObject config;

    AlertAttrType(String _value, String _text, List<String> _expressionList, JSONObject _config) {
        this.value = _value;
        this.text = _text;
        this.expressionList = _expressionList;
        this.config = _config;
    }

    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> resultList = new ArrayList<>();
        for (AlertAttrType e : values()) {
            JSONObject obj = new JSONObject();
            obj.put("value", e.getValue());
            obj.put("text", e.getText());
            resultList.add(obj);
        }
        return resultList;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
    }

    public List<String> getExpressionList() {
        return expressionList;
    }

    public JSONObject getConfig(AlertAttrTypeVo alertAttrTypeVo) {
        if (alertAttrTypeVo != null && config != null) {
            String configStr = com.alibaba.fastjson.JSON.toJSONString(config);
            configStr = configStr.replace("#{alertAttrTypeVo.id}", alertAttrTypeVo.getId().toString());
            return com.alibaba.fastjson.JSON.parseObject(configStr);
        }
        return config;
    }

    public static JSONObject getConfig(String name, AlertAttrTypeVo alertAttrTypeVo) {
        for (AlertAttrType s : AlertAttrType.values()) {
            if (s.getValue().equals(name)) {
                return s.getConfig(alertAttrTypeVo);
            }
        }
        return null;
    }


    public static List<String> getExpressionList(String name) {
        for (AlertAttrType s : AlertAttrType.values()) {
            if (s.getValue().equals(name)) {
                return s.getExpressionList();
            }
        }
        return new ArrayList<>();
    }

    public static String getText(String name) {
        for (AlertAttrType s : AlertAttrType.values()) {
            if (s.getValue().equals(name)) {
                return s.getText();
            }
        }
        return "";
    }
}
