package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_ALLALERTCONFIG_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "所有告警配置管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "修改所有告警页面的显示属性、条件和排序配置";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 6;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
