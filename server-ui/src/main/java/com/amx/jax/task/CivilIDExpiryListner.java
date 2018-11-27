package com.amx.jax.task;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.WebConfig;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.dao.CustomizeQuoteDao;
import com.amx.jax.dict.AmibTunnelEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.models.MetaData;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmibTunnelEvents.Names.QUOTE_READY, scheme = TunnelEventXchange.TASK_WORKER)
public class CivilIDExpiryListner implements ITunnelSubscriber<TunnelEvent> {

	private static final Logger logger = LoggerFactory.getLogger(CivilIDExpiryListner.class);
	
	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;
	
	@Autowired
	private WebConfig webConfig;

	@Autowired
	MetaData metaData;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String APPL_ID = "APPL_ID";
	private static final String QUOTE_ID = "QUOTE_ID";
	private static final String QUOTE_VERNO = "QUOTE_VERNO";
	public static final String CUST_NAME = "CUST_NAME";
	private static final String MAKE_NAME = "MAKE_NAME";
	private static final String SUBMAKE_NAME = "SUBMAKE_NAME";
	public static final String EMAIL = "EMAIL";
	public static final String MOBILE = "MOBILE";
	private static final String LANG_ID = "LANG_ID";

	// APPL_ID:QUOTE_ID:QUOTE_VERNO:CUST_NAME:MAKE_NAME:SUBMAKE_NAME:EMAIL:MOBILE:LANG_ID

	@Override
	public void onMessage(String channel, TunnelEvent event) {
		logger.info(" onMessage :: "+ JsonUtil.toJson(event));

		String applId = ArgUtil.parseAsString(event.getData().get(APPL_ID));
		String quoteId = ArgUtil.parseAsString(event.getData().get(QUOTE_ID));
		String quoteVerNumber = ArgUtil.parseAsString(event.getData().get(QUOTE_VERNO));
		String custName = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		String make = ArgUtil.parseAsString(event.getData().get(MAKE_NAME));
		String subMake = ArgUtil.parseAsString(event.getData().get(SUBMAKE_NAME));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String mobile = ArgUtil.parseAsString(event.getData().get(MOBILE));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put(DetailsConstants.APPLICATION_ID, applId);
		modeldata.put(DetailsConstants.QUOTE_SEQ_NUM, quoteId);
		modeldata.put(DetailsConstants.QUOTE_VER_NUMBER, quoteVerNumber);
		modeldata.put(DetailsConstants.CUSTOMER_NAME, custName);
		modeldata.put(DetailsConstants.MAKE_DESC, make);
		modeldata.put(DetailsConstants.SUB_MAKE_DESC, subMake);
		modeldata.put(DetailsConstants.CUSTOMER_EMAIL_ID, emailId);
		modeldata.put(DetailsConstants.CUSTOMER_MOBILE_NO, mobile);
		modeldata.put(DetailsConstants.LANGUAGE_INFO, langId);
		modeldata.put(DetailsConstants.COMPANY_NAME, webConfig.getAppCompCode());
		modeldata.put(DetailsConstants.URL_DETAILS, "");
		logger.info("getTenantProfile :: getContactUsEmail :" + webConfig.getTenantProfile().getContactUsEmail());
		modeldata.put(DetailsConstants.CONTACT_US_EMAIL, webConfig.getTenantProfile().getContactUsEmail());
		modeldata.put(DetailsConstants.CONTACT_US_MOBILE, webConfig.getTenantProfile().getContactUsHelpLineNumber());
		modeldata.put(DetailsConstants.AMIB_WEBSITE_LINK, webConfig.getTenantProfile().getAmibWebsiteLink());
		modeldata.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");

		if (!ArgUtil.isEmpty(emailId)) {
			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			wrapper.put("data", modeldata);

			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setITemplate(TemplatesIB.QUOTE_READY_AMIB);
			email.setSubject("Al Mulla Insurance Brokerage Quote for your Motor Policy Application :" + applId);
			postManClient.sendEmailAsync(email);

			/*
			 * if (ArgUtil.areEqual(expired, "0")) {
			 * email.setITemplate(TemplatesMX.CIVILID_EXPIRY);
			 * email.setSubject("Civil ID Expiry Reminder"); // Given by Umesh } else {
			 * email.setSubject("Civil ID has been expired"); // Given by Umesh
			 * email.setITemplate(TemplatesMX.CIVILID_EXPIRED); }
			 */

		}

		/*
		 * if (!ArgUtil.isEmpty(custId)) { PushMessage pushMessage = new PushMessage();
		 * if (ArgUtil.areEqual(expired, "0")) {
		 * pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRY); } else {
		 * pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRED); }
		 * pushMessage.addToUser(custId); pushMessage.setModel(wrapper);
		 * pushNotifyClient.send(pushMessage); }
		 */

	}
}
