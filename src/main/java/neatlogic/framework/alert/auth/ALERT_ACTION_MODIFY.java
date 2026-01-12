package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_ACTION_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "告警自定义操作管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "新增、修改和修改告警自定义操作";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 15;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
