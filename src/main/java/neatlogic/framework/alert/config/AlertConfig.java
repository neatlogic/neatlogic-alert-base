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

package neatlogic.framework.alert.config;

import neatlogic.framework.common.config.IConfigListener;

import java.util.Properties;

public class AlertConfig implements IConfigListener {

    private static int ORIGINAL_ALERT_THREAD_COUNT;
    private static int ALERT_EVENT_THREAD_COUNT;
    private static int ALERT_SUBSCRIBE_THREAD_COUNT;

    public static int ORIGINAL_ALERT_THREAD_COUNT() {
        return ORIGINAL_ALERT_THREAD_COUNT;
    }

    public static int ALERT_EVENT_THREAD_COUNT() {
        return ALERT_EVENT_THREAD_COUNT;
    }

    public static int ALERT_SUBSCRIBE_THREAD_COUNT() {
        return ALERT_SUBSCRIBE_THREAD_COUNT;
    }


    @Override
    public void loadConfig(Properties prop) {
        ORIGINAL_ALERT_THREAD_COUNT = Integer.parseInt(prop.getProperty("alert.original.thread.count", "5"));
        ALERT_EVENT_THREAD_COUNT = Integer.parseInt(prop.getProperty("alert.events.thread.count", "5"));
        ALERT_SUBSCRIBE_THREAD_COUNT = Integer.parseInt(prop.getProperty("alert.subscribe.thread.count", "3"));
    }
}
