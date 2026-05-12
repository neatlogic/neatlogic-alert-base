package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

public class ALERT_BATCH_DELETE extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "批量删除告警权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "可以对命中高级搜索规则的告警进行批量删除";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 19;
    }

}
