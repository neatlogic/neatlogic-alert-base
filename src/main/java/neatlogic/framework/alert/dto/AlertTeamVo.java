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

package neatlogic.framework.alert.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AlertTeamVo implements Serializable {
    private Long alertId;
    private String teamUuid;
    private String teamName;
    private String teamEmail;
    private String teamPhone;
    private List<AlertUserVo> userList;
    private List<AlertUserVo> leaderList;//领导用户列表

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlertTeamVo)) return false;
        AlertTeamVo that = (AlertTeamVo) o;
        return Objects.equals(teamUuid, that.teamUuid);
    }

    public List<AlertUserVo> getLeaderList() {
        return leaderList;
    }

    public void setLeaderList(List<AlertUserVo> leaderList) {
        this.leaderList = leaderList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(teamUuid);
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getTeamUuid() {
        return teamUuid;
    }

    public void setTeamUuid(String teamUuid) {
        this.teamUuid = teamUuid;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamEmail() {
        return teamEmail;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }

    public String getTeamPhone() {
        return teamPhone;
    }

    public void setTeamPhone(String teamPhone) {
        this.teamPhone = teamPhone;
    }

    public List<AlertUserVo> getUserList() {
        return userList;
    }

    public void setUserList(List<AlertUserVo> userList) {
        this.userList = userList;
    }
}
