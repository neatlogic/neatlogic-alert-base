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

public enum AlertAttrType {
    NUMBER("number", "数字"),
    STRING("string", "文本"),
    DATETIME("datetime", "日期时间"),
    JSONOBJ("jsonobj", "json对象"),
    JSONLIST("jsonlist", "json数组");

    private final String value;
    private final String text;

    AlertAttrType(String _value, String _text) {
        this.value = _value;
        this.text = _text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
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
