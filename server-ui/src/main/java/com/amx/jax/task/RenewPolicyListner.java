package com.amx.jax.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.WebConfig;
import com.amx.jax.constants.DetailsConstants;
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

@TunnelEventMapping(topic = AmibTunnelEvents.Names.RENEW_POLICY, scheme = TunnelEventXchange.TASK_WORKER)
public class RenewPolicyListner implements ITunnelSubscriber<TunnelEvent> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	MetaData metaData;
	
	@Autowired
	private WebConfig webConfig;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String CUST_CD = "CUST_CD";
	public static final String CUST_NAME = "CUST_NAME";
	private static final String MAKE_NAME = "MAKE_NAME";
	private static final String SUBMAKE_NAME = "SUBMAKE_NAME";
	private static final String EXP_DATE = "EXP_DATE";
	public static final String EMAIL = "EMAIL";
	public static final String MOBILE = "MOBILE";
	private static final String LANG_ID = "LANG_ID";

	// CUST_CD:CUST_NAME:MAKE_NAME:SUBMAKE_NAME:EXP_DATE:EMAIL:MOBILE:LANG_ID

	@Override
	public void onMessage(String channel, TunnelEvent event) {

		System.out.println("RenewPolicyListner :: onMessage :: "+ JsonUtil.toJson(event));

		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));

		String custCd = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		String custName = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		String make = ArgUtil.parseAsString(event.getData().get(MAKE_NAME));
		String subMake = ArgUtil.parseAsString(event.getData().get(SUBMAKE_NAME));
		String expDate = ArgUtil.parseAsString(event.getData().get(EXP_DATE));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String mobile = ArgUtil.parseAsString(event.getData().get(MOBILE));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put(DetailsConstants.CUSTOMER_CD, custCd);
		modeldata.put(DetailsConstants.CUSTOMER_NAME, custName);
		modeldata.put(DetailsConstants.MAKE_DESC, make);
		modeldata.put(DetailsConstants.SUB_MAKE_DESC, subMake);
		modeldata.put(DetailsConstants.EXPIRY_DATE, expDate);
		modeldata.put(DetailsConstants.CUSTOMER_EMAIL_ID, emailId);
		modeldata.put(DetailsConstants.CUSTOMER_MOBILE_NO, mobile);
		modeldata.put(DetailsConstants.LANGUAGE_INFO, langId);
		modeldata.put(DetailsConstants.COMPANY_NAME, "AMIB");
		modeldata.put(DetailsConstants.URL_DETAILS, "");
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
			email.setITemplate(TemplatesIB.POLICY_DUE_REMINDER);
			email.setSubject("Al Mulla Insurance Brokerage Policy Due Reminder");
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
		 * if (!ArgUtil.isEmpty(custName)) { PushMessage pushMessage = new
		 * PushMessage(); if (ArgUtil.areEqual(expired, "0")) {
		 * pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRY); } else {
		 * pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRED); }
		 * pushMessage.addToUser(custId); pushMessage.setModel(wrapper);
		 * pushNotifyClient.send(pushMessage); }
		 */

		
		/*if (!ArgUtil.isEmpty(custName)) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRY);
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}*/
		 
		
	}

}
