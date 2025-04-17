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

import neatlogic.framework.common.constvalue.IUserType;
import neatlogic.framework.dto.UserTypeVo;

import java.util.HashMap;
import java.util.Map;

public enum AlertUserType implements IUserType {
    WORKER("worker", "处理人", true),
    WORKER_TEAM("workerteam", "处理组", true);

    private final String value;
    private final String text;
    private final boolean isShow;

    AlertUserType(String _value, String _text, boolean _isShow) {
        this.value = _value;
        this.text = _text;
        this.isShow = _isShow;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public boolean getIsShow() {
        return isShow;
    }

    public static String getValue(String _status) {
        for (AlertUserType s : AlertUserType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _value) {
        for (AlertUserType s : AlertUserType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getText();
            }
        }
        return "";
    }


    @Override
    public UserTypeVo getUserType() {
        UserTypeVo vo = new UserTypeVo();
        vo.setModuleId(getModuleId());
        Map<String, String> map = new HashMap<>();
        for (AlertUserType type : AlertUserType.values()) {
            map.put(type.getValue(), type.getText());
        }
        vo.setValues(map);
        return vo;
    }

    @Override
    public String getModuleId() {
        return "alert";
    }
}
