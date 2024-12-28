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

import neatlogic.framework.alert.dto.AlertEventHandlerDataVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertEventMapper {
    List<AlertEventHandlerVo> getAlertEventHandlerByHandler(String handler);

    Integer getAlertEventHandlerMaxSort(AlertEventHandlerVo alertEventHandlerVo);

    AlertEventHandlerVo getAlertEventHandlerById(Long id);

    List<AlertEventHandlerVo> listEventHandler(AlertEventHandlerVo alertEventHandlerVo);

    List<AlertEventHandlerVo> getAlertEventHandlerByEvent(String event);

    List<AlertEventHandlerVo> getAlertEventHandlerByParentId(Long parentId);

    AlertEventHandlerVo getAlertEventHandlerByUuid(String uuid);

    List<AlertEventHandlerDataVo> listAlertEventHandlerData();

    AlertEventHandlerDataVo getAlertEventHandlerData(@Param("alertId") Long alertId, @Param("alertEventHandlerId") Long alertEventHandlerId);

    void saveAlertEventHandler(AlertEventHandlerVo alertEventHandlerVo);

    void insertAlertEventHandlerData(AlertEventHandlerDataVo alertEventHandlerDataVo);

    void updateAlertEventHandlerSort(@Param("id") Long id, @Param("sort") int sort);

    void deleteAlertEventHandlerById(Long id);

    void deleteAlertEventHandlerData(@Param("alertId") Long alertId, @Param("alertEventHandlerId") Long alertEventHandlerId);

}
