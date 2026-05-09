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

public class AlertBreakerCollectResultVo {
    private boolean collectingStarted;
    private AlertBreakerStateVo stateVo;
    private JSONObject data;

    public boolean isCollectingStarted() {
        return collectingStarted;
    }

    public void setCollectingStarted(boolean collectingStarted) {
        this.collectingStarted = collectingStarted;
    }

    public AlertBreakerStateVo getStateVo() {
        return stateVo;
    }

    public void setStateVo(AlertBreakerStateVo stateVo) {
        this.stateVo = stateVo;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
