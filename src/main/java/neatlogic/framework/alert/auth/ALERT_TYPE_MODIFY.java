package neatlogic.framework.alert.auth;

import neatlogic.framework.auth.core.AuthBase;
import java.util.Collections;
import java.util.List;

/** 权限名称与说明使用国际化键，权限标识及校验规则保持不变。 */
public class ALERT_TYPE_MODIFY extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "auth.alert_type_modify.name";
    }

    @Override
    public String getAuthIntroduction() {
        return "auth.alert_type_modify.description";
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
