package neatlogic.framework.alert.exception.breaker;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class AlertBreakerPolicyIsInUsedException extends ApiRuntimeException {
    public AlertBreakerPolicyIsInUsedException() {
        super("熔断策略已被事件插件引用，不能删除");
    }
}

