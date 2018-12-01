package com.amx.jax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class WebAppStatus extends IStatusCodeListPlugin<WebAppStatusCodes, ApiWebAppStatus> {

	/**
	 * Error Enum Codes for Offsite Server
	 * 
	 * @author lalittanwar
	 *
	 */
	public static enum WebAppStatusCodes implements IExceptionEnum {
		CLIENT_UNKNOWN, TECHNICAL_ERROR, SUCCESS,
		CIVIL_ID_VALID, CIVIL_ID_INVALID, CIVIL_ID_EXPIRED,
		CIVIL_ID_ALREADY_REGISTERED, CIVIL_ID_NOT_REGESTERED,
		MOBILE_NUMBER_REGISTERED, MOBILE_NUMBER_NOT_REGISTERED, INVALID_MOBILE,
		EMAIL_ID_VALID, EMAIL_ID_INVALID ,
		EMAIL_ID_REGESTERED, EMAIL_ID_NOT_REGESTERED,
		EMPTY_PASSWORD, CP_OTP_NOT_GENERATED,
		MOBILE_OR_EMAIL_ALREADY_EXISTS,
		USER_OTP_ENABLED, USER_OTP_DISABLED,
		NO_QUOTE_AVAILABLE,
		
		
		
		INVALID_CLIENT_SESSION, INVALID_CLIENT_REQUEST,

		OFFSITE_SERVER_ERROR, DOTP_REQUIRED, MOTP_REQUIRED, EOTP_REQUIRED;

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return 8000 + this.ordinal();
		}

	}

	/**
	 * 
	 * API Status Enum for Offiste Server APis
	 * 
	 * @author lalittanwar
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ApiWebAppStatus {
		WebAppStatusCodes[] value() default { WebAppStatusCodes.OFFSITE_SERVER_ERROR };
	}

	/**
	 * 
	 * @author lalittanwar
	 *
	 */
	public static class OffsiteServerError extends AmxApiException {

		private static final long serialVersionUID = 1L;

		public OffsiteServerError(AmxApiError error) {
			super(error);
		}

		public OffsiteServerError() {
			super("Offsite Server error occured");
			this.setError(WebAppStatusCodes.OFFSITE_SERVER_ERROR);
		}

		public OffsiteServerError(WebAppStatusCodes statusCode) {
			super(statusCode);
		}

		public OffsiteServerError(WebAppStatusCodes statusCode, String message) {
			super(statusCode, message);
		}

		public OffsiteServerError(Exception e) {
			super(e);
			this.setError(WebAppStatusCodes.OFFSITE_SERVER_ERROR);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new OffsiteServerError(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return WebAppStatusCodes.OFFSITE_SERVER_ERROR;
		}

		public static <T> T evaluate(Exception e) {
			throw new OffsiteServerError(e);
		}

		@Override
		public boolean isReportable() {
			return false;
		}

	}

	@Override
	public Class<ApiWebAppStatus> getAnnotionClass() {
		return ApiWebAppStatus.class;
	}

	@Override
	public WebAppStatusCodes[] getValues(ApiWebAppStatus annotation) {
		return annotation.value();
	}

}
