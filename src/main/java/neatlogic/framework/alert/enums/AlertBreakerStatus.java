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

public enum AlertBreakerStatus {
    PASS("pass", "alert.breakerstatus.pass"),
    OPEN("open", "alert.breakerstatus.open"),
    FAILED("failed", "alert.breakerstatus.failed");

    private final String value;
    private final String text;

    AlertBreakerStatus(String _value, String _text) {
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
        for (AlertBreakerStatus s : AlertBreakerStatus.values()) {
            if (s.getValue().equalsIgnoreCase(name) || s.name().equalsIgnoreCase(name)) {
                return s.getText();
            }
        }
        return "";
    }
}
