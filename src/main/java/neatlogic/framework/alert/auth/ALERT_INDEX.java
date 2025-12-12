package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class ALERT_INDEX extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "重建告警索引权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "拥有此权限可以单独重建单个告警的索引";
    }

    @Override
    public String getAuthGroup() {
        return "alert";
    }

    @Override
    public Integer getSort() {
        return 15;
    }

    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Collections.singletonList(ALERT_BASE.class);
    }

}
