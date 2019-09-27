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
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dbmodel.CustomerModel;
import com.amx.jax.dbmodel.PaymentLinkModel;
import com.amx.jax.dict.AmibTunnelEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IPaymentLinkRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmibTunnelEvents.Names.PAYMENT_LINK, scheme = TunnelEventXchange.TASK_WORKER)
public class DirectLinkListener implements ITunnelSubscriber<DBEvent> {
	private static final Logger logger = LoggerFactory.getLogger(DirectLinkListener.class);

	@Autowired
	PostManClient postmanClient;

	@Autowired
	PostManService postmanService;

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;
	
	@Autowired
	PushNotifyClient pushNotifyClient;
	
	@Autowired
	IPaymentLinkRepository iPaymentLinkRepository;
	
	@Autowired
	WebConfig webConfig;
	
	@Autowired
	ICustomerRepository iCustomerRepository;


	private static final String LINK_ID = "LINK_ID";
	private static final String QUOTE_ID = "QUOTE_ID";
	// private static final String QUOTE_VERNO = "QUOTE_VERNO";
	private static final String CODE = "CODE";
	private static final String LANG_ID = "LANG_ID";

	@Override
	public void onMessage(String channel, DBEvent message) {

		logger.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(message));

		BigDecimal linkId = ArgUtil.parseAsBigDecimal(message.getData().get(LINK_ID), new BigDecimal(0));
		BigDecimal quoteId =ArgUtil.parseAsBigDecimal(message.getData().get(QUOTE_ID), new BigDecimal(0));
		// BigDecimal quoteVerNo =
		// ArgUtil.parseAsBigDecimal(message.getData().get(QUOTE_VERNO), new
		// BigDecimal(0));
		String code = ArgUtil.parseAsString(message.getData().get(CODE));
		BigDecimal langId = ArgUtil.parseAsBigDecimal(message.getData().get(LANG_ID), new BigDecimal(0));
		PaymentLinkModel paymentLinkModel = iPaymentLinkRepository.findOne(linkId);
		CustomerModel customerModel = iCustomerRepository.findByCustSeqNo(paymentLinkModel.getCustSeqNo());
		
		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(customerModel.getIdNo(),
				HardCodedValues.USER_TYPE, customerModel.getUserSeqNo(), langId);
		
		ArrayResponseModel arrayResponseModel = customerRegistrationDao.getCompanySetUp(langId , webConfig.getAppCompCode());
		ArrayList<CompanySetUp> getCompanySetUp = arrayResponseModel.getDataArray();
		
		logger.info("Customer object is " + customerDetailModel.toString());
		String emailId = customerDetailModel.getEmail();
		String smsNo = customerDetailModel.getMobile();
		String custName = customerDetailModel.getUserName();

		Map<String, Object> modelData = new HashMap<String, Object>();
		Map<String, Object> wrapper = new HashMap<String, Object>();
		
		modelData.put("to", emailId);
		modelData.put("custname", custName);
		modelData.put("linkid", linkId);
		modelData.put("code", code);
		modelData.put("quoteid", quoteId);
		modelData.put("linkDate", paymentLinkModel.getLinkDate());
		modelData.put("amount", paymentLinkModel.getPaymentAmount());
		modelData.put(DetailsConstants.COMPANY_NAME, getCompanySetUp.get(0).getCompanyName());
		modelData.put(DetailsConstants.CONTACT_US_EMAIL, getCompanySetUp.get(0).getEmail());
		modelData.put(DetailsConstants.CONTACT_US_MOBILE, getCompanySetUp.get(0).getHelpLineNumber());
		modelData.put(DetailsConstants.AMIB_WEBSITE_LINK, getCompanySetUp.get(0).getWebSite());
		
		
		Email email = new Email();
		if ("2".equals(langId)) {
			email.setLang(Language.AR);
			modelData.put("languageid", Language.AR);
			modelData.put(DetailsConstants.COUNTRY_NAME, "الكويت");
		} else {
			email.setLang(Language.EN);
			modelData.put("languageid", Language.EN);
			modelData.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		}
		wrapper.put("data", modelData);
		if (!ArgUtil.isEmpty(emailId)) {
			logger.debug("Json value of wrapper is " + JsonUtil.toJson(wrapper));
			email.setITemplate(TemplatesIB.PAYMENT_LINK);
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);

			postmanClient.sendEmail(email);
		}
		
		if(!ArgUtil.isEmpty(smsNo)) {
			SMS sms = new SMS();
			if ("2".equals(langId)) {
				sms.setLang(Language.AR);
				modelData.put("languageid", Language.AR);
			} else {
				sms.setLang(Language.EN);
				modelData.put("languageid", Language.EN);
			}
			sms.setITemplate(TemplatesIB.PAYMENT_LINK_SMS);
			sms.addTo(smsNo);
			sms.setModel(wrapper);
			postmanClient.sendSMS(sms);
		}
		
		
	}
	
	

}
