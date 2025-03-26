package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

public class ALERT_STATUS_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "告警状态管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "可以对告警状态进行添加、修改和删除";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 8;
    }

}
