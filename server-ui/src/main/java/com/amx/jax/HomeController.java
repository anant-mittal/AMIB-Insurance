
package com.amx.jax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.models.MetaData;
import com.amx.jax.rest.RestService;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.session.UserSession;
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
	CommonHttpRequest httpService;

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

		
		/*long checkTimeNew = System.currentTimeMillis() / (1000 * 60 * 5);
		if (checkTimeNew != checkTime) 
		{
			try 
			{
				Map<String, Object> map = JsonUtil.toMap(restService.ajax(appConfig.getCdnURL() + "/dist/build.json?_=" + checkTimeNew).get().asObject());
				if (map.containsKey("version")) 
				{
					versionNew = ArgUtil.parseAsString(map.get("version"));
				}
				checkTime = checkTimeNew;
			} 
			catch (Exception e) 
			{
				System.out.println("getVersion Exception"+ e);
			}
		}
		System.out.println("getVersion Exception versionNew :"+ versionNew);
		return versionNew;*/

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

	//@Timed
	@RequestMapping(value = {"/login/**" , "/resetPwd"}, method = { RequestMethod.GET })
	public String loginJPage(Model model)
	{
		System.out.println("HomeController :: loginJPage 1 :: getLanguage : " + httpService.getLanguage());
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webConfig.getAppTitle());
		model.addAttribute("cdnUrl", appConfig.getCdnURL());
		model.addAttribute("cdnVerion", getVersion());
		//model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getFingerprint());
		//model.addAttribute("fcmSenderId", fcmSenderId);
		return "app";
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = { "Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson()
	{
		System.out.println("HomeController :: loginJPage 2 :: getLanguage : " + httpService.getLanguage());
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);
		wrapper.setMessage(WebResponseStatus.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
		System.out.println("HomeController :: loginJPage 2 :: JsonUtil.toJson(wrapper): " + JsonUtil.toJson(wrapper));
		
		return JsonUtil.toJson(wrapper);
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
		JSONObject dataJson = new JSONObject();
		ArrayList<String> dataList = new ArrayList<>();
		try
		{
			TreeMap<Integer, String> data = customizeQuoteService.getTermsAndConditionTest();
			Iterator it = data.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				dataList.add(pair.getValue().toString());
				it.remove();
			}
			model.addAttribute("terms",dataList);
			System.out.println("HomeController :: termsAndCondition :: dataJson :" + dataList.toString());
			System.out.println("HomeController :: termsAndCondition :: model    :" + model.asMap());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
