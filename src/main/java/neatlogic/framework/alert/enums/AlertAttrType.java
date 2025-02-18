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

import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;

public enum AlertAttrType {
    NUMBER("number", "数字", new ArrayList<String>() {{
        this.add("equal");
        this.add("notequal");
        this.add("gt");
        this.add("lt");
        this.add("gte");
        this.add("lte");
        this.add("is-null");
        this.add("is-not-null");
    }}),
    TEXT("text", "文本", new ArrayList<String>() {{
        this.add("equal");
        this.add("notequal");
        this.add("like");
        this.add("notlike");
        this.add("is-null");
        this.add("is-not-null");
    }}),
    DATETIME("datetime", "日期时间", new ArrayList<String>() {{
        this.add("range");
        this.add("is-null");
        this.add("is-not-null");
    }});
    //JSONOBJ("jsonobj", "json对象"),
    //JSONLIST("jsonlist", "json数组");

    private final String value;
    private final String text;
    private List<String> expressionList;

    AlertAttrType(String _value, String _text, List<String> _expressionList) {
        this.value = _value;
        this.text = _text;
        this.expressionList = _expressionList;
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
