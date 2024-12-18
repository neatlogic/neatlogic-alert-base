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

package neatlogic.framework.alert.event;

public enum AlertEventType {
    ALERT_INPUT("ALERT_INPUT", "输入告警", "告警输入时，经过转换插件转换后触发此事件，这个时刻可以决定告警是否需要保存到数据库"),
    ALERT_SAVE("ALERT_SAVE", "保存告警", "告警保存时触发此事件"),
    ALERT_STATUE_CHANGE("ALERT_STATUS_CHANGE", "更新告警状态", "告警状态发生变化时触发此事件"),
    ALERT_DELETE("ALERT_DELETE", "删除告警", "告警删除时触发此事件");

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


    public static String getLabel(String name) {
        for (AlertEventType s : AlertEventType.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }


}
