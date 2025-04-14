package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_EVENT_PLUGIN_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "告警事件插件管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "修改告警事件插件配置";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 11;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
