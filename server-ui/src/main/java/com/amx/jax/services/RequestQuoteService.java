
package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.dao.RequestQuoteDao;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.DownloadImageModel;
import com.amx.jax.models.ImageModel;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.InsuranceCompanyDetails;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.RequestQuoteInfo;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.models.VehicleDetailsGetModel;
import com.amx.jax.models.VehicleDetailsHeaderModel;
import com.amx.jax.models.VehicleDetailsUpdateModel;
import com.amx.jax.ui.session.UserSession;

@Service
public class RequestQuoteService
{

	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteService.class);

	String TAG = "com.amx.jax.services.RequestQuoteService :- ";

	@Autowired
	public RequestQuoteDao requestQuoteDao;

	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	@Autowired
	private EmailSmsService emailSmsService;

	@Autowired
	MetaData metaData;
	
	@Autowired
	UserSession userSession;


	public AmxApiResponse<RequestQuoteInfo, Object> getIncompleteApplication()
	{
		AmxApiResponse<RequestQuoteInfo, Object> resp = new AmxApiResponse<RequestQuoteInfo, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();

		try
		{
			IncompleteApplModel incompleteApplModel = requestQuoteDao.getIncompleteApplication(userSession.getCivilId() , metaData.getUserType() , userSession.getCustomerSequenceNumber());
			requestQuoteInfo.setAppSeqNumber(incompleteApplModel.getAppSeqNumber());
			requestQuoteModel.setRequestQuoteInfo(requestQuoteInfo);

			if (null == incompleteApplModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(incompleteApplModel.getErrorCode());
			resp.setMessage(incompleteApplModel.getErrorMessage());
			resp.setData(requestQuoteInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getMake()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getMake();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
			resp.setResults(arrayResponseModel.getDataArray());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getModel(String make)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getModel(make);
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getFuleType()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getFuleType();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getPurpose()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getPurpose();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getShape()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getShape();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getColour()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getColour();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getVehicleCondition()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getVehicleCondition();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getMaxVehicleAgeAllowed()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(requestQuoteDao.getMaxVehicleAgeAllowed());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getPolicyDuration()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(requestQuoteDao.getPolicyDuration());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getRequestQuoteDetails()
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();

		try
		{
			IncompleteApplModel incompleteApplModel = requestQuoteDao.getIncompleteApplication(userSession.getCivilId() , metaData.getUserType() , userSession.getCustomerSequenceNumber());
			BigDecimal appSeqNumber = incompleteApplModel.getAppSeqNumber();
			logger.info(TAG + " getRequestQuoteDetails :: appSeqNumber :" + appSeqNumber);

			AmxApiResponse<?, Object> respInfoDetails = getIncompleteApplication();
			if (respInfoDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return respInfoDetails;
			}
			else
			{
				requestQuoteModel.setRequestQuoteInfo((RequestQuoteInfo) respInfoDetails.getData());
			}

			AmxApiResponse<?, Object> respVehicleDetails = getAppVehicleDetails(appSeqNumber);
			if (respVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return respVehicleDetails;
			}
			else if (null == appSeqNumber)
			{
				requestQuoteModel.getRequestQuoteInfo().setApplicationType("NEW");
				requestQuoteModel.setVehicleDetails(null);
			}
			else
			{
				VehicleDetails vehicleDetails = (VehicleDetails) respVehicleDetails.getData();
				requestQuoteModel.getRequestQuoteInfo().setApplicationType(vehicleDetails.getApplicationType());
				requestQuoteModel.setVehicleDetails(vehicleDetails);
			}

			AmxApiResponse<?, Object> respPersonalDetails = getProfileDetails();
			if (respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return respPersonalDetails;
			}
			else
			{
				requestQuoteModel.setPersonalDetails((PersonalDetails) respPersonalDetails.getData());
			}

			AmxApiResponse<?, Object> respImageDetails = getImageDetails(appSeqNumber);
			if (respImageDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return respImageDetails;
			}
			else
			{
				requestQuoteModel.setVehicleImageDetails(respImageDetails.getResults());
			}

			AmxApiResponse<?, Object> insuranceCompDetails = getInsuranceCompanyDetails(appSeqNumber);
			if (insuranceCompDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return insuranceCompDetails;
			}
			else
			{
				requestQuoteModel.setInsuranceCompDetails(insuranceCompDetails.getData());
			}

			resp.setData(requestQuoteModel);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getAppVehicleDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<VehicleDetails, Object> resp = new AmxApiResponse<VehicleDetails, Object>();
		VehicleDetails vehicleDetails = new VehicleDetails();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getAppVehicleDetails(appSeqNumber);

			logger.info(TAG + " getAppVehicleDetails :: arrayResponseModel.getErrorCode() :" + arrayResponseModel.getErrorCode());
			
			if (null == arrayResponseModel.getErrorCode())
			{
				ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = arrayResponseModel.getDataArray();
				
				logger.info(TAG + " getAppVehicleDetails :: vehicleDetailsArray.size() :" + vehicleDetailsArray.size());
				
				if (vehicleDetailsArray.size() >= 1)
				{
					VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
					vehicleDetails.setMakeCode(vehicleDetailsGetModel.getMakeCode());
					vehicleDetails.setSubMakeCode(vehicleDetailsGetModel.getSubMakeCode());
					vehicleDetails.setVehicleTypeDesc(vehicleDetailsGetModel.getVehicleTypeDesc());
					vehicleDetails.setModelYear(vehicleDetailsGetModel.getModelNumber());
					vehicleDetails.setVehicleValue(vehicleDetailsGetModel.getMaxInsmat());
					vehicleDetails.setPolicyDuration(vehicleDetailsGetModel.getPolicyPeriod());
					vehicleDetails.setPurposeCode(vehicleDetailsGetModel.getPurposeCode());
					vehicleDetails.setColourCode(vehicleDetailsGetModel.getColourCode());
					vehicleDetails.setShapeCode(vehicleDetailsGetModel.getShapeCode());
					vehicleDetails.setSeatingCapacity(vehicleDetailsGetModel.getNoPass());
					vehicleDetails.setFuelCode(vehicleDetailsGetModel.getFuelCode());
					vehicleDetails.setChasis(vehicleDetailsGetModel.getChasis());
					vehicleDetails.setVehicleConditionCode(vehicleDetailsGetModel.getVehicleConditionCode());
					vehicleDetails.setKtNumber(vehicleDetailsGetModel.getKtNumber());
					
					if (null != vehicleDetailsGetModel.getApplicationType())
					{
						vehicleDetails.setApplicationType(vehicleDetailsGetModel.getApplicationType().toUpperCase());
					}
					else
					{
						vehicleDetails.setApplicationType(null);
					}
					
					logger.info(TAG + " getAppVehicleDetails :: vehicleDetails :" + vehicleDetails.toString());
				}
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
			resp.setData(vehicleDetails);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> setAppVehicleDetails(BigDecimal appSeqNumber, VehicleDetails vehicleDetails, BigDecimal oldDocNumber)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();

		try
		{

			logger.info(TAG + " setAppVehicleDetails :: oldDocNumber1 :" + oldDocNumber);
			if (null == oldDocNumber)
			{
				ArrayResponseModel arrayResponseModel = requestQuoteDao.getAppVehicleDetails(appSeqNumber);
				if (null == arrayResponseModel.getErrorCode())
				{
					ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = arrayResponseModel.getDataArray();
					logger.info(TAG + " getAppVehicleDetails :: vehicleDetailsArray :" + vehicleDetailsArray.toString());
					if (vehicleDetailsArray.size() >= 1)
					{
						VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
						oldDocNumber = vehicleDetailsGetModel.getOldDocNumber();
						logger.info(TAG + " setAppVehicleDetails :: oldDocNumber2 :" + oldDocNumber);
					}
				}
			}

			VehicleDetailsHeaderModel vehicleDetailsHeaderModel = requestQuoteDao.setVehicleDetailsHeader(appSeqNumber, vehicleDetails, oldDocNumber , userSession.getCivilId() , userSession.getCustomerSequenceNumber() , userSession.getUserSequenceNumber());
			if (null == vehicleDetailsHeaderModel.getErrorCode())
			{
				if (null == appSeqNumber)
				{
					appSeqNumber = vehicleDetailsHeaderModel.getAppSeqNumber();
					logger.info(TAG + " setAppVehicleDetails :: appSeqNumber2 :" + appSeqNumber);
				}
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(vehicleDetailsHeaderModel.getErrorCode());
				resp.setMessage(vehicleDetailsHeaderModel.getErrorMessage());
				return resp;
			}

			VehicleDetailsUpdateModel vehicleDetailsUpdateModel = requestQuoteDao.insUpdateVehicleDetails(appSeqNumber, vehicleDetails , userSession.getCivilId());
			if (null == vehicleDetailsUpdateModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(vehicleDetailsUpdateModel.getErrorCode());
				resp.setMessage(vehicleDetailsUpdateModel.getErrorMessage());
				return resp;
			}

			requestQuoteInfo.setAppSeqNumber(appSeqNumber);
			requestQuoteModel.setRequestQuoteInfo(requestQuoteInfo);
			requestQuoteModel.setPersonalDetails(null);
			requestQuoteModel.setVehicleDetails(vehicleDetails);
			requestQuoteModel.setVehicleImageDetails(null);
			resp.setData(requestQuoteModel);
			resp.setMessageKey(vehicleDetailsUpdateModel.getErrorCode());
			resp.setMessage(vehicleDetailsUpdateModel.getErrorMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<PersonalDetails, Object> getProfileDetails()
	{
		AmxApiResponse<PersonalDetails, Object> resp = new AmxApiResponse<PersonalDetails, Object>();
		PersonalDetails personalDetails = new PersonalDetails();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		customerProfileDetailModel = personalDetailsDao.getProfileDetails(userSession.getCivilId() , metaData.getUserType() , userSession.getCustomerSequenceNumber());

		logger.info(TAG + " getProfileDetails :: customerProfileDetailModel :" + customerProfileDetailModel.toString());
		
		personalDetails.setAreaCode(customerProfileDetailModel.getAreaCode());
		personalDetails.setBusinessCode(customerProfileDetailModel.getBusinessCode());
		personalDetails.setEmail(customerProfileDetailModel.getEmail());
		personalDetails.setEnglishName(customerProfileDetailModel.getEnglishName());
		personalDetails.setGenderCode(customerProfileDetailModel.getGenderCode());
		personalDetails.setGovCode(customerProfileDetailModel.getGovCode());
		personalDetails.setIdExpiryDate(DateFormats.uiFormattedDate(customerProfileDetailModel.getIdExpiryDate()));
		personalDetails.setMobile(customerProfileDetailModel.getMobile());
		personalDetails.setNatyCode(customerProfileDetailModel.getNatyCode());
		personalDetails.setNativeArabicName(customerProfileDetailModel.getNativeArabicName());
		
		logger.info(TAG + " getProfileDetails :: personalDetails :" + personalDetails.toString());
		
		userSession.setCustomerEmailId(customerProfileDetailModel.getEmail());
		
		if (customerProfileDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		resp.setData(personalDetails);
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorCode());
		return resp;
	}

	public AmxApiResponse<?, Object> setProfileDetails(BigDecimal appSeqNumber, PersonalDetails personalDetails)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();
		boolean custSeqNumberAvailable = false;

		if (null != personalDetails.getIdExpiryDate())
		{
			String dateFromDb = personalDetails.getIdExpiryDate();
			if (DateFormats.checkExpiryDate(dateFromDb))
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(MessageKey.KEY_CIVIL_ID_EXPIRED);
				return resp;
			}
		}
		customerProfileDetailModel.setEnglishName(personalDetails.getEnglishName());
		customerProfileDetailModel.setNativeArabicName(personalDetails.getNativeArabicName());
		customerProfileDetailModel.setGenderCode(personalDetails.getGenderCode());
		customerProfileDetailModel.setIdExpiryDate(DateFormats.setDbSqlFormatDate(personalDetails.getIdExpiryDate().toString()));
		customerProfileDetailModel.setBusinessCode(personalDetails.getBusinessCode());
		customerProfileDetailModel.setNatyCode(personalDetails.getNatyCode());
		customerProfileDetailModel.setGovCode(personalDetails.getGovCode());
		customerProfileDetailModel.setAreaCode(personalDetails.getAreaCode());
		customerProfileDetailModel.setMobile(personalDetails.getMobile());
		customerProfileDetailModel.setEmail(personalDetails.getEmail());

		logger.info(TAG + " setProfileDetails :: metaData.getCustomerSequenceNumber() :" + userSession.getCustomerSequenceNumber());
		if (null == userSession.getCustomerSequenceNumber() || userSession.getCustomerSequenceNumber().toString().equals(""))
		{
			custSeqNumberAvailable = false;
		}

		logger.info(TAG + " setProfileDetails :: custSeqNumberAvailable :" + custSeqNumberAvailable);
		customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel , userSession.getCivilId() , metaData.getUserType() , userSession.getCustomerSequenceNumber());
		userSession.setCustomerSequenceNumber(customerProfileDetailModel.getCustSequenceNumber());
		
		if (!custSeqNumberAvailable)
		{
			logger.info(TAG + " setProfileDetails :: customerProfileDetailModel.getCustSequenceNumber() :" + customerProfileDetailModel.getCustSequenceNumber());
			logger.info(TAG + " setProfileDetails :: appSeqNumber :" + appSeqNumber);
			AmxApiResponse<ResponseInfo, Object> updateCustSeqNum = updateCustomerSequenceNumber(customerProfileDetailModel.getCustSequenceNumber(), appSeqNumber , userSession.getCivilId());
			if (updateCustSeqNum.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return updateCustSeqNum;
			}
		}

		if (null == customerProfileDetailModel.getErrorCode())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		requestQuoteInfo.setAppSeqNumber(appSeqNumber);
		requestQuoteModel.setRequestQuoteInfo(requestQuoteInfo);
		requestQuoteModel.setPersonalDetails(personalDetails);
		requestQuoteModel.setVehicleDetails(null);
		requestQuoteModel.setVehicleImageDetails(null);

		resp.setData(requestQuoteModel);
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorMessage());
		return resp;
	}

	public AmxApiResponse<?, Object> getImageMetaData()
	{
		AmxApiResponse<?, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getImageMetaData();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setMeta(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorCode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getImageDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<?, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getImageDetails(appSeqNumber);
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorCode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public DownloadImageModel downloadVehicleImage(BigDecimal docSeqNumber)
	{ 
		try
		{
			return requestQuoteDao.downloadVehicleImage(docSeqNumber);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public AmxApiResponse<?, Object> uploadVehicleImage(MultipartFile file, BigDecimal appSeqNumber, String docTypeCode, BigDecimal docSeqNumber)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();
		try
		{
			ImageModel imageModel = requestQuoteDao.uploadVehicleImage(file, appSeqNumber, docTypeCode, docSeqNumber , userSession.getCivilId());
			if (null == imageModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				AmxApiResponse<?, Object> respImageDetails = getImageDetails(appSeqNumber);
				if (respImageDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return respImageDetails;
				}
				else
				{
					requestQuoteModel.setVehicleImageDetails(respImageDetails.getResults());
				}
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}

			requestQuoteInfo.setAppSeqNumber(appSeqNumber);
			requestQuoteModel.setRequestQuoteInfo(requestQuoteInfo);
			requestQuoteModel.setPersonalDetails(null);
			requestQuoteModel.setVehicleDetails(null);

			resp.setData(requestQuoteModel);
			resp.setMessageKey(imageModel.getErrorCode());
			resp.setMessage(imageModel.getErrorCode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getInsuranceCompanyDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getInsuranceCompanyDetails(appSeqNumber);

			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setData(arrayResponseModel.getDataArray());
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<ResponseInfo, Object> updateCustomerSequenceNumber(BigDecimal custSeqNumber, BigDecimal appSeqNumber , String civilId)
	{
		CustomerProfileDetailModel customerProfileDetailModel = requestQuoteDao.updateCustomerSequenceNumber(custSeqNumber, appSeqNumber , civilId);
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		if (null == customerProfileDetailModel.getErrorCode())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> submitRequestQuote(BigDecimal appSeqNumber, BigDecimal insuranceCompCode)
	{
		String makeDesc = "";
		String subMakeDesc = "";
		String urlDetails = "https://www.javatpoint.com/spring-security-login-logout";
		
		logger.info(TAG + " submitRequestQuote :: appSeqNumber :" + appSeqNumber);
		logger.info(TAG + " submitRequestQuote :: insuranceCompCode :" + insuranceCompCode);
		
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		
		AmxApiResponse<?, Object> updateInsuranceProvider = updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
		if (updateInsuranceProvider.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return updateInsuranceProvider;
		}
		
		ArrayResponseModel arrayResponseModel = requestQuoteDao.submitRequestQuote(appSeqNumber , userSession.getCivilId());
		if (null == arrayResponseModel.getErrorCode())
		{
			logger.info(TAG + " submitRequestQuote :: arrayResponseModel.getErrorCode() :" + arrayResponseModel.getErrorCode());
			
			ArrayResponseModel getVehicleDetailsArray = requestQuoteDao.getAppVehicleDetails(appSeqNumber);
			if (null == getVehicleDetailsArray.getErrorCode())
			{
				logger.info(TAG + " submitRequestQuote :: getVehicleDetailsArray.getErrorCode() :" + getVehicleDetailsArray.getErrorCode());
			
				ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = getVehicleDetailsArray.getDataArray();
				if (vehicleDetailsArray.size() >= 1)
				{
					VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
					makeDesc = vehicleDetailsGetModel.getMakeDesc();
					subMakeDesc = vehicleDetailsGetModel.getSubMakeDesc();
					
					logger.info(TAG + " submitRequestQuote :: makeDesc:" + makeDesc);
					logger.info(TAG + " submitRequestQuote :: subMakeDesc :" + subMakeDesc);
				}
				resp.setStatusKey(ApiConstants.SUCCESS);
			}

			logger.info(TAG + " submitRequestQuote :: makeDesc :" + makeDesc);
			logger.info(TAG + " submitRequestQuote :: subMakeDesc :" + subMakeDesc);
			logger.info(TAG + " submitRequestQuote :: urlDetails :" + urlDetails);
			
			emailSmsService.emailToCustomerOnCompilitionRequestQuote(makeDesc,subMakeDesc,appSeqNumber);
			emailSmsService.emailToAmibOnCompilitionRequestQuote(makeDesc,subMakeDesc,appSeqNumber);
			resp.setStatusKey(ApiConstants.SUCCESS);
			
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		resp.setMessageKey(arrayResponseModel.getErrorCode());
		resp.setMessage(arrayResponseModel.getErrorMessage());

		return resp;
	}

	public AmxApiResponse<?, Object> updateInsuranceProvider(BigDecimal appSeqNumber, BigDecimal insuranceCompCode , String civilId)
	{
		logger.info(TAG + " updateInsuranceProvider :: appSeqNumber      :" + appSeqNumber);
		logger.info(TAG + " updateInsuranceProvider :: insuranceCompCode :" + insuranceCompCode);
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();

		AmxApiResponse<?, Object> getInsuranceCompanyDetails = getInsuranceCompanyDetails(appSeqNumber);
		if (getInsuranceCompanyDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return getInsuranceCompanyDetails;
		}
		else
		{
			ArrayList<InsuranceCompanyDetails> dataArray = (ArrayList<InsuranceCompanyDetails>) getInsuranceCompanyDetails.getData();
			for (int i = 0; i < dataArray.size(); i++)
			{
				String status = "";
				InsuranceCompanyDetails insuranceCompanyDetails = dataArray.get(i);

				logger.info(TAG + " updateInsuranceProvider :: getCompanyCode     :" + insuranceCompanyDetails.getCompanyCode());
				logger.info(TAG + " updateInsuranceProvider :: insuranceCompCodee :" + insuranceCompCode);
				
				if(null != insuranceCompCode && null != insuranceCompanyDetails.getCompanyCode() && insuranceCompanyDetails.getCompanyCode().toString().equalsIgnoreCase(HardCodedValues.COMPANY_CODE))
				{
					logger.info(TAG + " updateInsuranceProvider :: AMIB :: getCompanyCode     :" + insuranceCompanyDetails.getCompanyCode());
					logger.info(TAG + " updateInsuranceProvider :: AMIB :: insuranceCompCodee :" + insuranceCompCode);
				}
				else
				{
					logger.info(TAG + " updateInsuranceProvider :: NON AMIB :: getCompanyCode     :" + insuranceCompanyDetails.getCompanyCode());
					logger.info(TAG + " updateInsuranceProvider :: NON AMIB :: insuranceCompCodee :" + insuranceCompCode);
					
					if (null != insuranceCompCode && null != insuranceCompanyDetails.getCompanyCode() && insuranceCompCode.equals(insuranceCompanyDetails.getCompanyCode()))
					{
						logger.info(TAG + " submitRequestQuote :: EqualsCheck :" + insuranceCompCode.equals(insuranceCompanyDetails.getCompanyCode()));
						status = "Y";
					}
					else
					{
						status = "N";
					}
					
					ArrayResponseModel updateInsuranceProvider = requestQuoteDao.updateInsuranceProvider(appSeqNumber, insuranceCompanyDetails.getCompanyCode(), status , civilId);
					if (updateInsuranceProvider.getErrorCode() != null)
					{
						if (null == updateInsuranceProvider.getErrorCode())
						{
							resp.setStatusKey(ApiConstants.SUCCESS);
						}
						else
						{
							resp.setStatusKey(ApiConstants.FAILURE);
						}
						resp.setMessageKey(updateInsuranceProvider.getErrorCode());
						resp.setMessage(updateInsuranceProvider.getErrorMessage());
					}
				}
			}
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getRenewPolicyDetails(BigDecimal oldDocNumber)
	{
		BigDecimal insuranceCompCode = null;
		BigDecimal appSeqNumber = null;
		logger.info(TAG + " getRenewPolicyDetails :: oldDocNumber :" + oldDocNumber);

		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		try
		{
			AmxApiResponse<?, Object> getVehicleDetails = getRenewPolicyVehicleDetails(oldDocNumber);
			if (getVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return getVehicleDetails;
			}
			else
			{
				if (null != getVehicleDetails.getMeta())
				{
					insuranceCompCode = (BigDecimal) getVehicleDetails.getMeta();
					logger.info(TAG + " getRenewPolicyDetails :: insuranceCompCode :" + insuranceCompCode);
				}

				AmxApiResponse<?, Object> submitVehicleDetails = setAppVehicleDetails(appSeqNumber, (VehicleDetails) getVehicleDetails.getData(), oldDocNumber);
				if (submitVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return submitVehicleDetails;
				}
				else
				{
					RequestQuoteModel requestQuoteModel = (RequestQuoteModel) submitVehicleDetails.getData();
					RequestQuoteInfo requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
					appSeqNumber = requestQuoteInfo.getAppSeqNumber();
					logger.info(TAG + " getRenewPolicyDetails :: appSeqNumber1 :" + appSeqNumber);
				}

				AmxApiResponse<?, Object> respPersonalDetails = getProfileDetails();
				if (respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return respPersonalDetails;
				}
				else
				{
					AmxApiResponse<?, Object> setPersonalDetails = setProfileDetails(appSeqNumber, (PersonalDetails) respPersonalDetails.getData());
					if (setPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						return setPersonalDetails;
					}
				}

				AmxApiResponse<?, Object> updateInsuranceProvider = updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
				if (updateInsuranceProvider.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return updateInsuranceProvider;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setMessage(e.toString());
			return resp;
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getRenewPolicyVehicleDetails(BigDecimal appDocNumberDet)
	{
		AmxApiResponse<VehicleDetails, Object> resp = new AmxApiResponse<VehicleDetails, Object>();
		VehicleDetails vehicleDetails = new VehicleDetails();
		BigDecimal companyCode = null;
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getRenewPolicyVehicleDetails(appDocNumberDet);
			if (null == arrayResponseModel.getErrorCode())
			{
				if (null != arrayResponseModel && null != arrayResponseModel.getDataArray() && arrayResponseModel.getDataArray().size() > 0)
				{
					vehicleDetails = (VehicleDetails) arrayResponseModel.getDataArray().get(0);
				}

				if (null != arrayResponseModel.getData())
				{
					companyCode = new BigDecimal(arrayResponseModel.getData());
				}

				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
			resp.setData(vehicleDetails);
			resp.setMeta(companyCode);
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
