
package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.dao.RequestQuoteDao;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.DownloadImageModel;
import com.amx.jax.models.ImageModel;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.InsuranceCompanyDetails;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.RequestQuoteInfo;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.models.VehicleDetailsGetModel;
import com.amx.jax.models.VehicleDetailsHeaderModel;
import com.amx.jax.models.VehicleDetailsUpdateModel;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.utility.CodeAvaibility;

@Service
public class RequestQuoteService
{

	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteService.class);

	@Autowired
	public RequestQuoteDao requestQuoteDao;

	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	@Autowired
	private EmailSmsService emailSmsService;

	@Autowired
	UserSession userSession;


	public AmxApiResponse<RequestQuoteInfo, Object> getIncompleteApplication()
	{
		AmxApiResponse<RequestQuoteInfo, Object> resp = new AmxApiResponse<RequestQuoteInfo, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();

		try
		{
			IncompleteApplModel incompleteApplModel = requestQuoteDao.getIncompleteApplication(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());
			requestQuoteInfo.setAppSeqNumber(incompleteApplModel.getAppSeqNumber());
			requestQuoteModel.setRequestQuoteInfo(requestQuoteInfo);

			if (null == incompleteApplModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(incompleteApplModel.getErrorCode());
			}
			resp.setMessageKey(incompleteApplModel.getErrorCode());
			resp.setMessage(incompleteApplModel.getErrorMessage());
			resp.setData(requestQuoteInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getMake()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getMake(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
			resp.setResults(arrayResponseModel.getDataArray());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getModel(String make)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getModel(make, userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getFuleType()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getFuleType(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getPurpose()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getPurpose(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getShape()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getShape(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getColour()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getColour(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getVehicleCondition()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getVehicleCondition(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getMaxVehicleAgeAllowed()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(requestQuoteDao.getMaxVehicleAgeAllowed());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getPolicyDuration()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(requestQuoteDao.getPolicyDuration());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getRequestQuoteDetails()
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();

		try
		{
			IncompleteApplModel incompleteApplModel = requestQuoteDao.getIncompleteApplication(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());
			BigDecimal appSeqNumber = incompleteApplModel.getAppSeqNumber();
			
			AmxApiResponse<?, Object> respInfoDetails = getIncompleteApplication();
			if (!respInfoDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return respInfoDetails;
			}
			else
			{
				requestQuoteModel.setRequestQuoteInfo((RequestQuoteInfo) respInfoDetails.getData());
			}

			AmxApiResponse<?, Object> respVehicleDetails = getAppVehicleDetails(appSeqNumber);
			if (!respVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
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
			if (!respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return respPersonalDetails;
			}
			else
			{
				requestQuoteModel.setPersonalDetails((PersonalDetails) respPersonalDetails.getData());
			}

			AmxApiResponse<?, Object> respImageDetails = getImageDetails(appSeqNumber);
			if (!respImageDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return respImageDetails;
			}
			else
			{
				requestQuoteModel.setVehicleImageDetails(respImageDetails.getResults());
			}

			AmxApiResponse<?, Object> insuranceCompDetails = getInsuranceCompanyDetails(appSeqNumber);
			if (!insuranceCompDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
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
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getAppVehicleDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<VehicleDetails, Object> resp = new AmxApiResponse<VehicleDetails, Object>();
		VehicleDetails vehicleDetails = new VehicleDetails();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getAppVehicleDetails(appSeqNumber, userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = arrayResponseModel.getDataArray();
				if (vehicleDetailsArray.size() >= 1)
				{
					VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
					vehicleDetails.setMakeCode(vehicleDetailsGetModel.getMakeCode());
					vehicleDetails.setSubMakeCode(vehicleDetailsGetModel.getSubMakeCode());
					vehicleDetails.setVehicleTypeDesc(vehicleDetailsGetModel.getVehicleTypeDesc());
					vehicleDetails.setModelYear(vehicleDetailsGetModel.getModelNumber());
					vehicleDetails.setVehicleValue(vehicleDetailsGetModel.getMaxInsmat());
					vehicleDetails.setPolicyDuration(vehicleDetailsGetModel.getPolicyPeriod());
					
					vehicleDetails.setPurposeCode(CodeAvaibility.purposeCodeCheck(vehicleDetailsGetModel.getPurposeCode() , getPurpose().getResults()));
					
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
				}
				
				logger.info("RequestQuoteService :: getAppVehicleDetails :: vehicleDetails :" + vehicleDetails.toString());
				
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
			resp.setData(vehicleDetails);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
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

			if (null == oldDocNumber)
			{
				ArrayResponseModel arrayResponseModel = requestQuoteDao.getAppVehicleDetails(appSeqNumber, userSession.getLanguageId());
				if (null == arrayResponseModel.getErrorCode())
				{
					ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = arrayResponseModel.getDataArray();
					if (vehicleDetailsArray.size() >= 1)
					{
						VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
						oldDocNumber = vehicleDetailsGetModel.getOldDocNumber();
					}
				}
			}

			VehicleDetailsHeaderModel vehicleDetailsHeaderModel = requestQuoteDao.setVehicleDetailsHeader(appSeqNumber, vehicleDetails, oldDocNumber , userSession.getCivilId() , userSession.getCustomerSequenceNumber() , userSession.getUserSequenceNumber());
			if (null == vehicleDetailsHeaderModel.getErrorCode())
			{
				if (null == appSeqNumber)
				{
					appSeqNumber = vehicleDetailsHeaderModel.getAppSeqNumber();
				}
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(vehicleDetailsHeaderModel.getErrorCode());
				resp.setMessageKey(vehicleDetailsHeaderModel.getErrorCode());
				resp.setMessage(vehicleDetailsHeaderModel.getErrorMessage());
				return resp;
			}

			VehicleDetailsUpdateModel vehicleDetailsUpdateModel = requestQuoteDao.insUpdateVehicleDetails(appSeqNumber, vehicleDetails , userSession.getCivilId());
			if (null == vehicleDetailsUpdateModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}	
			else
			{
				resp.setStatusKey(vehicleDetailsUpdateModel.getErrorCode());
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
		}
		return resp;
	}

	public AmxApiResponse<PersonalDetails, Object> getProfileDetails()
	{
		AmxApiResponse<PersonalDetails, Object> resp = new AmxApiResponse<PersonalDetails, Object>();
		PersonalDetails personalDetails = new PersonalDetails();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		customerProfileDetailModel = personalDetailsDao.getProfileDetails(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber(), userSession.getLanguageId());

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
		
		userSession.setCustomerEmailId(customerProfileDetailModel.getEmail());

		if (customerProfileDetailModel.getErrorCode() == null)
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		else
		{
			resp.setStatusKey(customerProfileDetailModel.getErrorCode());
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
				resp.setStatusKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
				resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
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

		if (null == userSession.getCustomerSequenceNumber() || userSession.getCustomerSequenceNumber().toString().equals(""))
		{
			custSeqNumberAvailable = false;
		}

		customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel , userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
		userSession.setCustomerSequenceNumber(customerProfileDetailModel.getCustSequenceNumber());
		
		if (!custSeqNumberAvailable)
		{
			AmxApiResponse<ResponseInfo, Object> updateCustSeqNum = updateCustomerSequenceNumber(customerProfileDetailModel.getCustSequenceNumber(), appSeqNumber , userSession.getCivilId());
			if (!updateCustSeqNum.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return updateCustSeqNum;
			}
		}

		if (null == customerProfileDetailModel.getErrorCode())
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		else
		{
			resp.setStatusKey(customerProfileDetailModel.getErrorCode());
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
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getImageMetaData(userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setMeta(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorCode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getImageDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<?, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getImageDetails(appSeqNumber, userSession.getLanguageId());
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setResults(arrayResponseModel.getDataArray());
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorCode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
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
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				AmxApiResponse<?, Object> respImageDetails = getImageDetails(appSeqNumber);
				if (!respImageDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
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
				resp.setStatusKey(imageModel.getErrorCode());
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
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getInsuranceCompanyDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getInsuranceCompanyDetails(appSeqNumber, userSession.getLanguageId());

			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setData(arrayResponseModel.getDataArray());
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}
	
	public AmxApiResponse<ResponseInfo, Object> updateCustomerSequenceNumber(BigDecimal custSeqNumber, BigDecimal appSeqNumber , String civilId)
	{
		CustomerProfileDetailModel customerProfileDetailModel = requestQuoteDao.updateCustomerSequenceNumber(custSeqNumber, appSeqNumber , civilId);
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		if (null == customerProfileDetailModel.getErrorCode())
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		else
		{
			resp.setStatusKey(customerProfileDetailModel.getErrorCode());
			resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> submitRequestQuote(BigDecimal appSeqNumber, BigDecimal insuranceCompCode)
	{
		String makeDesc = "";
		String subMakeDesc = "";
		
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		
		AmxApiResponse<?, Object> updateInsuranceProvider = updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
		if (!updateInsuranceProvider.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return updateInsuranceProvider;
		}
		
		ArrayResponseModel arrayResponseModel = requestQuoteDao.submitRequestQuote(appSeqNumber , userSession.getCivilId());
		if (null == arrayResponseModel.getErrorCode())
		{
			ArrayResponseModel getVehicleDetailsArray = requestQuoteDao.getAppVehicleDetails(appSeqNumber, userSession.getLanguageId());
			if (null == getVehicleDetailsArray.getErrorCode())
			{
			
				ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = getVehicleDetailsArray.getDataArray();
				if (vehicleDetailsArray.size() >= 1)
				{
					VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
					makeDesc = vehicleDetailsGetModel.getMakeDesc();
					subMakeDesc = vehicleDetailsGetModel.getSubMakeDesc();
				}
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			
			emailSmsService.emailToCustomerOnCompilitionRequestQuote(makeDesc,subMakeDesc,appSeqNumber);
			emailSmsService.emailToAmibOnCompilitionRequestQuote(makeDesc,subMakeDesc,appSeqNumber);
			
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
			}
		resp.setMessageKey(arrayResponseModel.getErrorCode());
		resp.setMessage(arrayResponseModel.getErrorMessage());

		return resp;
	}

	public AmxApiResponse<?, Object> updateInsuranceProvider(BigDecimal appSeqNumber, BigDecimal insuranceCompCode , String civilId)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();

		AmxApiResponse<?, Object> getInsuranceCompanyDetails = getInsuranceCompanyDetails(appSeqNumber);
		if (!getInsuranceCompanyDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
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

				if(null != insuranceCompCode && null != insuranceCompanyDetails.getCompanyCode() && insuranceCompanyDetails.getCompanyCode().toString().equalsIgnoreCase(HardCodedValues.COMPANY_CODE))
				{
					logger.info(" updateInsuranceProvider :: AMIB :: getCompanyCode     :" + insuranceCompanyDetails.getCompanyCode());
					logger.info(" updateInsuranceProvider :: AMIB :: insuranceCompCodee :" + insuranceCompCode);
				}
				else
				{
					if (null != insuranceCompCode && null != insuranceCompanyDetails.getCompanyCode() && insuranceCompCode.equals(insuranceCompanyDetails.getCompanyCode()))
					{
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
							resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
						}
						else
						{
							resp.setStatusKey(updateInsuranceProvider.getErrorCode());
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
		logger.info(" getRenewPolicyDetails :: oldDocNumber :" + oldDocNumber);
	
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		try
		{
			AmxApiResponse<?, Object> getVehicleDetails = getRenewPolicyVehicleDetails(oldDocNumber);
			if (!getVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return getVehicleDetails;
			}
			else
			{
				if (null != getVehicleDetails.getMeta())
				{
					insuranceCompCode = (BigDecimal) getVehicleDetails.getMeta();
					logger.info(" getRenewPolicyDetails :: insuranceCompCode :" + insuranceCompCode);
				}
	
				AmxApiResponse<?, Object> submitVehicleDetails = setAppVehicleDetails(appSeqNumber, (VehicleDetails) getVehicleDetails.getData(), oldDocNumber);
				if (!submitVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					return submitVehicleDetails;
				}
				else
				{
					RequestQuoteModel requestQuoteModel = (RequestQuoteModel) submitVehicleDetails.getData();
					RequestQuoteInfo requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
					appSeqNumber = requestQuoteInfo.getAppSeqNumber();
					logger.info(" getRenewPolicyDetails :: appSeqNumber1 :" + appSeqNumber);
				}
	
				AmxApiResponse<?, Object> respPersonalDetails = getProfileDetails();
				if (!respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					return respPersonalDetails;
				}
				else
				{
					AmxApiResponse<?, Object> setPersonalDetails = setProfileDetails(appSeqNumber, (PersonalDetails) respPersonalDetails.getData());
					if (!setPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
					{
						return setPersonalDetails;
					}
				}

				AmxApiResponse<?, Object> updateInsuranceProvider = updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
				if (!updateInsuranceProvider.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
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
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getRenewPolicyVehicleDetails(appDocNumberDet, userSession.getLanguageId());
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

				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
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
		}
		return resp;
	}
}
