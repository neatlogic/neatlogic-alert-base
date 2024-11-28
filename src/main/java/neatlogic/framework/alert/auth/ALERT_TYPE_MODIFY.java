package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_TYPE_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "统一告警平台修改告警类型权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "新增、修改和修改告警类型";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 2;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
