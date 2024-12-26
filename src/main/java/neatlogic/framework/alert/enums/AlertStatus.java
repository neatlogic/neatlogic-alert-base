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

public enum AlertStatus {
    NEW("new", "新告警", "green", null),
    CONFIRMED("confirmed", "已确认", "lime", null),
    PROCESSING("processing", "处理中", null, "processing"),
    RESOLVED("resolved", "已处理", "#2db7f5", null),
    CLOSED("closed", "已关闭", null, "default");

    private final String value;
    private final String text;
    private final String color;
    private final String status;

    AlertStatus(String _value, String _text, String _color, String _status) {
        this.value = _value;
        this.text = _text;
        this.color = _color;
        this.status = _status;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
    }

    public String getColor() {
        return color;
    }

    public String getStatus() {
        return status;
    }

    public static String getColor(String name) {
        for (AlertStatus s : AlertStatus.values()) {
            if (s.getValue().equals(name)) {
                return s.getColor();
            }
        }
        return "";
    }

    public static String getStatus(String name) {
        for (AlertStatus s : AlertStatus.values()) {
            if (s.getValue().equals(name)) {
                return s.getStatus();
            }
        }
        return "";
    }

    public static String getText(String name) {
        for (AlertStatus s : AlertStatus.values()) {
            if (s.getValue().equals(name)) {
                return s.getText();
            }
        }
        return "";
    }
}
