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

package neatlogic.framework.alert.dao.mapper;

import neatlogic.framework.alert.dto.breaker.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertBreakerMapper {
    AlertBreakerPolicyVo getAlertBreakerPolicyById(Long id);

    List<AlertBreakerPolicyVo> searchAlertBreakerPolicy(AlertBreakerPolicyVo vo);

    int searchAlertBreakerPolicyCount(AlertBreakerPolicyVo vo);

    int checkAlertBreakerPolicyNameIsExists(AlertBreakerPolicyVo vo);

    int checkAlertBreakerPolicyIsInUsed(Long id);

    void insertAlertBreakerPolicy(AlertBreakerPolicyVo vo);

    void updateAlertBreakerPolicy(AlertBreakerPolicyVo vo);

    void deleteAlertBreakerPolicy(Long id);

    List<AlertEventHandlerBreakerPolicyVo> getBreakerPolicyListByEventHandlerId(Long eventHandlerId);

    List<AlertEventHandlerBreakerPolicyVo> getBreakerPolicyListByEventHandlerIdList(List<Long> eventHandlerIdList);

    void insertEventHandlerBreakerPolicy(AlertEventHandlerBreakerPolicyVo vo);

    void deleteEventHandlerBreakerPolicyByEventHandlerId(Long eventHandlerId);

    void insertAlertBreakerStateIfNotExists(AlertBreakerStateVo vo);

    AlertBreakerStateVo getAlertBreakerStateForUpdate(@Param("policyId") Long policyId, @Param("uniqueKey") String uniqueKey);

    AlertBreakerStateVo getAlertBreakerStateByIdForUpdate(Long id);

    AlertBreakerStateVo getAlertBreakerStateById(Long id);

    void updateAlertBreakerState(AlertBreakerStateVo vo);

    int updateCollectingAlertBreakerStateToFlushing(Long id);

    int insertAlertBreakerCollectItem(AlertBreakerCollectItemVo vo);

    int checkAlertBreakerCollectItemIsExists(@Param("stateId") Long stateId, @Param("alertId") Long alertId);

    int getAlertBreakerCollectItemCountByStateId(Long stateId);

    List<Long> getAlertBreakerCollectAlertIdListByStateId(Long stateId);

    List<Long> getAlertBreakerCollectAlertIdListByStateIdAndBaselineAlertId(@Param("stateId") Long stateId, @Param("baselineAlertId") Long baselineAlertId);

    void deleteAlertBreakerCollectItemByStateId(Long stateId);

    List<AlertBreakerStateVo> getExpiredOpenAlertBreakerStateList(@Param("handler") String handler, @Param("limit") Integer limit);

    List<AlertBreakerStateVo> getCollectingAlertBreakerStateList(@Param("handler") String handler, @Param("lastId") Long lastId, @Param("limit") Integer limit);

    int searchAlertBreakerStateCount(AlertBreakerStateVo vo);

    List<AlertBreakerStateVo> searchAlertBreakerState(AlertBreakerStateVo vo);

    void insertAlertBreakerAudit(AlertBreakerAuditVo vo);

    int searchAlertBreakerAuditCount(AlertBreakerAuditVo vo);

    List<AlertBreakerAuditVo> searchAlertBreakerAudit(AlertBreakerAuditVo vo);

    List<AlertBreakerAuditVo> getAlertBreakerAuditListByEventHandlerAuditIdList(List<Long> eventHandlerAuditIdList);
}
