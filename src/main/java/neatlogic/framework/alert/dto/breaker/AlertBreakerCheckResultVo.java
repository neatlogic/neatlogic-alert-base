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

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class AlertBreakerCheckResultVo {
    private boolean breaked;
    private Date windowStart;
    private Date windowEnd;
    private Integer triggerCount;
    private Date openUntil;
    private JSONObject data;
    private boolean clearCollectItemOnOpen;

    public boolean isBreaked() {
        return breaked;
    }

    public void setBreaked(boolean breaked) {
        this.breaked = breaked;
    }

    public Date getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Date windowStart) {
        this.windowStart = windowStart;
    }

    public Date getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Date windowEnd) {
        this.windowEnd = windowEnd;
    }

    public Integer getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Date getOpenUntil() {
        return openUntil;
    }

    public void setOpenUntil(Date openUntil) {
        this.openUntil = openUntil;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public boolean isClearCollectItemOnOpen() {
        return clearCollectItemOnOpen;
    }

    public void setClearCollectItemOnOpen(boolean clearCollectItemOnOpen) {
        this.clearCollectItemOnOpen = clearCollectItemOnOpen;
    }
}
