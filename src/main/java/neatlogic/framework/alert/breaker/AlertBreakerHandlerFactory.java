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

package neatlogic.framework.alert.breaker;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.*;

@RootComponent
public class AlertBreakerHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IAlertBreakerHandler> handlerMap = new HashMap<>();
    private static final List<IAlertBreakerHandler> handlerList = new ArrayList<>();

    public static IAlertBreakerHandler getHandler(String name) {
        return handlerMap.get(name);
    }

    public static List<IAlertBreakerHandler> getHandlerList() {
        return handlerList;
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IAlertBreakerHandler> myMap = context.getBeansOfType(IAlertBreakerHandler.class);
        for (Map.Entry<String, IAlertBreakerHandler> entry : myMap.entrySet()) {
            IAlertBreakerHandler handler = entry.getValue();
            handlerMap.put(handler.getName(), handler);
            handlerList.add(handler);
        }
        handlerList.sort(Comparator.comparing(IAlertBreakerHandler::getName));
    }

    @Override
    protected void myInit() {

    }
}
