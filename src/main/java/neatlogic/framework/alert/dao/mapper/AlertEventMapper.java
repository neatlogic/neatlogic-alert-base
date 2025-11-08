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

import neatlogic.framework.alert.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertEventMapper {
    AlertVo getAlertById(Long id);

    List<AlertTeamVo> getAlertTeamByAlertId(Long alertId);

    List<AlertUserVo> getAlertLeaderByTeamId(String teamUuid);

    List<AlertUserVo> getAlertUserByTeamId(String teamUuid);

    List<AlertUserVo> getAlertUserByAlertId(Long alertId);

    Integer getAlertEventExecuteCount(@Param("handler") String handler, @Param("miniSecond") Long miniSecond);

    //List<AlertEventHandlerVo> getAlertEventHandlerByHandler(String handler);
    AlertEventHandlerAuditVo getLastAlertEventHandlerAudit(AlertEventHandlerAuditVo alertEventHandlerAuditVo);

    Integer getAlertEventHandlerMaxSort(AlertEventHandlerVo alertEventHandlerVo);

    List<AlertEventPluginVo> getAllAlertEventPluginConfig();

    AlertEventPluginVo getAlertEventPluginConfigByName(String name);

    AlertEventHandlerVo getAlertEventHandlerById(Long id);

    List<AlertEventHandlerVo> listEventHandler(AlertEventHandlerVo alertEventHandlerVo);

    List<AlertEventHandlerVo> getAlertEventHandlerByEvent(@Param("event") String event, @Param("alertType") Long alertType);

    List<AlertEventHandlerVo> getAlertEventHandlerByParentId(Long parentId);

    AlertEventHandlerVo getAlertEventHandlerByUuid(String uuid);

    void saveAlertEventPluginConfig(AlertEventPluginVo alertEventPluginVo);

    void saveAlertEventHandler(AlertEventHandlerVo alertEventHandlerVo);

    void insertAlertEventAudit(AlertEventHandlerAuditVo alertEventHandlerAuditVo);


    void updateAlertEventHandlerAudit(AlertEventHandlerAuditVo alertEventHandlerAuditVo);

    void updateAlertEventAuditResult(AlertEventHandlerAuditVo alertEventHandlerAuditVo);

    void updateAlertEventHandlerSort(@Param("id") Long id, @Param("sort") int sort);

    void deleteAlertEventHandlerById(Long id);

    void deleteAlertEventPluginConfig(String name);

}
