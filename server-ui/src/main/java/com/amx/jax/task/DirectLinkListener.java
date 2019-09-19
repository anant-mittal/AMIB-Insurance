package com.amx.jax.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dict.AmibTunnelEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.ui.session.UserSession;
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
	UserSession userSession;

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;
	
	@Autowired
	PushNotifyClient pushNotifyClient;

	private static final String LINK_ID = "LINK_ID";
	private static final String QUOTE_ID = "QUOTE_ID";
	// private static final String QUOTE_VERNO = "QUOTE_VERNO";
	private static final String CODE = "CODE";
	private static final String LANG_ID = "LANG_ID";

	@Override
	public void onMessage(String channel, DBEvent message) {

		logger.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(message));

		BigDecimal linkId = ArgUtil.parseAsBigDecimal(message.getData().get(LINK_ID), new BigDecimal(0));
		BigDecimal quoteId =ArgUtil.parseAsBigDecimal(message.getData().get(QUOTE_ID), new BigDecimal(0));
		// BigDecimal quoteVerNo =
		// ArgUtil.parseAsBigDecimal(message.getData().get(QUOTE_VERNO), new
		// BigDecimal(0));
		String code = ArgUtil.parseAsString(message.getData().get(CODE));
		BigDecimal langId = ArgUtil.parseAsBigDecimal(message.getData().get(LANG_ID), new BigDecimal(0));
		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(userSession.getCivilId(),
				HardCodedValues.USER_TYPE, userSession.getUserSequenceNumber(), userSession.getLanguageId());

		logger.debug("Customer object is " + customerDetailModel.toString());
		String emailId = customerDetailModel.getEmail();
		String smsNo = customerDetailModel.getMobile();
		BigDecimal custId = customerDetailModel.getCustSequenceNumber();
		
		
		String custName = customerDetailModel.getUserName();

		Map<String, Object> modelData = new HashMap<String, Object>();
		Map<String, Object> wrapper = new HashMap<String, Object>();
		
		modelData.put("to", emailId);
		modelData.put("custname", custName);
		modelData.put("linkid", linkId);
		modelData.put("code", code);
		modelData.put("quoteid", quoteId);
		
		Email email = new Email();
		if ("2".equals(langId)) {
			email.setLang(Language.AR);
			modelData.put("languageid", Language.AR);
		} else {
			email.setLang(Language.EN);
			modelData.put("languageid", Language.EN);
		}
		wrapper.put("data", modelData);
		if (!ArgUtil.isEmpty(emailId)) {
			logger.debug("Json value of wrapper is " + JsonUtil.toJson(wrapper));
			email.setITemplate(TemplatesMX.PAYMENT_LINK);
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
			sms.setITemplate(TemplatesMX.PAYMENT_LINK);
			sms.addTo(smsNo);
			sms.setModel(wrapper);
			postmanClient.sendSMS(sms);
		}
		
		if(!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.PAYMENT_LINK);
			pushMessage.setModel(wrapper);
			pushMessage.addToUser(custId);
			
			pushNotifyClient.send(pushMessage);
		}
	}
	
	

}
