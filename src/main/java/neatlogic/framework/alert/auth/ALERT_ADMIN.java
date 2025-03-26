package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

public class ALERT_ADMIN extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "告警管理员权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "可以对所有告警进行状态修改、关闭、删除等操作";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 9;
    }

}
