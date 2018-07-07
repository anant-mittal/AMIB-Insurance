




package com.insurance.user_registartion.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

/*import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;*/
import com.google.common.util.concurrent.ExecutionError;
import com.insurance.constant.CommunicationChannel;
import com.insurance.error.GlobalException;
import com.insurance.error.JaxError;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;
import com.insurance.user_registartion.dao.CustomerRegistrationDao;
import com.insurance.user_registartion.model.CivilIdOtpModel;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.user_registartion.model.CustomerRegistrationDetails;
import com.insurance.user_registartion.model.PersonInfo;

public class CustomerRegistrationService extends AbstractService
{

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Autowired
	CustomerPersonalDetail customerPersonalDetail;

	//public PostManService postManService;

	@Override
	public String getModelType()
	{
		return null;
	}

	public ApiResponse addNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		try
		{
			ApiResponse apiResponse = customerRegistrationDao.addNewCustomer(customerPersonalDetail);
			return apiResponse;
		}
		catch (Exception e)
		{
			throw new GlobalException("Customer Registration Failed Kindly try after soem time .");
		}
	}

	public ApiResponse sendOtpForCivilId(String civilId)
	{
		return sendOtp(civilId, null, null);
	}
	
	public ApiResponse sendOtp(String civilId, List<CommunicationChannel> channels, Boolean initRegistration)
	{
		
		CivilIdOtpModel model = new CivilIdOtpModel();
		
		PersonInfo personinfo = new PersonInfo();
		
		personinfo.setEmail("abhishektiwaribecse@gmail.com");
		personinfo.setMobile("8796589233");
		
		//sendOtpSms(personinfo, model);
		
		ApiResponse response = getBlackApiResponse();
		
		response.setResponseStatus(ResponseStatus.OK);
		
		return response;
	}
	

	/*public void sendOtpSms(PersonInfo pinfo, CivilIdOtpModel model)
	{

		SMS sms = new SMS();
		sms.addTo("8796589233");
		sms.getModel().put("data", model);
		sms.setTemplate(Templates.RESET_OTP_SMS);

		try
		{
			postManService.sendSMSAsync(sms);
			//if (!appConfig.isProdMode())
			{
				//sendToSlack("mobile", sms.getTo().get(0), model.getmOtpPrefix(), model.getmOtp());
				sendToSlack("mobile", sms.getTo().get(0), "AMX ", "1234");
			}
		}
		catch (PostManException e)
		{

		}
	}

	public void sendOtpEmail(PersonInfo pinfo, CivilIdOtpModel civilIdOtpModel)
	{

		Email email = new Email();
		email.setSubject("OTP Email");
		//email.addTo(pinfo.getEmail());
		email.addTo("abhishektiwaribecse@gmail.com");
		email.setTemplate(Templates.RESET_OTP);
		email.setHtml(true);
		email.getModel().put("data", civilIdOtpModel);

		sendEmail(email);

		//if (!appConfig.isProdMode())
		{
			//sendToSlack("email", email.getTo().get(0), civilIdOtpModel.geteOtpPrefix(), civilIdOtpModel.geteOtp());
			sendToSlack("email", email.getTo().get(0), "AMX ", "1234");
		}

	}// end of sendOtpEmail

	private void sendEmail(Email email)
	{
		try
		{
			postManService.sendEmailAsync(email);
		}
		catch (PostManException e)
		{
			
		}
	}

	public void sendToSlack(String channel, String to, String prefix, String otp)
	{
		Notipy msg = new Notipy();
		msg.setMessage(String.format("%s = %s", channel, to));
		msg.addLine(String.format("OTP = %s-%s", prefix, otp));
		msg.setChannel(Channel.NOTIPY);
		try
		{
			postManService.notifySlack(msg);
		}
		catch (PostManException e)
		{
			
		}
	}*/

}
