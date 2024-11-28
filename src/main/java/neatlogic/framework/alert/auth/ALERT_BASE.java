package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

public class ALERT_BASE extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "统一告警平台基础权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "使用统一告警平台模块";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 1;
    }

}
