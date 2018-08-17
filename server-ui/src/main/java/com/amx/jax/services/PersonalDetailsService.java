
package com.amx.jax.services;

import java.math.BigDecimal;
import java.sql.Date;

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
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.models.CustomerProfileUpdateResponse;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;

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

	@Autowired
	MetaData userSession;

	public AmxApiResponse<CustomerProfileDetailResponse, Object> getProfileDetails()
	{
		AmxApiResponse<CustomerProfileDetailResponse, Object> resp = new AmxApiResponse<CustomerProfileDetailResponse, Object>();
		CustomerProfileDetailResponse customerProfileDetailResponse = new CustomerProfileDetailResponse();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();

		customerProfileDetailModel = personalDetailsDao.getProfileDetails();

		customerProfileDetailResponse.setAreaCode(customerProfileDetailModel.getAreaCode());
		customerProfileDetailResponse.setAreaDesc(customerProfileDetailModel.getAreaDesc());
		customerProfileDetailResponse.setBusinessCode(customerProfileDetailModel.getBusinessCode());
		customerProfileDetailResponse.setBusinessDesc(customerProfileDetailModel.getBusinessDesc());
		customerProfileDetailResponse.setEmail(customerProfileDetailModel.getEmail());
		customerProfileDetailResponse.setEnglishName(customerProfileDetailModel.getEnglishName());
		customerProfileDetailResponse.setGenderCode(customerProfileDetailModel.getGenderCode());
		customerProfileDetailResponse.setGenderDesc(customerProfileDetailModel.getGenderDesc());
		customerProfileDetailResponse.setGovCode(customerProfileDetailModel.getGovCode());
		customerProfileDetailResponse.setGovDesc(customerProfileDetailModel.getGovDesc());
		customerProfileDetailResponse.setIdExpiryDate(customerProfileDetailModel.getIdExpiryDate());
		customerProfileDetailResponse.setLanguageId(customerProfileDetailModel.getLanguageId());
		customerProfileDetailResponse.setMobile(customerProfileDetailModel.getMobile());
		customerProfileDetailResponse.setNatyCode(customerProfileDetailModel.getNatyCode());
		customerProfileDetailResponse.setNatyDesc(customerProfileDetailModel.getNatyDesc());
		customerProfileDetailResponse.setNativeArabicName(customerProfileDetailModel.getNativeArabicName());

		logger.info(TAG + " getProfileDetails :: customerProfileDetailResponse :" + customerProfileDetailResponse);

		if (customerProfileDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setData(customerProfileDetailResponse);
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorCode());

		return resp;
	}

	public AmxApiResponse<CustomerProfileUpdateResponse, Object> updateProfileDetails(CustomerProfileUpdateRequest customerProfileUpdateRequest)
	{
		AmxApiResponse<CustomerProfileUpdateResponse, Object> resp = new AmxApiResponse<CustomerProfileUpdateResponse, Object>();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		CustomerProfileUpdateResponse customerProfileUpdateResponse = new CustomerProfileUpdateResponse();

		customerProfileDetailModel.setCustSequenceNumber(customerProfileUpdateRequest.getCustomerSequenceNumber());
		customerProfileDetailModel.setEnglishName(customerProfileUpdateRequest.getEnglishName());
		customerProfileDetailModel.setNativeArabicName(customerProfileUpdateRequest.getNativeArabicName());
		customerProfileDetailModel.setGenderCode(customerProfileUpdateRequest.getGenderCode());
		customerProfileDetailModel.setIdExpiryDate(customerProfileUpdateRequest.getIdExpiryDate());
		customerProfileDetailModel.setBusinessCode(customerProfileUpdateRequest.getBusinessCode());
		customerProfileDetailModel.setNatyCode(customerProfileUpdateRequest.getNatyCode());
		customerProfileDetailModel.setGovCode(customerProfileUpdateRequest.getGovCode());
		customerProfileDetailModel.setAreaCode(customerProfileUpdateRequest.getAreaCode());
		customerProfileDetailModel.setMobile(customerProfileUpdateRequest.getMobile());
		customerProfileDetailModel.setEmail(customerProfileUpdateRequest.getEmail());

		customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel);

		customerProfileUpdateResponse.setStatus(customerProfileDetailModel.getStatus());
		customerProfileUpdateResponse.setErrorCode(customerProfileDetailModel.getErrorCode());
		customerProfileUpdateResponse.setErrorMessage(customerProfileDetailModel.getErrorMessage());

		logger.info(TAG + " updateProfileDetails :: customerProfileDetailModel :" + customerProfileDetailModel);

		if (customerProfileUpdateResponse.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setMessageKey(customerProfileUpdateResponse.getErrorCode());
		resp.setMessage(customerProfileUpdateResponse.getErrorCode());

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

	public AmxApiResponse<?, Object> getGender()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getGender());

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
