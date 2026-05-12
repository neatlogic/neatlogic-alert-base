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

import java.util.Date;

public class AlertBreakerResultVo {
    private Long policyId;
    private Long stateId;
    private Date openUntil;
    private boolean isBreaked;
    private boolean isOpenStarted;
    private String status;

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Date getOpenUntil() {
        return openUntil;
    }

    public void setOpenUntil(Date openUntil) {
        this.openUntil = openUntil;
    }

    public boolean isBreaked() {
        return isBreaked;
    }

    public void setBreaked(boolean breaked) {
        isBreaked = breaked;
    }

    public boolean isOpenStarted() {
        return isOpenStarted;
    }

    public void setOpenStarted(boolean openStarted) {
        isOpenStarted = openStarted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
