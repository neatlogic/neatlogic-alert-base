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

package neatlogic.framework.alert.dto.condition;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

public class ConditionVo implements Serializable {
    private String uuid;
    private String name;
    private String id;
    private String type;
    private String expression;
    private JSONArray valueList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public JSONArray getValueList() {
        return valueList;
    }

    public void setValueList(JSONArray valueList) {
        this.valueList = valueList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String buildScript() {
        return "calculate('" + expression + "', data['" + this.getName() + "'], condition['" + this.getName() + "'], define['" + this.getName() + "'])";
    }


}
