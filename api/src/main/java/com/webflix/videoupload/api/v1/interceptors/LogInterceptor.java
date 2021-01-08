package com.webflix.videoupload.api.v1.interceptors;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.cdi.Log;
import com.webflix.videoupload.services.beans.LogTracerBean;
import org.apache.logging.log4j.CloseableThreadContext;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;

@Log
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogInterceptor {

	@Inject
	LogTracerBean logTracerBean;

	@AroundInvoke
	public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

		// ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

		HashMap settings = new HashMap();

		settings.put("environmentType", EeConfig.getInstance().getEnv().getName());
		settings.put("applicationName", EeConfig.getInstance().getName());
		settings.put("applicationVersion", EeConfig.getInstance().getVersion());
		settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

		settings.put("logTracerUuid", logTracerBean.getUuid());

		try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(settings)) {
			Object result = context.proceed();
			return result;
		}
	}

}
