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

public enum AlertEventType {
    ALERT_INPUT("ALERT_INPUT", "接入告警", "告警接入时，经过转换插件转换后触发此事件，这个时刻可以决定告警是否需要保存到数据库"),
    ALERT_SAVE("ALERT_SAVE", "创建告警", "以新告警的方式保存时，触发此事件"),
    ALERT_CONVERGE("ALERT_CONVERGE", "收敛告警", "以收敛告警（存在父告警）的方式被保存时，触发此事件"),
    ALERT_CONVERGE_IN("ALERT_CONVERGE_IN", "子告警加入", "当父告警有子告警加入时，触发此事件"),
    ALERT_CONVERGE_OUT("ALERT_CONVERGE_OUT", "子告警移除", "当父告警有子告警移出或被删除时，触发此事件"),
    ALERT_STATUE_CHANGE("ALERT_STATUS_CHANGE", "更新告警状态", "告警状态发生变化时，触发此事件"),
    ALERT_CLOSE("ALERT_CLOSE", "关闭告警", "告警关闭时，触发此事件"),
    ALERT_OPEN("ALERT_OPEN", "打开告警", "告警重新打开时，触发此事件"),
    ALERT_DELETE("ALERT_DELETE", "删除告警", "告警删除时，触发此事件"),
    ALERT_SUPPRESS("ALERT_SUPPRESS", "屏蔽告警", "成功触发屏蔽策略时，触发此事件");

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
        return label;
    }

    public String getDescription() {
        return description;
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
