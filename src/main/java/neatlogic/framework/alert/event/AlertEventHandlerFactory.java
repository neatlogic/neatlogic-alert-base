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

package neatlogic.framework.alert.event;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@RootComponent
public class AlertEventHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IAlertEventHandler> eventMap = new HashMap<>();
    private static final List<IAlertEventHandler> pluginList = new ArrayList<>();

    public static IAlertEventHandler getHandler(String handlerName) {
        return eventMap.get(handlerName);
    }

    public static List<IAlertEventHandler> getHandlerList() {
        return pluginList;
    }

    public static List<IAlertEventHandler> getHandlerList(String event) {
        return pluginList.stream().filter(d -> d.supportEventTypes().contains(event)).collect(Collectors.toList());
    }

    public static List<IAlertEventHandler> getHandlerList(String event, String parentPlugin) {
        return pluginList.stream().filter(
                d -> (StringUtils.isBlank(event) || d.supportEventTypes().contains(event)) &&
                        (StringUtils.isBlank(parentPlugin) || d.supportParentHandler().contains(parentPlugin.toLowerCase()))
        ).collect(Collectors.toList());
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IAlertEventHandler> myMap = context.getBeansOfType(IAlertEventHandler.class);
        for (Map.Entry<String, IAlertEventHandler> entry : myMap.entrySet()) {
            IAlertEventHandler handler = entry.getValue();
            eventMap.put(handler.getName(), handler);
            pluginList.add(handler);
        }
        pluginList.sort(Comparator.comparing(IAlertEventHandler::getName));
    }

    @Override
    protected void myInit() {

    }
}
