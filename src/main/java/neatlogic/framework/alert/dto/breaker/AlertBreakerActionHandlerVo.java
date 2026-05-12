/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the Sustainable Use License (SUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.alert.dto.breaker;

import java.util.List;

public class AlertBreakerActionHandlerVo {
    private String name;
    private String label;
    private String description;
    private List<String> supportTriggerList;

    public AlertBreakerActionHandlerVo() {
    }

    public AlertBreakerActionHandlerVo(String name, String label, String description, List<String> supportTriggerList) {
        this.name = name;
        this.label = label;
        this.description = description;
        this.supportTriggerList = supportTriggerList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSupportTriggerList() {
        return supportTriggerList;
    }

    public void setSupportTriggerList(List<String> supportTriggerList) {
        this.supportTriggerList = supportTriggerList;
    }
}
