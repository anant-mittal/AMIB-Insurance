
package com.amx.jax;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amx.utils.ArgUtil;

@Configuration
@PropertySource("classpath:application-lib.properties")
public class WebConfig {

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	public static final String APP_NAME = "${app.name}";

	public static final String APP_TITLE = "${app.title}";

	public static final String APP_PROD = "${app.prod}";

	public static final String APP_SWAGGER = "${app.swagger}";

	public static final String APP_DEBUG = "${app.debug}";

	public static final String APP_CACHE = "${app.cache}";

	public static final String APP_AUTH_KEY = "${app.auth.key}";

	public static final String APP_AUTH_ENABLED = "${app.auth.enabled}";

	public static final String APP_COMP = "${app.company.code}";

	//public static final String CONFIG_EMAIL = "${spring.mail.username}";
	
	public static final String PAYMENT_URL = "${jax.payment.url}";
	

	@Value(APP_NAME)
	@AppParamKey(AppParam.APP_NAME)
	private String appName;

	@Value(APP_NAME)
	private String appTitle;

	@Value(APP_COMP)
	private String appComp;

	/*@Value(CONFIG_EMAIL)
	private String configEmail;

	public String getConfigEmail() {
		return configEmail;
	}*/

	@Value(PAYMENT_URL)
	private String paymentUrl;

	public String getPaymentUrl() 
	{
		return paymentUrl;
	}
	
	public String getAppCompCode() {
		return appComp;
	}

	public String getAppTitle() {
		return appTitle;
	}
	
	

	@Value(APP_PROD)
	@AppParamKey(AppParam.APP_PROD)
	private Boolean prodMode;

	@Value(APP_SWAGGER)
	@AppParamKey(AppParam.APP_SWAGGER)
	private boolean swaggerEnabled;

	@Value(APP_DEBUG)
	@AppParamKey(AppParam.APP_DEBUG)
	private Boolean debug;

	@Value(APP_AUTH_KEY)
	private String appAuthKey;

	@Value(APP_AUTH_ENABLED)
	@AppParamKey(AppParam.APP_AUTH_ENABLED)
	private boolean appAuthEnabled;

	@Value(APP_CACHE)
	@AppParamKey(AppParam.APP_CACHE)
	private Boolean cache;

	@Value("${server.session.cookie.http-only}")
	private boolean cookieHttpOnly;

	@Value("${server.session.cookie.secure}")
	private boolean cookieSecure;

	public boolean isCookieHttpOnly() {
		return cookieHttpOnly;
	}

	public boolean isCookieSecure() {
		return cookieSecure;
	}

	public String getAppName() {
		return appName;
	}

	public Boolean isDebug() {
		return debug;
	}

	public Boolean isCache() {
		return cache;
	}

}
