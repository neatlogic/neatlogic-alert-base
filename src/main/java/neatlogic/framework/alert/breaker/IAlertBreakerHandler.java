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

import neatlogic.framework.alert.dto.AlertEventHandlerAuditVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerResultVo;

public interface IAlertBreakerHandler {
    /**
     * 获取熔断策略插件唯一标识。
     *
     * @return 插件唯一标识，需要与前端配置组件名称保持一致。
     */
    String getName();

    /**
     * 获取熔断策略插件显示名称。
     *
     * @return 插件显示名称，用于页面展示。
     */
    String getLabel();

    /**
     * 获取熔断策略插件说明。
     *
     * @return 插件说明，用于页面展示插件用途和行为。
     */
    String getDescription();

    /**
     * 执行熔断前置检查。
     * <p>
     * 事件插件执行前会按策略顺序调用该方法；只要任一策略返回熔断结果，后续策略不再执行check，
     * 当前事件插件也会被跳过。
     * </p>
     *
     * @param policyVo            熔断策略配置。
     * @param alertVo             当前告警信息。
     * @param eventHandlerVo      当前事件插件配置。
     * @param eventHandlerAuditId 当前事件插件审计记录id。
     * @return 熔断检查结果。
     */
    AlertBreakerResultVo execute(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId);

    /**
     * 执行事件插件后的熔断后置处理。
     * <p>
     * 事件插件流程结束并完成审计状态更新后，会对当前事件插件关联的所有启用熔断策略调用该方法。
     * 该方法只用于采集执行结果或更新插件私有状态，不参与阻断主流程；实现类应避免抛出影响业务流程的异常。
     * </p>
     *
     * @param policyVo             熔断策略配置。
     * @param alertVo              当前告警信息。
     * @param eventHandlerVo       当前事件插件配置。
     * @param eventHandlerAuditVo  当前事件插件审计记录，状态已更新为最终状态。
     */
    void after(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, AlertEventHandlerAuditVo eventHandlerAuditVo);
}
