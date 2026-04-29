package neatlogic.framework.alert.exception.breaker;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class AlertBreakerHandlerNotFoundException extends ApiRuntimeException {
    public AlertBreakerHandlerNotFoundException(String name) {
        super("告警熔断策略插件：“{0}”不存在", name);
    }
}

