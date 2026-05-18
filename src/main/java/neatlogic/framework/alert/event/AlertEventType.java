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

package neatlogic.framework.alert.event;

import neatlogic.framework.util.$;

public enum AlertEventType {
    ALERT_INPUT("ALERT_INPUT", "term.alert.event.inputname", "term.alert.event.inputdesc"),
    ALERT_SAVE("ALERT_SAVE", "term.alert.event.savename", "term.alert.event.savedesc"),
    ALERT_CONVERGE("ALERT_CONVERGE", "term.alert.event.convergename", "term.alert.event.convergedesc"),
    ALERT_CONVERGE_IN("ALERT_CONVERGE_IN", "term.alert.event.convergeinname", "term.alert.event.convergeindesc"),
    ALERT_CONVERGE_OUT("ALERT_CONVERGE_OUT", "term.alert.event.convergeoutname", "term.alert.event.convergeoutdesc"),
    ALERT_STATUE_CHANGE("ALERT_STATUS_CHANGE", "term.alert.event.statuschangename", "term.alert.event.statuschangedesc"),
    ALERT_CLOSE("ALERT_CLOSE", "term.alert.event.alertclosename", "term.alert.event.alertclosedesc"),
    ALERT_OPEN("ALERT_OPEN", "term.alert.event.alertopenname", "term.alert.event.alertopendesc"),
    ALERT_DELETE("ALERT_DELETE", "term.alert.event.alertdeletename", "term.alert.event.alertdeletedesc"),
    ALERT_SUPPRESS("ALERT_SUPPRESS", "term.alert.event.alertsuppressname", "term.alert.event.alertsuppressdesc");

    private final String name;
    private final String label;
    private final String description;

    AlertEventType(String name, String label, String description) {
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return $.t(label);
    }

    public String getDescription() {
        return $.t(description);
    }

    public static AlertEventType get(String name) {
        for (AlertEventType type : AlertEventType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public static String getLabel(String name) {
        for (AlertEventType s : AlertEventType.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }


}
