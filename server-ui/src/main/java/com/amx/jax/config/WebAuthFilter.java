package com.amx.jax.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.ui.session.UIConstants;
import com.amx.jax.ui.session.UserSession;

/**
 * The Class WebAuthFilter.
 */
@Component
public class WebAuthFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(UserSession.class);

	/** The session service. */
	@Autowired
	UserSession userSession;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		if (!userSession.isRequestAuthorized()) {
			HttpServletResponse response = ((HttpServletResponse) resp);
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", "/logout");
		} 
		else 
		{
			String referrer = req.getParameter(UIConstants.REFERRER);
			if (referrer != null) {
				userSession.setReferrer(referrer);
			}

			if (!AppContextUtil.getRequestType().isTrack()) {
				AppContextUtil.setActorId(
						new AuditActor(userSession.isValid() ? AuditActor.ActorType.CUSTOMER
							: AuditActor.ActorType.GUEST, userSession.getCivilId()));
			}
			chain.doFilter(req, resp);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// empty
	}
}
