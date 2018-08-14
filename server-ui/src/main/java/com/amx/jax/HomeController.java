




package com.amx.jax;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.amx.jax.AppConfig;
import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rest.RestService;
import com.amx.jax.service.HttpService;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.session.RegSession;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import io.swagger.annotations.Api;

/**
 * The Class HomeController.
 */
@Controller
@Api(value = "Auth APIs")
public class HomeController
{

	String TAG = "com.amx.jax.services :: HomeController :: ";

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/** The web app config. */
	@Autowired
	private WebConfig webConfig;

	@Autowired
	private AppConfig appConfig;

	/** The http service. */
	@Autowired
	HttpService httpService;

	@Autowired
	RestService restService;

	@Autowired
	RegSession regSession;

	/** The check time. */
	private long checkTime = 0L;

	/** The version new. */
	private String versionNew = "_";

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion()
	{

		/*
		 * long checkTimeNew = System.currentTimeMillis() / (1000 * 60 * 5); if
		 * (checkTimeNew != checkTime) { try { Map<String, Object> map =
		 * JsonUtil.toMap(restService.ajax(webConfig.getCdnURL() +
		 * "/dist/build.json?_=" + checkTimeNew).get().asObject()); if
		 * (map.containsKey("version")) { versionNew =
		 * ArgUtil.parseAsString(map.get("version")); } checkTime =
		 * checkTimeNew; } catch (Exception e) {
		 * LOGGER.error("getVersion Exception", e); } } return versionNew;
		 */

		return "1.0.1";

	}

	/**
	 * Login ping.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 */
	@RequestMapping(value = "/pub/meta/**", method = { RequestMethod.GET })
	@ResponseBody
	public String loginPing(HttpServletRequest request)
	{
		AmxApiResponse<Object, Object> wrapper = new AmxApiResponse<Object, Object>();
		return JsonUtil.toJson(wrapper);
	}

	/**
	 * Login J page.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model)
	{
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webConfig.getAppTitle());
		model.addAttribute("cdnUrl", webConfig.getCdnURL());
		model.addAttribute("cdnVerion", getVersion());
		return "app";
	}

	/**
	 * Login P json.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = { "Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson()
	{
		AmxApiResponse<Object, Object> wrapper = new AmxApiResponse<Object, Object>();
		// wrapper.setMessage(WebResponseStatus.UNAUTHORIZED,
		// ResponseMessage.UNAUTHORIZED);
		return JsonUtil.toJson(wrapper);
	}

	/**
	 * Default page.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = { "/register/**", "/app/**", "/home/**", "/" }, method = { RequestMethod.GET })
	public String defaultPage(Model model)
	{
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webConfig.getAppTitle());
		model.addAttribute("cdnUrl", webConfig.getCdnURL());
		model.addAttribute("cdnVerion", getVersion());
		
		if (httpService.getLanguage().toString().equalsIgnoreCase("EN"))
		{
			regSession.setLanguageId(0);
			customerRegistrationService.getCompanySetUp(0, httpService.getDeviceId());
		}

		
		return "app";
	}

}
