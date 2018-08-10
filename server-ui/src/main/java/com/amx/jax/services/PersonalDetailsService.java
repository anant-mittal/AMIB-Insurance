
package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerDetailRequest;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.CustomerProfileDetailRequest;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.session.RegSession;

@Service
public class PersonalDetailsService
{
	String TAG = "com.amx.jax.services :: PersonalDetailsService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Autowired
	RegSession regSession;

	public AmxApiResponse<CustomerDetailResponse, Object> getUserDetails(CustomerDetailRequest customerLoginRequest)
	{
		AmxApiResponse<CustomerDetailResponse, Object> resp = new AmxApiResponse<CustomerDetailResponse, Object>();
		CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse();

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(customerLoginRequest.getCivilId());

		customerDetailResponse.setMobile(customerDetailModel.getMobile());
		customerDetailResponse.setEmail(customerDetailModel.getEmail());
		customerDetailResponse.setLanguageId(customerDetailModel.getLanguageId());
		customerDetailResponse.setMailVerify(customerDetailModel.getMobileVerify());
		customerDetailResponse.setMailVerify(customerDetailModel.getMailVerify());
		customerDetailResponse.setLastLogin(customerDetailModel.getLastLogin());
		customerDetailResponse.setDeviceId(customerDetailModel.getDeviceId());
		customerDetailResponse.setDeviceType(customerDetailModel.getDeviceType());
		customerDetailResponse.setCivilId(customerDetailModel.getCivilId());
		customerDetailResponse.setDbStatus(customerDetailModel.getDbStatus());
		customerDetailResponse.setCustSeqNumber(customerDetailModel.getCustSequenceNumber());

		if (customerDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		resp.setMessageKey(customerDetailModel.getErrorCode());
		resp.setMessage(customerDetailModel.getErrorCode());
		resp.setData(customerDetailResponse);

		logger.info(TAG + " getUserDetails :: customerDetailResponse :" + customerDetailResponse);

		return resp;
	}

	public AmxApiResponse<CustomerDetailResponse, Object> getUserProfileDetails(CustomerProfileDetailRequest customerProfileDetailRequest)
	{
		AmxApiResponse<CustomerDetailResponse, Object> resp = new AmxApiResponse<CustomerDetailResponse, Object>();
		CustomerProfileDetailResponse customerProfileDetailResponse = new CustomerProfileDetailResponse();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();

		customerProfileDetailModel.setCivilId(customerProfileDetailRequest.getCivilId());
		customerProfileDetailModel.setCustSequenceNumber(customerProfileDetailRequest.getCustSeqNumber());
		customerProfileDetailModel.setCountryId(regSession.getCountryId());
		customerProfileDetailModel.setCompCd(regSession.getCompCd());
		customerProfileDetailModel.setLanguageId(regSession.getLanguageId());
		customerProfileDetailModel.setUserType(regSession.getUserType());

		customerProfileDetailModel = personalDetailsDao.getUserProfileDetails(customerProfileDetailModel);

		if (customerProfileDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorCode());

		return resp;
	}

	public AmxApiResponse<?, Object> getBusiness()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getBusiness());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getNationality()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getNationality());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getGovernorates()
	{

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getGovernorates());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getArea(String gov)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getArea(gov));

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}
}
