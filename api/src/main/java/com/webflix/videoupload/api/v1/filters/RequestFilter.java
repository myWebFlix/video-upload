package com.webflix.videoupload.api.v1.filters;

import com.webflix.videoupload.services.beans.LogTracerBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
@ApplicationScoped
public class RequestFilter implements ContainerRequestFilter {

	@Inject
	private LogTracerBean logTracerBean;

	@Override
	public void filter(ContainerRequestContext reqCtx) {
		String uuid;
		if (reqCtx.getHeaders().containsKey("Log-Tracer"))
			uuid = reqCtx.getHeaders().get("Log-Tracer").get(0);
		else
			uuid = UUID.randomUUID().toString();
		logTracerBean.setUuid(uuid);
	}

}
