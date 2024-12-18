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

package neatlogic.framework.alert.event;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.*;

@RootComponent
public class AlertEventHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IAlertEventHandler> eventMap = new HashMap<>();
    private static final List<IAlertEventHandler> pluginList = new ArrayList<>();

    public static IAlertEventHandler getHandler(String eventName) {
        return eventMap.get(eventName);
    }

    public static List<IAlertEventHandler> getHandlerList() {
        return pluginList;
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
