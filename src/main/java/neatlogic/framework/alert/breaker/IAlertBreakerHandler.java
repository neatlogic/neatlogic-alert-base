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
import neatlogic.framework.alert.dto.breaker.AlertBreakerCollectResultVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerResultVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerStateVo;

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
     * 收集熔断命中后的本次事件数据。
     * <p>
     * 只有当前策略在 {@link #execute(AlertBreakerPolicyVo, AlertVo, AlertEventHandlerVo, Long)} 中返回熔断结果后，
     * 才会调用该方法。该方法用于记录被熔断事件的上下文，例如告警id、通知目标、聚合批次信息等。
     * 实现类应自行处理并发合并和幂等问题，避免影响主流程。
     * </p>
     *
     * @param policyVo            熔断策略配置。
     * @param alertVo             当前告警信息。
     * @param eventHandlerVo      当前事件插件配置。
     * @param eventHandlerAuditId 当前事件插件审计记录id。
     * @param resultVo            熔断检查结果，包含本次命中的状态记录id。
     */
    void collect(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId, AlertBreakerResultVo resultVo);

    /**
     * 首次进入熔断收集态后的生命周期回调。
     * <p>
     * 只有当前策略从非收集态首次推进到COLLECTING，并且 {@link #collect(AlertBreakerPolicyVo, AlertVo, AlertEventHandlerVo, Long, AlertBreakerResultVo)}
     * 内部事务已经提交成功后才会调用。该方法用于执行事务外动作，例如创建延迟处理作业。
     * 实现类应保证该方法幂等，并避免异常影响主流程。
     * </p>
     *
     * @param policyVo            熔断策略配置。
     * @param alertVo             当前告警信息。
     * @param eventHandlerVo      当前事件插件配置。
     * @param eventHandlerAuditId 当前事件插件审计记录id。
     * @param resultVo            熔断检查结果。
     * @param collectResultVo     收集结果，包含首次进入COLLECTING的状态信息。
     */
    void collectingStarted(AlertBreakerPolicyVo policyVo, AlertVo alertVo, AlertEventHandlerVo eventHandlerVo, Long eventHandlerAuditId, AlertBreakerResultVo resultVo, AlertBreakerCollectResultVo collectResultVo);

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

    /**
     * 处理已到期的熔断状态。
     * <p>
     * 调度任务会扫描已经到期的熔断状态，并根据策略插件调用该方法。该方法用于执行补偿动作，
     * 例如发送聚合通知、清理插件状态或恢复熔断状态。实现类应保证重复调用时具备幂等性。
     * </p>
     *
     * @param policyVo 熔断策略配置。
     * @param stateVo  已到期的熔断状态。
     */
    void flush(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo);
}
