package com.amx.jax.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.WebConfig;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dict.AmibTunnelEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;

@TunnelEventMapping(topic = AmibTunnelEvents.Names.QUOTE_READY, scheme = TunnelEventXchange.TASK_WORKER)
public class QuoteReadyListener implements ITunnelSubscriber<DBEvent> {

	private static final Logger logger = LoggerFactory.getLogger(QuoteReadyListener.class);
	
	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;
	
	@Autowired
	private WebConfig webConfig;

	@Autowired
	private CustomerRegistrationDao customerRegistrationDao;
	
	String languageInfo = "0";
	
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
	public void onMessage(String channel, DBEvent event) 
	{
		BigDecimal languageId;
		String applId = ArgUtil.parseAsString(event.getData().get(APPL_ID));
		String quoteId = ArgUtil.parseAsString(event.getData().get(QUOTE_ID));
		String quoteVerNumber = ArgUtil.parseAsString(event.getData().get(QUOTE_VERNO));
		String custName = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		String make = ArgUtil.parseAsString(event.getData().get(MAKE_NAME));
		String subMake = ArgUtil.parseAsString(event.getData().get(SUBMAKE_NAME));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String mobile = ArgUtil.parseAsString(event.getData().get(MOBILE));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		languageInfo = langId;
		
		if (null != langId && !langId.equals("")) 
		{
			languageId = new BigDecimal(langId);
		} 
		else 
		{
			languageId = new BigDecimal(0);
		}
		
		logger.info("CivilIDExpiryListner :: getTenantProfile :: languageId :" + languageId);
		
		ArrayResponseModel arrayResponseModel = customerRegistrationDao.getCompanySetUp(languageId , webConfig.getAppCompCode());
		ArrayList<CompanySetUp> getCompanySetUp = arrayResponseModel.getDataArray();
		
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
		logger.info("CivilIDExpiryListner :: getTenantProfile :: getCompanyName :" + getCompanySetUp.get(0).getCompanyName());
		modeldata.put(DetailsConstants.COMPANY_NAME, getCompanySetUp.get(0).getCompanyName());
		modeldata.put(DetailsConstants.URL_DETAILS, "");
		modeldata.put(DetailsConstants.CONTACT_US_EMAIL, getCompanySetUp.get(0).getEmail());
		modeldata.put(DetailsConstants.CONTACT_US_MOBILE, getCompanySetUp.get(0).getHelpLineNumber());
		modeldata.put(DetailsConstants.AMIB_WEBSITE_LINK, getCompanySetUp.get(0).getWebSite());
		if ("1".equals(langId)) 
		{
			modeldata.put(DetailsConstants.COUNTRY_NAME, "????????????");
		} 
		else 
		{
			modeldata.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		}
		

		if (!ArgUtil.isEmpty(emailId)) 
		{
			Email email = new Email();
			if ("1".equals(langId)) 
			{
				logger.info("CivilIDExpiryListner :: getTenantProfile :: Lang AR :" + langId);
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} 
			else 
			{
				logger.info("CivilIDExpiryListner :: getTenantProfile :: Lang EN :" + langId);
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			wrapper.put("data", modeldata);
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setITemplate(TemplatesIB.QUOTE_READY_AMIB);
			
			if ("1".equals(langId)) 
			{
				email.setSubject("?????????? ?????????????? ?????????????? ?????????? ???? ?????????? ?????????? ???????????????? ???????????? ????:" + applId);
			}
			else
			{
				email.setSubject("Al Mulla Insurance Brokerage Quote for your Motor Policy Application :" + applId);
			}
			postManClient.sendEmailAsync(email);
		}

		/*
		 * if (!ArgUtil.isEmpty(custName)) { PushMessage pushMessage = new
		 * PushMessage(); if (ArgUtil.areEqual(languageInfo, "0")) {
		 * pushMessage.setITemplate(TemplatesIB.CIVILID_EXPIRY); } else {
		 * pushMessage.setITemplate(TemplatesIB.CIVILID_EXPIRED); }
		 * pushMessage.setModel(wrapper); pushNotifyClient.send(pushMessage); }
		 */
	}
}
