package com.amx.jax.config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.session.UserSession;

/**
 * The Class WebJaxAdvice.
 */
@ControllerAdvice
public class WebJaxAdvice {

	/** The log. */
	private Logger LOG = LoggerFactory.getLogger(WebJaxAdvice.class);

	/**
	 * Handle.
	 *
	 * @param exc
	 *            the exc
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the response entity
	 */
	
	
	UserSession userSession;
	
	@ExceptionHandler(AmxApiException.class)
	public ResponseEntity<ResponseWrapper<Object>> handle(AmxApiException exc, HttpServletRequest request, HttpServletResponse response) 
	{
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();

		wrapper.setMessage(WebResponseStatus.UNKNOWN_JAX_ERROR, exc);
		
		if (exc.getError().toString().equalsIgnoreCase("USER_LOGIN_ATTEMPT_EXCEEDED")) 
		{
			userSession.unIndexUser();
		}

		wrapper.setException(exc.getClass().getName());

		return new ResponseEntity<ResponseWrapper<Object>>(wrapper, HttpStatus.OK);
	}
}
