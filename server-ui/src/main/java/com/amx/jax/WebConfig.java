
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
public class WebConfig
{

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	public static final String APP_NAME = "${app.name}";

	public static final String APP_TITLE = "${app.title}";

	public static final String APP_PROD = "${app.prod}";

	public static final String APP_SWAGGER = "${app.swagger}";

	public static final String APP_DEBUG = "${app.debug}";

	public static final String APP_CACHE = "${app.cache}";

	public static final String APP_AUTH_KEY = "${app.auth.key}";

	public static final String APP_AUTH_ENABLED = "${app.auth.enabled}";

	public static final String AMX_CDN_URL = "${amx.cdn.url}";

	public static final String AMX_APP_URL = "${amx.app.url}";

	public static final String APP_COMP = "${app.company.code}";

	public static final String CONFIG_EMAIL = "${spring.mail.username}";

	@Value(APP_NAME)
	@AppParamKey(AppParam.APP_NAME)
	private String appName;

	@Value(APP_NAME)
	private String appTitle;

	@Value(APP_COMP)
	private String appComp;

	@Value(CONFIG_EMAIL)
	private String configEmail;

	public String getConfigEmail()
	{
		return configEmail;
	}

	public String getAppCompCode()
	{
		return appComp;
	}

	public String getAppTitle()
	{
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

	@Value(AMX_CDN_URL)
	@AppParamKey(AppParam.JAX_CDN_URL)
	private String cdnURL;

	@Value(AMX_APP_URL)
	@AppParamKey(AppParam.JAX_APP_URL)
	private String appURL;

	@Value("${server.session.cookie.http-only}")
	private boolean cookieHttpOnly;

	@Value("${server.session.cookie.secure}")
	private boolean cookieSecure;

	public boolean isCookieHttpOnly()
	{
		return cookieHttpOnly;
	}

	public boolean isCookieSecure()
	{
		return cookieSecure;
	}

	public String getAppName()
	{
		return appName;
	}

	public Boolean isProdMode()
	{
		return prodMode;
	}

	public Boolean isSwaggerEnabled()
	{
		return swaggerEnabled;
	}

	public Boolean isDebug()
	{
		return debug;
	}

	public Boolean isCache()
	{
		return cache;
	}

	public String getCdnURL()
	{
		return cdnURL;
	}

	public String getAppURL()
	{
		return appURL;
	}

	@Bean
	public AppParam loadAppParams()
	{

		for (Field field : WebConfig.class.getDeclaredFields())
		{
			AppParamKey s = field.getAnnotation(AppParamKey.class);
			Value v = field.getAnnotation(Value.class);
			if (s != null && v != null)
			{
				Matcher match = pattern.matcher(v.value());
				if (match.find())
				{
					s.value().setProperty(match.group(1));
				}

				String typeName = field.getGenericType().getTypeName();
				Object value = null;
				try
				{
					value = field.get(this);
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}

				if ("java.lang.String".equals(typeName))
				{
					s.value().setValue(ArgUtil.parseAsString(value));
				}
				else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName))
				{
					s.value().setEnabled(ArgUtil.parseAsBoolean(value));
				}
			}
		}

		return null;
	}

}
