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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

import java.io.Serializable;
import java.util.Objects;

public class AlertUserVo implements Serializable {
    @EntityField(name = "告警id", type = ApiParamType.LONG)
    private Long alertId;
    @EntityField(name = "用户uuid", type = ApiParamType.STRING)
    private String userId;
    @EntityField(name = "用户账号", type = ApiParamType.STRING)
    private String userAccount;
    @EntityField(name = "用户名", type = ApiParamType.STRING)
    private String userName;
    @EntityField(name = "电话", type = ApiParamType.STRING)
    private String userPhone;
    @EntityField(name = "Email", type = ApiParamType.STRING)
    private String userEmail;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlertUserVo)) return false;
        AlertUserVo that = (AlertUserVo) o;
        return Objects.equals(userId, that.userId);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
