package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_ATTR_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "告警扩展属性管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "新增、修改和修改告警扩展属性";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 3;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
