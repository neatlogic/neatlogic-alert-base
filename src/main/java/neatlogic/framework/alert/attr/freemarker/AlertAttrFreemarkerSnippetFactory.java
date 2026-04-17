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

package neatlogic.framework.alert.attr.freemarker;

import neatlogic.framework.alert.dto.AlertAttrTypeVo;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AlertAttrFreemarkerSnippetFactory {
    private static final Map<String, IAlertAttrFreemarkerSnippetHandler> handlerMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections("neatlogic");
        Set<Class<? extends IAlertAttrFreemarkerSnippetHandler>> modules = reflections.getSubTypesOf(IAlertAttrFreemarkerSnippetHandler.class);
        for (Class<? extends IAlertAttrFreemarkerSnippetHandler> c : modules) {
            if (c.isInterface()) {
                continue;
            }
            try {
                IAlertAttrFreemarkerSnippetHandler handler = c.newInstance();
                if (StringUtils.isNotBlank(handler.getName())) {
                    handlerMap.put(handler.getName(), handler);
                }
            } catch (Throwable ignored) {
            }
        }
    }

    public static String getFreemarkerSnippet(AlertAttrTypeVo alertAttrTypeVo) {
        if (alertAttrTypeVo == null) {
            return null;
        }
        IAlertAttrFreemarkerSnippetHandler handler = handlerMap.get(alertAttrTypeVo.getType());
        if (handler == null) {
            return null;
        }
        return handler.getFreemarkerSnippet(alertAttrTypeVo);
    }
}
