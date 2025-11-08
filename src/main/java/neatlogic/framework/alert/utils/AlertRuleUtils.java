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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertRuleVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AlertRuleUtils {
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
