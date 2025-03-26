package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_NOTIFY_TEMPLATE_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "通知模板管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "新增、修改和修改通知模板管理";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 7;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }
}
