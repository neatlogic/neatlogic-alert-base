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

package neatlogic.framework.alert.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertRuleVo;
import neatlogic.framework.alert.dto.AlertVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlertRuleUtils {
    /** 按告警保存约定组合属性，匹配历史告警时可要求所有属性均有有效值。 */
    public static String buildUniqueKey(AlertVo alertVo, JSONArray uniqueAttrList, List<AlertRuleVo> ruleList, boolean requireComplete) {
        List<String> attrList = new ArrayList<>();
        for (int i = 0; i < uniqueAttrList.size(); i++) {
            attrList.add(uniqueAttrList.getJSONObject(i).getString("name"));
        }
        attrList.sort(String::compareTo);
        JSONObject alertObj = JSON.parseObject(JSON.toJSONString(alertVo));
        JSONObject attrObj = alertObj.getJSONObject("attrObj");
        String key = "";
        for (String attr : attrList) {
            String value;
            if (attr.startsWith("const_")) {
                value = alertObj.getString(attr.substring("const_".length()));
            } else if (attr.startsWith("attr_")) {
                value = null;
                if (attrObj != null) {
                    value = attrObj.getString(attr.substring("attr_".length()));
                }
                if (value == null && !requireComplete) {
                    continue;
                }
            } else {
                if (requireComplete) {
                    return null;
                }
                continue;
            }
            List<AlertRuleVo> attrRuleList = ruleList.stream().filter(rule -> attr.equals(rule.getAttrName())).collect(Collectors.toList());
            value = doRule(value, attrRuleList);
            if (requireComplete && StringUtils.isBlank(value)) {
                return null;
            }
            if (StringUtils.isNotBlank(key)) {
                key += "#";
            }
            key += value;
        }
        return key;
    }

    public static String doRule(String content, JSONArray ruleList) {
        if (StringUtils.isNotBlank(content) && CollectionUtils.isNotEmpty(ruleList)) {
            for (int i = 0; i < ruleList.size(); i++) {
                JSONObject rule = ruleList.getJSONObject(i);
                String pattern = rule.getString("pattern");
                String replacement = rule.getString("replacement");
                content = content.replaceAll(pattern, replacement);
            }
        }
        return content;
    }

    public static String doRule(String content, List<AlertRuleVo> alertRuleList) {
        if (StringUtils.isNotBlank(content) && CollectionUtils.isNotEmpty(alertRuleList)) {
            for (AlertRuleVo alertRuleVo : alertRuleList) {
                JSONArray ruleList = alertRuleVo.getConfig().getJSONArray("ruleList");
                if (CollectionUtils.isNotEmpty(ruleList)) {
                    for (int i = 0; i < ruleList.size(); i++) {
                        JSONObject rule = ruleList.getJSONObject(i);
                        String pattern = rule.getString("pattern");
                        String replacement = rule.getString("replacement");
                        content = content.replaceAll(pattern, replacement);
                    }
                }
            }
        }
        return content;
    }
}
