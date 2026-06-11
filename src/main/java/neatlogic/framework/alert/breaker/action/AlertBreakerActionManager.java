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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerActionAuditVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerActionVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerPolicyVo;
import neatlogic.framework.alert.dto.breaker.AlertBreakerStateVo;
import neatlogic.framework.alert.enums.AlertBreakerActionTrigger;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlertBreakerActionManager {
    private static final Logger logger = LoggerFactory.getLogger(AlertBreakerActionManager.class);

    private AlertBreakerActionManager() {
    }

    public static List<AlertBreakerActionAuditVo> execute(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertBreakerActionTrigger trigger, List<AlertVo> alertList) {
        return execute(policyVo, stateVo, trigger, alertList, null);
    }

    public static List<AlertBreakerActionAuditVo> execute(AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertBreakerActionTrigger trigger, List<AlertVo> alertList, Long breakerAuditId) {
        List<AlertBreakerActionAuditVo> auditList = new ArrayList<>();
        List<AlertBreakerActionVo> actionList;
        try {
            actionList = getActionList(policyVo, trigger);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return auditList;
        }
        if (CollectionUtils.isEmpty(actionList)) {
            return auditList;
        }
        for (AlertBreakerActionVo actionVo : actionList) {
            try {
                if (actionVo == null || Objects.equals(actionVo.getIsActive(), 0)) {
                    continue;
                }
                IAlertBreakerActionHandler handler = AlertBreakerActionHandlerFactory.getHandler(actionVo.getHandler());
                if (handler == null) {
                    logger.warn("Alert breaker action handler not found: {}", actionVo.getHandler());
                    continue;
                }
                if (handler.supportTrigger() != null && !handler.supportTrigger().contains(trigger)) {
                    logger.warn("Alert breaker action handler {} does not support trigger {}", actionVo.getHandler(), trigger);
                    continue;
                }
                AlertBreakerActionAuditVo auditVo = execute(handler, actionVo, policyVo, stateVo, trigger, alertList, breakerAuditId);
                if (auditVo != null) {
                    auditList.add(auditVo);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return auditList;
    }

    private static AlertBreakerActionAuditVo execute(IAlertBreakerActionHandler handler, AlertBreakerActionVo actionVo, AlertBreakerPolicyVo policyVo, AlertBreakerStateVo stateVo, AlertBreakerActionTrigger trigger, List<AlertVo> alertList, Long breakerAuditId) {
        if (trigger == AlertBreakerActionTrigger.OPEN) {
            return handler.triggerOpen(actionVo, policyVo, stateVo, CollectionUtils.isEmpty(alertList) ? null : alertList.get(0), breakerAuditId);
        } else if (trigger == AlertBreakerActionTrigger.AGGREGATE) {
            return handler.triggerAggregate(actionVo, policyVo, stateVo, alertList, breakerAuditId);
        } else if (trigger == AlertBreakerActionTrigger.RECOVER) {
            return handler.triggerRecover(actionVo, policyVo, stateVo, alertList, breakerAuditId);
        }
        return null;
    }

    private static List<AlertBreakerActionVo> getActionList(AlertBreakerPolicyVo policyVo, AlertBreakerActionTrigger trigger) {
        List<AlertBreakerActionVo> actionList = new ArrayList<>();
        JSONObject config = policyVo == null ? null : policyVo.getConfig();
        if (config == null || trigger == null) {
            return actionList;
        }
        JSONArray actionArray = config.getJSONArray(getConfigKey(trigger));
        if (CollectionUtils.isEmpty(actionArray)) {
            return actionList;
        }
        for (int i = 0; i < actionArray.size(); i++) {
            actionList.add(actionArray.getObject(i, AlertBreakerActionVo.class));
        }
        return actionList;
    }

    private static String getConfigKey(AlertBreakerActionTrigger trigger) {
        if (trigger == AlertBreakerActionTrigger.OPEN) {
            return "openActionList";
        } else if (trigger == AlertBreakerActionTrigger.AGGREGATE) {
            return "aggregateActionList";
        } else if (trigger == AlertBreakerActionTrigger.RECOVER) {
            return "recoverActionList";
        }
        return "";
    }
}
