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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlertAttr {

    public static List<AlertAttrDefineVo> getConstAttrList(String... excludeColumns) {
        List<AlertAttrDefineVo> attrList = new ArrayList<>();
        attrList.add(new AlertAttrDefineVo("const_title", "标题"));
        attrList.add(new AlertAttrDefineVo("const_id", "id"));
        attrList.add(new AlertAttrDefineVo("const_level", "级别", "select", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
            this.add("gt");
            this.add("lt");
            this.add("gte");
            this.add("lte");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dynamicUrl", "/api/rest/alert/level/list");
            this.put("valueName", "level");
            this.put("textName", "label");
        }}));
        attrList.add(new AlertAttrDefineVo("const_alertTime", "创建时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm:ss");
        }}));
        attrList.add(new AlertAttrDefineVo("const_updateTime", "更新时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm:ss");
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
                    this.setValue("new");
                    this.setText("新告警");
                }});
                this.add(new ValueTextVo() {{
                    this.setValue("confirmed");
                    this.setText("已响应");
                }});
                this.add(new ValueTextVo() {{
                    this.setValue("processing");
                    this.setText("处理中");
                }});
                this.add(new ValueTextVo() {{
                    this.setValue("resolved");
                    this.setText("已解决");
                }});
                this.add(new ValueTextVo() {{
                    this.setValue("closed");
                    this.setText("已关闭");
                }});
            }});
        }}));
        attrList.add(new AlertAttrDefineVo("const_source", "来源"));
        attrList.add(new AlertAttrDefineVo("const_entityType", "实体类型"));
        attrList.add(new AlertAttrDefineVo("const_entityName", "实体名称"));
        attrList.add(new AlertAttrDefineVo("const_ip", "IP"));
        attrList.add(new AlertAttrDefineVo("const_port", "端口"));
        if (excludeColumns != null && excludeColumns.length > 0) {
            return attrList.stream().filter(d -> Arrays.stream(excludeColumns).noneMatch(ed -> ed.equals(d.getName()))).collect(Collectors.toList());
        }
        return attrList;
    }
}
