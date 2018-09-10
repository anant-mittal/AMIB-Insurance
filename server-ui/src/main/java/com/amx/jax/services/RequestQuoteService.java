
package com.amx.jax.services;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.dao.RequestQuoteDao;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.models.CustomerProfileUpdateResponse;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.ImageMandatoryResponse;
import com.amx.jax.models.ImageUploadStatusResponse;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.RequestQuoteInfo;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.models.Validate;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.models.VehicleDetailsGetModel;
import com.amx.jax.models.VehicleDetailsHeaderModel;
import com.amx.jax.models.VehicleDetailsUpdateModel;
import com.insurance.services.OtpService;

@Service
public class RequestQuoteService
{

	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteService.class);

	String TAG = "com.amx.jax.vehicledetails.service.VehicleDetailsService :- ";

	@Autowired
	public RequestQuoteDao requestQuoteDao;

	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private OtpService otpService;

	public AmxApiResponse<RequestQuoteModel, Object> getIncompleteApplication()
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		RequestQuoteModel requestQuoteModel = new RequestQuoteModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();

		try
		{
			IncompleteApplModel incompleteApplModel = requestQuoteDao.getIncompleteApplication();
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

	public AmxApiResponse<?, Object> getFuleType()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getFuleType();
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

	public AmxApiResponse<?, Object> getPurpose()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getPurpose();
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

	public AmxApiResponse<?, Object> getShape()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getShape();
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

	public AmxApiResponse<?, Object> getColour()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getColour();
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

	public AmxApiResponse<?, Object> getVehicleCondition()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getVehicleCondition();
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

	public AmxApiResponse<?, Object> getAppVehicleDetails(RequestQuoteModel requestQuoteModel)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		VehicleDetails vehicleDetails = new VehicleDetails();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getAppVehicleDetails(requestQuoteModel);

			if (null == arrayResponseModel.getErrorCode())
			{
				ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = arrayResponseModel.getDataArray();
				logger.info(TAG + " getAppVehicleDetails :: vehicleDetailsArray :" + vehicleDetailsArray.toString());
				if (vehicleDetailsArray.size() >= 1)
				{
					VehicleDetailsGetModel vehicleDetailsGetModel = vehicleDetailsArray.get(0);
					logger.info(TAG + " getAppVehicleDetails :: vehicleDetailsGetModel :" + vehicleDetailsGetModel.toString());
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
				}
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			requestQuoteModel.setVehicleDetails(vehicleDetails);
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorMessage());
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

	public AmxApiResponse<?, Object> setAppVehicleDetails(RequestQuoteModel requestQuoteModel)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		try
		{
			VehicleDetailsHeaderModel vehicleDetailsHeaderModel = requestQuoteDao.setVehicleDetailsHeader(requestQuoteModel);
			if (null == vehicleDetailsHeaderModel.getErrorCode())
			{
				RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();
				requestQuoteInfo.setAppSeqNumber(vehicleDetailsHeaderModel.getAppSeqNumber());
				logger.info(TAG + " setAppVehicleDetails :: getAppSeqNumber :" + vehicleDetailsHeaderModel.getAppSeqNumber());
				requestQuoteModel.setRequestQuoteInfo(requestQuoteInfo);
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setData(requestQuoteModel);
				resp.setMessageKey(vehicleDetailsHeaderModel.getErrorCode());
				resp.setMessage(vehicleDetailsHeaderModel.getErrorMessage());
				return resp;
			}

			logger.info(TAG + " setAppVehicleDetails :: requestQuoteModel :" + requestQuoteModel.toString());
			VehicleDetailsUpdateModel vehicleDetailsUpdateModel = requestQuoteDao.insUpdateVehicleDetails(requestQuoteModel);
			if (null == vehicleDetailsUpdateModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
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

	public AmxApiResponse<RequestQuoteModel, Object> getProfileDetails(RequestQuoteModel requestQuoteModel)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();

		PersonalDetails personalDetails = new PersonalDetails();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		customerProfileDetailModel = personalDetailsDao.getProfileDetails();

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
		requestQuoteModel.setPersonalDetails(personalDetails);

		if (customerProfileDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		resp.setData(requestQuoteModel);
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorCode());
		return resp;
	}

	public AmxApiResponse<?, Object> setProfileDetails(String mOtp, String eOtp, RequestQuoteModel requestQuoteModel)
	{
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		CustomerProfileDetailModel customerProfileDetailModelCheck = new CustomerProfileDetailModel();
		PersonalDetails personalDetails = requestQuoteModel.getPersonalDetails();

		customerProfileDetailModelCheck = personalDetailsDao.getProfileDetails();

		logger.info(TAG + " updateProfileDetails :: getMobile 1 :" + customerProfileDetailModelCheck.getMobile());
		logger.info(TAG + " updateProfileDetails :: getEmail  2 :" + customerProfileDetailModelCheck.getEmail());
		logger.info(TAG + " updateProfileDetails :: getMobile 3 :" + personalDetails.getMobile());
		logger.info(TAG + " updateProfileDetails :: getEmail  4 :" + personalDetails.getEmail());

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

		if (null != customerProfileDetailModelCheck.getMobile() && null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getMobile().equals(personalDetails.getMobile()) && !customerProfileDetailModelCheck.getEmail().equals(personalDetails.getEmail()))

		{
			logger.info(TAG + " updateProfileDetails :: Both Chnaged");

			AmxApiResponse<Validate, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(personalDetails.getMobile());
			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<Validate, Object> validateEmailID = customerRegistrationService.isValidEmailId(personalDetails.getEmail());
			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			AmxApiResponse<Validate, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(personalDetails.getMobile());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}

			AmxApiResponse<Validate, Object> emailIdExists = customerRegistrationService.isEmailIdExist(personalDetails.getEmail());
			if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				emailIdExists.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
				emailIdExists.setStatusKey(ApiConstants.FAILURE);
				return emailIdExists;
			}

			AmxApiResponse<?, Object> validateDOTP = otpService.validateDOTP(eOtp, mOtp, personalDetails.getEmail(), personalDetails.getMobile());
			if (null != validateDOTP)
			{
				return validateDOTP;
			}
		}
		else if (null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getEmail().equals(personalDetails.getEmail()))
		{
			AmxApiResponse<Validate, Object> validateEmailID = customerRegistrationService.isValidEmailId(personalDetails.getEmail());
			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			AmxApiResponse<Validate, Object> emailIdExists = customerRegistrationService.isEmailIdExist(personalDetails.getEmail());
			if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				emailIdExists.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
				emailIdExists.setStatusKey(ApiConstants.FAILURE);
				return emailIdExists;
			}

			logger.info(TAG + " updateProfileDetails :: Email " + customerProfileDetailModelCheck.getEmail());
			AmxApiResponse<?, Object> validateEOTP = otpService.validateEOTP(eOtp, customerProfileDetailModelCheck.getEmail());
			if (null != validateEOTP)
			{
				return validateEOTP;
			}

		}
		else if (null != customerProfileDetailModelCheck.getMobile() && !customerProfileDetailModelCheck.getMobile().equals(personalDetails.getMobile()))
		{
			logger.info(TAG + " updateProfileDetails :: Mobile Chnaged");

			AmxApiResponse<Validate, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(personalDetails.getMobile());
			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<Validate, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(personalDetails.getMobile());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}

			AmxApiResponse<?, Object> validateMOTP = otpService.validateMOTP(mOtp, customerProfileDetailModelCheck.getMobile());
			if (null != validateMOTP)
			{
				return validateMOTP;
			}
		}

		customerProfileDetailModel.setEnglishName(personalDetails.getEnglishName());
		customerProfileDetailModel.setNativeArabicName(personalDetails.getNativeArabicName());
		customerProfileDetailModel.setGenderCode(personalDetails.getGenderCode());
		customerProfileDetailModel.setIdExpiryDate(DateFormats.setExpiryDateToDb(personalDetails.getIdExpiryDate().toString()));
		customerProfileDetailModel.setBusinessCode(personalDetails.getBusinessCode());
		customerProfileDetailModel.setNatyCode(personalDetails.getNatyCode());
		customerProfileDetailModel.setGovCode(personalDetails.getGovCode());
		customerProfileDetailModel.setAreaCode(personalDetails.getAreaCode());
		customerProfileDetailModel.setMobile(personalDetails.getMobile());
		customerProfileDetailModel.setEmail(personalDetails.getEmail());

		customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel);

		if (null == customerProfileDetailModel.getErrorCode())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		resp.setData(requestQuoteModel);
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorMessage());
		return resp;
	}

	public AmxApiResponse<ImageUploadStatusResponse, Object> checkIfImageAlreadyUploaded(String docType)
	{
		AmxApiResponse<ImageUploadStatusResponse, Object> resp = new AmxApiResponse<ImageUploadStatusResponse, Object>();
		try
		{
			String docSequenceNumber = requestQuoteDao.checkIfImageAlreadyUploaded(docType);
			ImageUploadStatusResponse imageUploadStatusResponse = new ImageUploadStatusResponse();
			imageUploadStatusResponse.setDocSequenceNumber(docSequenceNumber);
			resp.setData(imageUploadStatusResponse);
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<ImageMandatoryResponse, Object> getMandatoryImage()
	{
		AmxApiResponse<ImageMandatoryResponse, Object> resp = new AmxApiResponse<ImageMandatoryResponse, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = requestQuoteDao.getMandatoryImage();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setResults(arrayResponseModel.getDataArray());
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

}
