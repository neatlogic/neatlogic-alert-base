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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.util.javascript.JavascriptUtil;

import javax.script.ScriptException;

public class AlertScriptUtils {
    private static final String PREPARE_INPUT_ALERT =
            "(function() {"
                    + "function freeze(value) {"
                    + "if (value && typeof value === 'object') {"
                    + "Object.keys(value).forEach(function(key) { freeze(value[key]); });"
                    + "Object.freeze(value);"
                    + "}"
                    + "}"
                    + "freeze(O.inputAlert);"
                    + "Object.defineProperty(O, 'inputAlert', { writable: false, configurable: false });"
                    + "delete N.inputAlert;"
                    + "})();";

    /** 执行告警脚本，输入快照仅在 O 上提供，试跑和事件执行使用同一约定。 */
    public static String transform(JSONObject alertData, String script) throws ScriptException {
        if (alertData.getJSONObject("inputAlert") != null) {
            return JavascriptUtil.transform(alertData.toJSONString(), PREPARE_INPUT_ALERT + "\n" + script);
        }
        return JavascriptUtil.transform(alertData, script);
    }
}
