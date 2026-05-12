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

package neatlogic.framework.alert.breaker.action;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.*;

@RootComponent
public class AlertBreakerActionHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IAlertBreakerActionHandler> actionMap = new HashMap<>();
    private static final List<IAlertBreakerActionHandler> actionList = new ArrayList<>();

    public static IAlertBreakerActionHandler getHandler(String handlerName) {
        return actionMap.get(handlerName);
    }

    public static List<IAlertBreakerActionHandler> getHandlerList() {
        return actionList;
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IAlertBreakerActionHandler> myMap = context.getBeansOfType(IAlertBreakerActionHandler.class);
        for (Map.Entry<String, IAlertBreakerActionHandler> entry : myMap.entrySet()) {
            IAlertBreakerActionHandler handler = entry.getValue();
            actionMap.put(handler.getName(), handler);
            actionList.add(handler);
        }
        actionList.sort(Comparator.comparing(IAlertBreakerActionHandler::getName));
    }

    @Override
    protected void myInit() {

    }
}
