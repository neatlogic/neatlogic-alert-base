/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.alert.dao.mapper;

import neatlogic.framework.alert.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertEventMapper {
    AlertVo getAlertById(Long id);

    List<AlertTeamVo> getAlertTeamByAlertId(Long alertId);

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
