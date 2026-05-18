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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertAttrDefineVo;
import neatlogic.framework.common.dto.ValueTextVo;

import java.util.ArrayList;
import java.util.List;

public class AlertAttr {
    public static List<AlertAttrDefineVo> getAllConstAttrList() {
        return new ArrayList<>(attrList);
    }

    public static List<AlertAttrDefineVo> getColumnConstAttrList() {
        List<AlertAttrDefineVo> returnList = new ArrayList<>(attrList);
        returnList.removeIf(d -> !d.isColumn());
        return returnList;
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

    public static List<AlertAttrDefineVo> getAggregateConstAttrList() {
        List<AlertAttrDefineVo> returnList = new ArrayList<>(attrList);
        returnList.removeIf(d -> !d.getIsAggregate());
        return returnList;
    }

    private static final List<AlertAttrDefineVo> attrList = new ArrayList<>();

    static {
        attrList.add(new AlertAttrDefineVo("const_id", "id", "text", new ArrayList<>() {{
            this.add("equal");
            this.add("notequal");
        }}, null)
                .setFreemarkerSnippet("${DATA.const_id}")
                .setIsSearch(true)
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_uniqueKey", "nfae.alertattr", "text", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
            this.add("is-null");
            this.add("is-not-null");
        }}, null).setFreemarkerSnippet("${DATA.const_uniqueKey}")
                .setIsSearch(true)
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_title", "common.title", "text", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject()).setWholeRow(true).setFreemarkerSnippet("${DATA.const_title}")
                .setIsSearch(true)
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_level", "term.cmdb.alertlevel", "select", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true)
                .setIsSort(true));

        attrList.add(new AlertAttrDefineVo("const_isChild", "term.alert.issubalert", "select", new ArrayList<String>() {{
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
        }}).setFreemarkerSnippet("${DATA.const_isChild}")
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo().setName("const_levelLabel").setLabel("term.alert.levelname")
                .setFreemarkerSnippet("${DATA.const_levelLabel}")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_type", "common.type", "select", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true)
                .setIsSort(true));

        attrList.add(new AlertAttrDefineVo()
                .setName("const_typeName")
                .setLabel("common.typename")
                .setFreemarkerSnippet("${DATA.const_typeName}").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_isClose", "page.isclose", "select", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true));
        //if (isExpand == 1) {
        attrList.add(new AlertAttrDefineVo()
                .setName("const_isCloseName")
                .setLabel("term.alert.isclosename")
                .setFreemarkerSnippet("${DATA.const_isCloseName}").setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_status", "common.status", "select", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true)
                .setIsSort(true));

        attrList.add(new AlertAttrDefineVo("const_similarCount", "term.alert.simillaralertcount", "text", new ArrayList<String>() {{
            this.add("equal");
            this.add("notequal");
            this.add("gt");
            this.add("lt");
            this.add("gte");
            this.add("lte");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("type", "number");
        }}).setIsCondition(true));

        attrList.add(new AlertAttrDefineVo()
                .setName("const_statusName")
                .setLabel("common.statusname")
                .setFreemarkerSnippet("${DATA.const_statusName}").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_alertTime", "common.createtime", "datetime", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true)
                .setIsSort(true));

        attrList.add(new AlertAttrDefineVo()
                .setName("const_alertTimeStr")
                .setLabel("term.alert.createtimetext")
                .setFreemarkerSnippet("${DATA.const_alertTimeStr}")
                .setIsTemplate(true)
        );

        attrList.add(new AlertAttrDefineVo("const_updateTime", "term.alert.updatetime", "datetime", new ArrayList<String>() {{
            this.add("range");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("type", "datetimerange");
            this.put("format", "yyyy-MM-dd HH:mm");
        }}).setFreemarkerSnippet("${DATA.const_updateTime}")
                .setIsSearch(true)
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true)
                .setIsSort(true));
        //if (isExpand == 1) {
        attrList.add(new AlertAttrDefineVo()
                .setName("const_updateTimeStr")
                .setLabel("term.alert.updatetimetext")
                .setFreemarkerSnippet("${DATA.const_updateTimeStr}")
                .setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_source", "common.source")
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
                .setIsColumn(true)
                .setIsTemplate(true)
                .setIsCondition(true));

        attrList.add(new AlertAttrDefineVo("const_userList", "common.worker", "userselect", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_userUuidList", "term.alert.workeruuid", "userselect", new ArrayList<String>() {{
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
        attrList.add(new AlertAttrDefineVo().setName("const_userAccountList").setLabel("term.alert.workeraccount")
                .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userAccount}\"<#if user_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_userPhoneList").setLabel("term.alert.workerphone")
                .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userPhone}\"<#if user_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_userEmailList").setLabel("term.alert.workeremail")
                .setFreemarkerSnippet("[<#list DATA.const_userList as user>\"${user.userEmail}\"<#if user_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamEmailList").setLabel("term.alert.workerteamemail")
                .setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamEmail}\"<#if team_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamPhoneList").setLabel("term.alert.workerteamphone")
                .setFreemarkerSnippet("[<#list DATA.const_teamList as team>\"${team.teamPhone}\"<#if team_has_next>,</#if></#list>]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamAndLeaderPhoneList").setLabel("term.alert.workerteamleaderphone")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#if team.teamPhone?? && team.teamPhone?has_content>" +
                        "\"${team.teamPhone}\"" +
                        "<#if team.leaderList?? && (team.leaderList?size > 0)>" +
                        "<#list team.leaderList as leader>" +
                        ",\"${leader.userPhone}\"" +
                        "</#list>" +
                        "</#if>" +
                        "<#if !team?is_last>,</#if>" +
                        "<#else>" +
                        "<#if team.leaderList?? && (team.leaderList?size > 0)>" +
                        "<#list team.leaderList as leader>" +
                        "\"${leader.userPhone}\"<#if !leader?is_last>,</#if>" +
                        "</#list>" +
                        "<#if !team?is_last>,</#if>" +
                        "</#if>" +
                        "</#if>" +
                        "</#list>" +
                        "]")
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserAccountList").setLabel("alert.alert.teammemberaccount")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userAccount}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserPhoneList").setLabel("term.alert.workerteamaccountphone")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userPhone}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserEmailList").setLabel("term.alert.workerteammemberemail")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userEmail}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo().setName("const_teamUserNameList").setLabel("alert.term.workerteammembername")
                .setFreemarkerSnippet("[" +
                        "<#list DATA.const_teamList as team>" +
                        "<#list team.userList?default([]) as user>" +
                        "\"${user.userName}\"<#if !team?is_last || !user?is_last>,</#if>" +
                        "</#list>" +
                        "</#list>" +
                        "]").setIsTemplate(true));
        //}
        attrList.add(new AlertAttrDefineVo("const_teamList", "term.alert.workerteam", "userselect", new ArrayList<String>() {{
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
                .setIsColumn(true)
                .setIsTemplate(true));

        attrList.add(new AlertAttrDefineVo("const_teamUuidList", "term.alert.workerteamuuid", "userselect", new ArrayList<String>() {{
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

        attrList.add(new AlertAttrDefineVo("const_markList", "common.tag", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dynamicUrl", "/api/rest/alert/mark/search");
            this.put("valueName", "name");
            this.put("textName", "name");
        }}).setIsTemplate(true)
                .setIsSearch(true)
                .setIsColumn(false)
                .setFreemarkerSnippet("[<#list DATA.const_markList as mark>\"${mark.name}\"<#if mark_has_next>,</#if></#list>]"));

        attrList.add(new AlertAttrDefineVo("const_markNameList", "common.tag", "select", new ArrayList<String>() {{
            this.add("like");
            this.add("notlike");
            this.add("is-null");
            this.add("is-not-null");
        }}, new JSONObject() {{
            this.put("transfer", true);
            this.put("dynamicUrl", "/api/rest/alert/mark/search");
            this.put("valueName", "name");
            this.put("textName", "name");
        }}).setIsCondition(true));

        attrList.add(new AlertAttrDefineVo()
                .setName("alertCount")
                .setLabel("term.alert.aggregatecount")
                .setFreemarkerSnippet("${DATA.alertCount}")
                .setIsAggregate(true));

        attrList.add(new AlertAttrDefineVo()
                .setName("alertList")
                .setLabel("term.alert.aggregatealertlisthtml")
                .setFreemarkerSnippet("${DATA.alertList}")
                .setIsAggregate(true));

        attrList.add(new AlertAttrDefineVo()
                .setName("alertItemList")
                .setLabel("term.alert.aggregatealertitemlist")
                .setFreemarkerSnippet("${DATA.alertItemList}")
                .setIsAggregate(true));

    }
}
