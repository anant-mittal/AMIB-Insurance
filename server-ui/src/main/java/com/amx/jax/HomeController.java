
package com.amx.jax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.rest.RestService;
import com.amx.jax.service.HttpService;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.ui.session.UserSession;
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
	private MetaData metaData;

	/** The check time. */
	private long checkTime = 0L;

	/** The version new. */
	private String versionNew = "_";

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private CustomizeQuoteService customizeQuoteService;

	@Autowired
	UserSession userSession;

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
	 * Login ping
	 * 
	 * @param request
	 *            the request
	 * @return the string
	 */
	@RequestMapping(value = "/pub/meta/**", method = { RequestMethod.GET })
	@ResponseBody
	public String loginPing(HttpServletRequest request)
	{
		System.out.println("HomeController :: loginPing :: getLanguage : " + httpService.getLanguage());
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
	/*
	 * @RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	 * public String loginJPage(Model model) {
	 * System.out.println("HomeController :: loginJPage :: getLanguage : " +
	 * httpService.getLanguage()); model.addAttribute("lang",
	 * httpService.getLanguage()); model.addAttribute("applicationTitle",
	 * webConfig.getAppTitle()); model.addAttribute("cdnUrl",
	 * appConfig.getCdnURL()); model.addAttribute("cdnVerion", getVersion());
	 * return "app"; }
	 */

	/**
	 * Login P json.
	 *
	 * @return the string
	 */

	@RequestMapping(value = "/login/**")
	@ResponseBody
	public AmxApiResponse<Object, Object> UnAuthorizedAccess()
	{
		System.out.println("HomeController :: UnAuthorizedAccess :: getLanguage :" + httpService.getLanguage());

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			userSession = null;
			laguageSetUp();
			resp.setStatusKey(ApiConstants.AUTHORIZATION_FAILURE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.AUTHORIZATION_FAILURE);
		}
		return resp;
	}

	@RequestMapping(value = "/logout/**", method = { RequestMethod.GET })
	@ResponseBody
	public AmxApiResponse<Object, Object> logOut()
	{
		System.out.println("HomeController :: logOut :: getLanguage :" + httpService.getLanguage());

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			userSession = null;
			laguageSetUp();
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.LOGOUT_MESSAGE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	/**
	 * Default page.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = { "/", "/register/**", "/app/**", "/home/**", "/" }, method = { RequestMethod.GET })
	public String defaultPage(Model model)
	{
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webConfig.getAppTitle());
		model.addAttribute("cdnUrl", appConfig.getCdnURL());
		model.addAttribute("cdnVerion", getVersion());

		laguageSetUp();
		return "app";
	}

	@RequestMapping(value = { "/pub/terms" }, method = { RequestMethod.GET })
	public String termsAndCondition(Model model)
	{
		ArrayList<String> termsInfo = new ArrayList<String>();
		TreeMap<Integer, String> data = customizeQuoteService.getTermsAndConditionTest();

		Iterator it = data.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			termsInfo.add(pair.getValue().toString());
			it.remove();
		}

		for (int i = 0; i < termsInfo.size(); i++)
		{
			System.out.println("HomeController :: termsAndCondition :: termsInfo  : " + termsInfo.get(i));
		}

		model.addAllAttributes(termsInfo);

		return "terms";
	}

	public void laguageSetUp()
	{
		if (httpService.getLanguage().toString().equalsIgnoreCase("EN"))
		{
			metaData.setLanguageId(new BigDecimal(0));
			customerRegistrationService.getCompanySetUp();
		}
	}

}
