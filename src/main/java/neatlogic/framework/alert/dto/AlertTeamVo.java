/*
 * Copyright (C) 2025  深圳极向量科技有限公司 All Rights Reserved.
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlertTeamVo)) return false;
        AlertTeamVo that = (AlertTeamVo) o;
        return Objects.equals(teamUuid, that.teamUuid);
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
