/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
        this.add("is-null");
        this.add("is-not-null");
    }}, new JSONObject() {{
        this.put("transfer", true);
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
