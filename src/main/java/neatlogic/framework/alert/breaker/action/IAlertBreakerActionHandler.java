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

import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerActionVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerStateVo;
import neatlogic.framework.alert.enums.AlertBreakerActionTrigger;

import java.util.List;
import java.util.Set;

public interface IAlertBreakerActionHandler {
    String getName();

    String getLabel();

    String getDescription();

    Set<AlertBreakerActionTrigger> supportTrigger();

    /**
     * 熔断刚打开时触发。
     *
     * @param actionVo 动作配置。
     * @param policyVo 熔断策略。
     * @param stateVo 熔断状态。
     * @param alertVo 本次触发熔断的告警。
     */
    void triggerOpen(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertVo alertVo);

    /**
     * 熔断聚合处理时触发。
     *
     * @param actionVo 动作配置。
     * @param policyVo 熔断策略。
     * @param stateVo 熔断状态。
     * @param alertList 熔断期间收集到的告警列表。
     */
    void triggerAggregate(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, List<AlertVo> alertList);

    /**
     * 熔断恢复时触发。
     *
     * @param actionVo 动作配置。
     * @param policyVo 熔断策略。
     * @param stateVo 熔断状态。
     * @param alertList 如果恢复来自聚合处理，则为聚合告警列表；普通恢复可能为空。
     */
    void triggerRecover(AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, List<AlertVo> alertList);
}
