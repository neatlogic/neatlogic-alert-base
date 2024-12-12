/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.alert.enums;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertAttrDefineVo;
import neatlogic.framework.common.dto.ValueTextVo;

import java.util.ArrayList;
import java.util.List;

public class AlertAttr {
    public static List<AlertAttrDefineVo> getConstAttrList() {
        List<AlertAttrDefineVo> attrList = new ArrayList<>();
        attrList.add(new AlertAttrDefineVo("const_id", "id"));
        attrList.add(new AlertAttrDefineVo("const_level", "级别", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dynamicUrl", "/api/rest/alert/level/list");
            this.put("valueName", "level");
            this.put("textName", "label");
        }}));
        attrList.add(new AlertAttrDefineVo("const_title", "标题"));
        attrList.add(new AlertAttrDefineVo("const_alertTime", "告警时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm");
        }}));
        attrList.add(new AlertAttrDefineVo("const_type", "类型", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dynamicUrl", "/api/rest/alert/alerttype/search");
            this.put("rootName", "tbodyList");
            this.put("valueName", "id");
            this.put("textName", "label");
        }}));
        attrList.add(new AlertAttrDefineVo("const_status", "状态", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dataList", new ArrayList<ValueTextVo>() {{
                this.add(new ValueTextVo() {{
                    this.setValue("unresponded");
                    this.setText("未响应");
                }});
                this.add(new ValueTextVo() {{
                    this.setValue("responded");
                    this.setText("已响应");
                }});
            }});
        }}));
        attrList.add(new AlertAttrDefineVo("const_source", "来源"));
        attrList.add(new AlertAttrDefineVo("const_entityType", "实体类型"));
        attrList.add(new AlertAttrDefineVo("const_entityName", "实体名称"));
        attrList.add(new AlertAttrDefineVo("const_ip", "IP"));
        attrList.add(new AlertAttrDefineVo("const_port", "端口"));

        return attrList;
    }
}
