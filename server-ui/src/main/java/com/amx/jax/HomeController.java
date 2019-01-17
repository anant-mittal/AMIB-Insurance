
package com.amx.jax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.rest.RestService;
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
public class HomeController {

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

	/** The check time. */
	private long checkTime = 0L;

	/** The version new. */
	private String versionNew = "_";

	@Autowired
	private CustomizeQuoteService customizeQuoteService;

	@Autowired
	UserSession userSession;

	@Autowired
	IMetaService metaService;
	
	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired(required = false)
	private HttpServletResponse response;
	

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */

	public String getVersion() {
		long checkTimeNew = System.currentTimeMillis() / (1000 * 60 * 5);
		if (checkTimeNew != checkTime) {
			try {
				Map<String, Object> map = JsonUtil.toMap(restService
						.ajax(appConfig.getCdnURL() + "/dist/build.json?_=" + checkTimeNew).get().asObject());
				if (map.containsKey("version")) {
					versionNew = ArgUtil.parseAsString(map.get("version"));
				}
				checkTime = checkTimeNew;
			} catch (Exception e) {
				logger.info("HomeController :: getVersion() :: Exception :" + e);
			}
		}
		return versionNew;
	}

	// @Timed
	@RequestMapping(value = { "/", "/register/**", "/login/**", "/app/**", "/resetPwd", "/home/**" }, method = {
			RequestMethod.GET })
	public String loginJPage(Model model, HttpServletRequest request) {

		
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webConfig.getAppTitle());
		model.addAttribute("cdnUrl", appConfig.getCdnURL());
		model.addAttribute("cdnVerion", getVersion());
		// model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getFingerprint());
		// model.addAttribute("fcmSenderId", fcmSenderId);
		return "app";
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson() {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);
		wrapper.setMessage(WebResponseStatus.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
		return JsonUtil.toJson(wrapper);
	}

	@RequestMapping(value = { "/pub/terms" }, method = { RequestMethod.GET })
	public String termsAndCondition(Model model) {
		JSONObject dataJson = new JSONObject();
		ArrayList<String> dataList = new ArrayList<>();
		try {
			TreeMap<Integer, String> data = customizeQuoteService.getTermsAndConditionTest();
			Iterator it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				dataList.add(pair.getValue().toString());
				it.remove();
			}
			model.addAttribute("terms", dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "terms";
	}

	
	@RequestMapping(value = { "/pub/checkIp" }, method = { RequestMethod.GET })
	public String getClientIpAddr(HttpServletRequest request) 
	{
		HashMap<String,String> dataJson = new HashMap();
		
		String ip = request.getHeader("X-Forwarded-For");
		dataJson.put("X-Forwarded-For", ip);
		
		logger.info("X-Forwarded-For :: ip :" + ip);
		
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.info("RemoteIpTest :: Proxy-Client-IP :: ip :" + ip);
			dataJson.put("Proxy-Client-IP", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			logger.info("RemoteIpTest :: WL-Proxy-Client-IP :: ip :" + ip);
			dataJson.put("WL-Proxy-Client-IP", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.info("RemoteIpTest :: HTTP_X_FORWARDED_FOR :: ip :" + ip);
			dataJson.put("HTTP_X_FORWARDED_FOR", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED");
			logger.info("RemoteIpTest :: HTTP_X_FORWARDED :: ip :" + ip);
			dataJson.put("HTTP_X_FORWARDED", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
			logger.info("RemoteIpTest :: HTTP_X_CLUSTER_CLIENT_IP :: ip :" + ip);
			dataJson.put("HTTP_X_CLUSTER_CLIENT_IP", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			logger.info("RemoteIpTest :: HTTP_CLIENT_IP :: ip :" + ip);
			dataJson.put("HTTP_CLIENT_IP", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
			logger.info("RemoteIpTest :: HTTP_FORWARDED_FOR :: ip :" + ip);
			dataJson.put("HTTP_FORWARDED_FOR", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED");
			logger.info("RemoteIpTest :: HTTP_FORWARDED :: ip :" + ip);
			dataJson.put("HTTP_FORWARDED", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_VIA");
			logger.info("RemoteIpTest :: HTTP_VIA :: ip :" + ip);
			dataJson.put("HTTP_VIA", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("REMOTE_ADDR");
			logger.info("RemoteIpTest :: REMOTE_ADDR :: ip :" + ip);
			dataJson.put("REMOTE_ADDR", ip);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
			logger.info("RemoteIpTest :: getRemoteAddr :: ip :" + ip);
			dataJson.put("request.getRemoteAddr()", ip);
		}
		
		ResponseWrapper<HashMap> wrapper = new ResponseWrapper<HashMap>(null);
		wrapper.setMessage(dataJson.toString());
		return JsonUtil.toJson(wrapper);
		
	}

}
