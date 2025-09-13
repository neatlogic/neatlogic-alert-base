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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertAttrDefineVo;
import neatlogic.framework.common.dto.ValueTextVo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlertAttr {
    public static List<AlertAttrDefineVo> getAllConstAttrList() {
        return new ArrayList<>(attrList);
    }

    public static List<AlertAttrDefineVo> getSearchConstAttrList() {
        List<AlertAttrDefineVo> returnList = new ArrayList<>(attrList);
        returnList.removeIf(d -> !d.isSearch());
        return returnList;
    }

    public static List<AlertAttrDefineVo> getTemplateConstAttrList() {
        List<AlertAttrDefineVo> returnList = new ArrayList<>(attrList);
        returnList.removeIf(d -> !d.isTemplate());
        return returnList;
    }

    public static List<AlertAttrDefineVo> getConditionConstAttrList() {
        List<AlertAttrDefineVo> returnList = new ArrayList<>(attrList);
        returnList.removeIf(d -> !d.isCondition());
        return returnList;
    }

    private static final List<AlertAttrDefineVo> attrList = new ArrayList<>();

    static {
        attrList.add(new AlertAttrDefineVo("const_id", "id", "text", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
        }}, null)
                .setFreemarkerSnippet("${DATA.const_id}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_uniqueKey", "唯一键", "text", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
            this.add("is-null");
            this.add("is-not-null");
        }}, null).setFreemarkerSnippet("${DATA.const_uniqueKey}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_title", "标题", "text", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject()).setWholeRow(true).setFreemarkerSnippet("${DATA.const_title}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));

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
        }}).setFreemarkerSnippet("${DATA.const_level}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo().setName("const_levelLabel").setLabel("级别名称")
                .setFreemarkerSnippet("${DATA.const_levelLabel}")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_type", "类型", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("dynamicUrl", "/api/rest/alert/alerttype/search");
            this.put("rootName", "tbodyList");
            this.put("valueName", "id");
            this.put("textName", "label");
        }}).setFreemarkerSnippet("${DATA.const_type}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));
        //if (isExpand == 1) {

        attrList.add(new AlertAttrDefineVo()
                .setName("const_typeName")
                .setLabel("类型名称")
                .setFreemarkerSnippet("${DATA.const_typeName}").setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_isClose", "是否关闭", "select", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dataList", new ArrayList<ValueTextVo>() {
                {
                    this.add(new ValueTextVo() {{
                        this.setValue("1");
                        this.setText("是");
                    }});
                    this.add(new ValueTextVo() {{
                        this.setValue("0");
                        this.setText("否");
                    }});
                }
            });
        }}).setFreemarkerSnippet("${DATA.const_isClose}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));
        //if (isExpand == 1) {
        attrList.add(new AlertAttrDefineVo()
                .setName("const_isCloseName")
                .setLabel("是否关闭名称")
                .setFreemarkerSnippet("${DATA.const_isCloseName}").setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_status", "状态", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("dynamicUrl", "/api/rest/alert/status/list");
            this.put("valueName", "name");
            this.put("textName", "label");
        }}).setFreemarkerSnippet("${DATA.const_status}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));
        //if (isExpand == 1) {
        attrList.add(new AlertAttrDefineVo()
                .setName("const_statusName")
                .setLabel("状态名称")
                .setFreemarkerSnippet("${DATA.const_statusName}").setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_alertTime", "创建时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("inworktime");
            this.add("outworktime");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm");
        }}).setFreemarkerSnippet("${DATA.const_alertTimeStr}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));
        //if (isExpand == 1) {
        attrList.add(new AlertAttrDefineVo()
                .setName("const_alertTimeStr")
                .setLabel("创建时间（文本）")
                .setFreemarkerSnippet("${DATA.const_alertTimeStr}")
                .setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_updateTime", "更新时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm");
        }}).setFreemarkerSnippet("${DATA.const_updateTime}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));
        //if (isExpand == 1) {
        attrList.add(new AlertAttrDefineVo()
                .setName("const_updateTimeStr")
                .setLabel("更新时间（文本）")
                .setFreemarkerSnippet("${DATA.const_updateTimeStr}")
                .setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_source", "来源")
                .setType("select")
                .setExpressionList(new ArrayList<String>() {{
                    this.add("like");
                    this.add("notlike");
                    this.add("is-null");
                    this.add("is-not-null");
                }})
                .setConfig(
                        new JSONObject() {{
                            this.put("transfer", true);
                            this.put("dynamicUrl", "/api/rest/alert/source/search");
                            this.put("rootName", "tbodyList");
                            this.put("valueName", "name");
                            this.put("textName", "label");
                        }}
                )
                .setFreemarkerSnippet("${DATA.const_source}")
                .setIsSearch(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_userList", "处理人", "userselect", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("groupList", new JSONArray() {{
                this.add("user");
            }});
        }}).setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userName}\"<#if user_has_next>,</#if></#list>]")
                .setIsSearch(true)
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_userUuidList", "处理人uuid", "userselect", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("groupList", new JSONArray() {{
                this.add("user");
            }});
        }}).setIsCondition(true));

        //if (isExpand == 1) {
        //扩展属性不需要提供控件和条件，一般只是给freemarker使用
        attrList.add(new AlertAttrDefineVo().setName("const_userAccountList").setLabel("处理人账号")
                .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userAccount}\"<#if user_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_userPhoneList").setLabel("处理人电话")
                .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userPhone}\"<#if user_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("处理人邮箱")
                .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userEmail}\"<#if user_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("处理组邮箱")
                .setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamEmail}\"<#if team_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("处理组电话")
                .setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamPhone}\"<#if team_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("处理组成员账号")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userAccount}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserPhoneList").setLabel("处理组成员电话")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userPhone}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("处理组成员邮箱")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userEmail}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("处理组成员名称")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userName}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_teamList", "处理组", "userselect", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("groupList", new JSONArray() {{
                this.add("team");
            }});
        }}).setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamName}\"<#if team_has_next>,</#if></#list>]")
                .setIsSearch(true)
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_teamUuidList", "处理组uuid", "userselect", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("groupList", new JSONArray() {{
                this.add("team");
            }});
        }}).setIsCondition(true));

    }


    public static List<AlertAttrDefineVo> getConstAttrList_bak(List<String> includeColumnList, List<String> excludeColumnList, int isExpand) {
        List<AlertAttrDefineVo> attrList = new ArrayList<>();
        attrList.add(new AlertAttrDefineVo("const_id", "id", "text", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
        }}, null).setFreemarkerSnippet("${DATA.const_id}").setIsCondition(true));
        attrList.add(new AlertAttrDefineVo("const_uniqueKey", "唯一键", "text", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
            this.add("is-null");
            this.add("is-not-null");
        }}, null).setFreemarkerSnippet("${DATA.const_uniqueKey}").setIsCondition(true));
        attrList.add(new AlertAttrDefineVo("const_title", "标题", "text", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject()).setWholeRow(true).setFreemarkerSnippet("${DATA.const_title}").setIsCondition(true));
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
        }}).setFreemarkerSnippet("${DATA.const_level}").setIsCondition(true));
        if (isExpand == 1) {
            attrList.add(new AlertAttrDefineVo().setName("const_levelLabel").setLabel("级别名称")
                    .setFreemarkerSnippet("${DATA.const_levelLabel}").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_type", "类型", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("dynamicUrl", "/api/rest/alert/alerttype/search");
            this.put("rootName", "tbodyList");
            this.put("valueName", "id");
            this.put("textName", "label");
        }}).setFreemarkerSnippet("${DATA.const_type}").setIsCondition(true));
        if (isExpand == 1) {
            attrList.add(new AlertAttrDefineVo()
                    .setName("const_typeName")
                    .setLabel("类型名称")
                    .setFreemarkerSnippet("${DATA.const_typeName}").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_isClose", "是否关闭", "select", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dataList", new ArrayList<ValueTextVo>() {
                {
                    this.add(new ValueTextVo() {{
                        this.setValue("1");
                        this.setText("是");
                    }});
                    this.add(new ValueTextVo() {{
                        this.setValue("0");
                        this.setText("否");
                    }});
                }
            });
        }}).setFreemarkerSnippet("${DATA.const_isClose}").setIsCondition(true));
        if (isExpand == 1) {
            attrList.add(new AlertAttrDefineVo()
                    .setName("const_isCloseName")
                    .setLabel("是否关闭名称")
                    .setFreemarkerSnippet("${DATA.const_isCloseName}").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_status", "状态", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("dynamicUrl", "/api/rest/alert/status/list");
            this.put("valueName", "name");
            this.put("textName", "label");
        }}).setFreemarkerSnippet("${DATA.const_status}").setIsCondition(true));
        if (isExpand == 1) {
            attrList.add(new AlertAttrDefineVo()
                    .setName("const_statusName")
                    .setLabel("状态名称")
                    .setFreemarkerSnippet("${DATA.const_statusName}").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_alertTime", "创建时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("inworktime");
            this.add("outworktime");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm");
        }}).setFreemarkerSnippet("${DATA.const_alertTimeStr}").setIsCondition(true));
        if (isExpand == 1) {
            attrList.add(new AlertAttrDefineVo()
                    .setName("const_alertTimeStr")
                    .setLabel("创建时间（文本）")
                    .setFreemarkerSnippet("${DATA.const_alertTimeStr}").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_updateTime", "更新时间", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm");
        }}).setFreemarkerSnippet("${DATA.const_updateTime}").setIsCondition(true));
        if (isExpand == 1) {
            attrList.add(new AlertAttrDefineVo()
                    .setName("const_updateTimeStr")
                    .setLabel("更新时间（文本）")
                    .setFreemarkerSnippet("${DATA.const_updateTimeStr}").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_source", "来源")
                .setType("select")
                .setExpressionList(new ArrayList<String>() {{
                    this.add("like");
                    this.add("notlike");
                    this.add("is-null");
                    this.add("is-not-null");
                }})
                .setConfig(
                        new JSONObject() {{
                            this.put("transfer", true);
                            this.put("dynamicUrl", "/api/rest/alert/source/search");
                            this.put("rootName", "tbodyList");
                            this.put("valueName", "name");
                            this.put("textName", "label");
                        }}
                )
                .setFreemarkerSnippet("${DATA.const_source}").setIsCondition(true));
        attrList.add(new AlertAttrDefineVo("const_userList", "处理人", "userselect", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("groupList", new JSONArray() {{
                this.add("user");
            }});
        }}).setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userName}\"<#if user_has_next>,</#if></#list>]"));
        if (isExpand == 1) {
            //扩展属性不需要提供控件和条件，一般只是给freemarker使用
            attrList.add(new AlertAttrDefineVo().setName("const_userAccountList").setLabel("处理人账号")
                    .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userAccount}\"<#if user_has_next>,</#if></#list>]")
                    .setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_userPhoneList").setLabel("处理人电话")
                    .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userPhone}\"<#if user_has_next>,</#if></#list>]")
                    .setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("处理人邮箱")
                    .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userEmail}\"<#if user_has_next>,</#if></#list>]")
                    .setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("处理组邮箱")
                    .setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamEmail}\"<#if team_has_next>,</#if></#list>]")
                    .setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("处理组电话")
                    .setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamPhone}\"<#if team_has_next>,</#if></#list>]")
                    .setIsTemplate(true));

            attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("处理组成员账号")
                    .setFreemarkerSnippet("[" +
                            "<#list DATA.const_teamList as team>" +
                            "<#list team.userList?default([]) as user>" +
                            "\"${user.userAccount}\"<#if !team?is_last || !user?is_last>,</#if>" +
                            "</#list>" +
                            "</#list>" +
                            "]").setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_teamUserPhoneList").setLabel("处理组成员电话")
                    .setFreemarkerSnippet("[" +
                            "<#list DATA.const_teamList as team>" +
                            "<#list team.userList?default([]) as user>" +
                            "\"${user.userPhone}\"<#if !team?is_last || !user?is_last>,</#if>" +
                            "</#list>" +
                            "</#list>" +
                            "]").setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("处理组成员邮箱")
                    .setFreemarkerSnippet("[" +
                            "<#list DATA.const_teamList as team>" +
                            "<#list team.userList?default([]) as user>" +
                            "\"${user.userEmail}\"<#if !team?is_last || !user?is_last>,</#if>" +
                            "</#list>" +
                            "</#list>" +
                            "]").setIsTemplate(true));
            attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("处理组成员名称")
                    .setFreemarkerSnippet("[" +
                            "<#list DATA.const_teamList as team>" +
                            "<#list team.userList?default([]) as user>" +
                            "\"${user.userName}\"<#if !team?is_last || !user?is_last>,</#if>" +
                            "</#list>" +
                            "</#list>" +
                            "]").setIsTemplate(true));
        }
        attrList.add(new AlertAttrDefineVo("const_teamList", "处理组", "userselect", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("multiple", true);
            this.put("groupList", new JSONArray() {{
                this.add("team");
            }});
        }}).setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamName}\"<#if team_has_next>,</#if></#list>]").setIsCondition(true));
        if (CollectionUtils.isNotEmpty(excludeColumnList)) {
            return attrList.stream().filter(d -> excludeColumnList.stream().noneMatch(ed -> ed.equals(d.getName()))).collect(Collectors.toList());
        }
        return attrList;
    }
}
