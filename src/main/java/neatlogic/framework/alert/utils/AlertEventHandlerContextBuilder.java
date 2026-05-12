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

package neatlogic.framework.alert.utils;

import neatlogic.framework.alert.dao.mapper.AlertEventMapper;
import neatlogic.framework.alert.dto.AlertTeamVo;
import neatlogic.framework.alert.dto.AlertVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AlertEventHandlerContextBuilder {
    @Resource
    private AlertEventMapper alertEventMapper;

    public AlertVo build(AlertVo alertVo) {
        if (alertVo == null || alertVo.getId() == null) {
            return alertVo;
        }
        AlertVo newAlertVo = alertEventMapper.getAlertById(alertVo.getId());
        if (newAlertVo != null) {
            newAlertVo.setUserList(alertEventMapper.getAlertUserByAlertId(alertVo.getId()));
            newAlertVo.setTeamList(alertEventMapper.getAlertTeamByAlertId(alertVo.getId()));
            if (CollectionUtils.isNotEmpty(newAlertVo.getTeamList())) {
                for (AlertTeamVo team : newAlertVo.getTeamList()) {
                    team.setLeaderList(alertEventMapper.getAlertLeaderByTeamId(team.getTeamUuid()));
                    team.setUserList(alertEventMapper.getAlertUserByTeamId(team.getTeamUuid()));
                }
            }
            newAlertVo.setPrevEventResult(alertVo.getPrevEventResult());
            return newAlertVo;
        }
        return alertVo;
    }
}
