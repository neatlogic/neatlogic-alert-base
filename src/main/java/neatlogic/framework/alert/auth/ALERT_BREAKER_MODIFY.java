package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_BREAKER_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "告警熔断策略管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "新增、修改和删除告警熔断策略";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 16;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
