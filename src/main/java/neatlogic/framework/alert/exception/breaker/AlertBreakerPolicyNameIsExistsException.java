package neatlogic.framework.alert.exception.breaker;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class AlertBreakerPolicyNameIsExistsException extends ApiRuntimeException {
    public AlertBreakerPolicyNameIsExistsException(String name) {
        super("告警熔断策略：“{0}”已存在", name);
    }
}

