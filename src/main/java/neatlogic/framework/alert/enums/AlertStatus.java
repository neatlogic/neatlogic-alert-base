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
