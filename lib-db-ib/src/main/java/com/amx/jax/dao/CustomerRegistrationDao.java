
package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerLoginModel;
import com.amx.jax.models.CustomerRegistrationModel;
import com.amx.jax.models.FailureException;
import com.amx.jax.models.ResponseInfo;
import oracle.jdbc.OracleTypes;

@Repository
public class CustomerRegistrationDao
{
	static String TAG = "CustomerRegistrationDao :: ";
	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IMetaService metaService;
	
	Connection connection;

	//public ArrayList<CompanySetUp> getCompanySetUp(BigDecimal langId , String companyCode)
	public ArrayResponseModel getCompanySetUp(BigDecimal langId , String companyCode)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ArrayList<CompanySetUp> companySetUpArray = new ArrayList<CompanySetUp>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		
		String callProcedure = "{call IRB_GET_COMPANY_SETUP(?,?,?,?,?)}";
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setString(1,companyCode);
			callableStatement.setBigDecimal(2, langId);
			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(3);

			while (rs.next())
			{
				CompanySetUp companySetUp = new CompanySetUp();

				companySetUp.setCntryCd(rs.getBigDecimal(1));
				companySetUp.setCompCd(rs.getBigDecimal(2));
				companySetUp.setCompanyName(rs.getString(3));
				companySetUp.setCbox(rs.getString(4));
				companySetUp.setCpo(rs.getString(5));
				companySetUp.setTeli(rs.getString(6));
				companySetUp.setTeli2(rs.getString(7));
				companySetUp.setFax(rs.getString(8));
				companySetUp.setEmail(rs.getString(9));
				companySetUp.setRegNumber(rs.getString(10));
				companySetUp.setMainAct(rs.getBigDecimal(11));
				companySetUp.setMainActCenter(rs.getBigDecimal(12));
				companySetUp.setHeading(rs.getString(13));
				companySetUp.setDecimalPlaceUpTo(rs.getBigDecimal(14));
				companySetUp.setCurrency(rs.getString(15));
				companySetUp.setLangId(rs.getBigDecimal(16));
				companySetUp.setAppName(rs.getString(17));
				companySetUp.setSmsSenderId(rs.getString(18));
				companySetUp.setHelpLineNumber(rs.getString(19));
				companySetUp.setWebSite(rs.getString(20));
				companySetUp.setEmailSenderId(rs.getString(21));
				companySetUpArray.add(companySetUp);
			}
			arrayResponseModel.setDataArray(companySetUpArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(4));
			arrayResponseModel.setErrorMessage(callableStatement.getString(5));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getCompanySetUp :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel isValidCivilId(String civilid)
	{
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call O_VALIDATE_CIVILID(?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setString(2, civilid);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			arrayResponseModel.setData(result);
		}
		catch (SQLException e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"isValidCivilId :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	//public boolean isCivilIdExist(String civilid , String userType)
	public ArrayResponseModel isCivilIdExist(String civilid , String userType)
	{

		getConnection();
		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		String callFunction = "{ ? = call IRB_IF_ONLINE_USEREXIST(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(4, userType);
			callableStatement.setString(5, civilid);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			arrayResponseModel.setData(result);
		}
		catch (SQLException e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"isValidCivilId :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ResponseInfo isValidMobileNumber(String mobileNumber)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_VALIDATE_MOBILE(?,?,?,?,?)}";
		ResponseInfo validate = new ResponseInfo();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, mobileNumber);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			String errorCode = callableStatement.getString(4);
			String errorMessage = callableStatement.getString(5);

			if (null == errorCode)
			{
				return validate;
			}
			else
			{
				validate.setErrorCode(errorCode);
				validate.setErrorMessage(errorMessage);
				return validate;
			}
		}
		catch (SQLException e)
		{
			validate.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			validate.setErrorMessage(e.toString());
			logger.info(TAG+"isValidCivilId :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}

	//public boolean isMobileNumberExist(String mobilenumber , String userType)
	public ArrayResponseModel isMobileNumberExist(String mobilenumber , String userType)
	{

		getConnection();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_DUP_MOBILE(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(4, userType);
			callableStatement.setString(5, mobilenumber);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			arrayResponseModel.setData(result);
			
		}
		catch (SQLException e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"isMobileNumberExist :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel isEmailIdExist(String email , String userType)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		String callFunction = "{ ? = call IRB_IF_DUP_EMAIL(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(4, userType);
			callableStatement.setString(5, email);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			arrayResponseModel.setData(result);
			
		}
		catch (SQLException e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"isEmailIdExist :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel isOtpEnabled(String civilId , String userType)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		
		String callFunction = "{ ? = call IRB_IF_OTP_ENABLED(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(4, userType);
			callableStatement.setString(5, civilId);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			arrayResponseModel.setData(result);
			
		}
		catch (SQLException e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"isOtpEnabled :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ResponseInfo setOtpCount(String civilId , String userType)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_UPDATE_OTP_ATTEMPT(?,?,?,?,?,?)}";
		ResponseInfo validate = new ResponseInfo();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			String errorCode = callableStatement.getString(5);
			String errorMessage = callableStatement.getString(6);

			if (null == errorCode)
			{
				validate.setErrorCode(errorCode);
				validate.setErrorMessage(errorMessage);
				return validate;
			}
			else
			{
				validate.setErrorCode(errorCode);
				validate.setErrorMessage(errorMessage);
				return validate;
			}
		}
		catch (SQLException e)
		{
			validate.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			validate.setErrorMessage(e.toString());
			logger.info(TAG+"setOtpCount :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}

	public CustomerRegistrationModel addNewCustomer(CustomerRegistrationModel customerRegistrationModel , BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		BigDecimal countyId = customerRegistrationModel.getCountryId();
		BigDecimal compCd = customerRegistrationModel.getCompCd();
		String userType = customerRegistrationModel.getUserType();
		String userCivilId = customerRegistrationModel.getCivilId();
		String userPassword = customerRegistrationModel.getPassword();
		String mobileNumber = customerRegistrationModel.getMobile();
		String emailId = customerRegistrationModel.getEmail();
		String deviceType = customerRegistrationModel.getDeviceType();
		String createdDeviceId = customerRegistrationModel.getCreatedDeviceId();
		String createdBy = customerRegistrationModel.getCivilId();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, countyId);
			callableStatement.setBigDecimal(2, compCd);
			callableStatement.setString(3, userType);
			callableStatement.setString(4, userCivilId);
			callableStatement.setString(5, userPassword);
			callableStatement.setString(6, mobileNumber);
			callableStatement.setString(7, "");
			callableStatement.setString(8, emailId);
			callableStatement.setString(9, "");
			callableStatement.setBigDecimal(10, languageId);
			callableStatement.setString(11, deviceType);
			callableStatement.setDate(12, getCurrentDate());
			callableStatement.setString(13, createdDeviceId);
			callableStatement.setString(14, createdBy);
			callableStatement.registerOutParameter(15, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			BigDecimal userSequenceNumber = callableStatement.getBigDecimal(15);
			String errorCode = callableStatement.getString(16);
			String errorMessage = callableStatement.getString(17);
			customerRegistrationModel = new CustomerRegistrationModel();

			if (errorCode == null)
			{
				customerRegistrationModel.setStatus(true);
			}
			else
			{
				customerRegistrationModel.setStatus(false);
			}
			customerRegistrationModel.setCivilid(userCivilId);
			customerRegistrationModel.setErrorCode(errorCode);
			customerRegistrationModel.setErrorMessage(errorMessage);
			customerRegistrationModel.setUserSequenceNumber(userSequenceNumber);
		}
		catch (Exception e)
		{
			customerRegistrationModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			customerRegistrationModel.setErrorMessage(e.toString());
			logger.info(TAG+"addNewCustomer :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerRegistrationModel;
	}

	public CustomerDetailModel getUserDetails(String civilId , String userType , BigDecimal userSeqNum , BigDecimal languageId)
	{
		getConnection();
		CustomerDetailModel customerDetailModel = null;
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_USERDTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String lastDate = null;

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, languageId);
			callableStatement.setBigDecimal(6, userSeqNum);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.TIMESTAMP);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerDetailModel = new CustomerDetailModel();
			customerDetailModel.setMobile(callableStatement.getString(7));
			customerDetailModel.setEmail(callableStatement.getString(8));
			customerDetailModel.setLanguageId(callableStatement.getBigDecimal(9));
			customerDetailModel.setMobileVerify(callableStatement.getString(10));
			customerDetailModel.setMailVerify(callableStatement.getString(11));
			if (null != callableStatement.getTimestamp(12))
			{
				lastDate = formatDate(callableStatement.getTimestamp(12).toString());
			}
			customerDetailModel.setLastLogin(lastDate);
			customerDetailModel.setDeviceId(callableStatement.getString(13));
			customerDetailModel.setDeviceType(callableStatement.getString(14));
			customerDetailModel.setUserName(callableStatement.getString(15));
			customerDetailModel.setDbStatus(callableStatement.getString(16));
			customerDetailModel.setCustSequenceNumber(callableStatement.getBigDecimal(17));
			customerDetailModel.setErrorCode(callableStatement.getString(18));
			customerDetailModel.setErrorMessage(callableStatement.getString(19));

			if (callableStatement.getString(18) == null)
			{
				customerDetailModel.setStatus(true);
			}
			else
			{
				customerDetailModel.setStatus(false);
			}

		}
		catch (Exception e)
		{
			customerDetailModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			customerDetailModel.setErrorMessage(e.toString());
			logger.info(TAG+"getUserDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerDetailModel;
	}

	public CustomerLoginModel validateUserLogin(CustomerLoginModel customerLoginModel , String userType , BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_LOGIN(?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, customerLoginModel.getCivilId());
			callableStatement.setString(5, customerLoginModel.getPassword());
			callableStatement.setString(6, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(7, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setBigDecimal(8, languageId);
			callableStatement.registerOutParameter(9, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(10, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			BigDecimal userSequenceNumber = callableStatement.getBigDecimal(9);
			BigDecimal amibRef = callableStatement.getBigDecimal(10);
			String errorCode = callableStatement.getString(11);
			String errorMessage = callableStatement.getString(12);
			
			customerLoginModel = new CustomerLoginModel();

			if (errorCode == null)
			{
				customerLoginModel.setStatus(true);
			}
			else
			{
				customerLoginModel.setStatus(false);
			}
			customerLoginModel.setUserSeqNum(userSequenceNumber);
			customerLoginModel.setAmibRef(amibRef);
			customerLoginModel.setErrorCode(errorCode);
			customerLoginModel.setErrorMessage(errorMessage);

		}
		catch (Exception e)
		{
			customerLoginModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			customerLoginModel.setErrorMessage(e.toString());
			logger.info(TAG+"getCompanySetUp :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerLoginModel;
	}

	public CustomerDetailModel updatePassword(CustomerDetailModel customerDetailModel , String civilId , String userType)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_RESET_PWD(?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setString(5, customerDetailModel.getPassword());
			callableStatement.setBigDecimal(6, null);
			callableStatement.setDate(7, getCurrentDate());
			callableStatement.setString(8, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(9, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(10, civilId);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerDetailModel = new CustomerDetailModel();

			String errorCode = callableStatement.getString(11);
			String errorMessage = callableStatement.getString(12);

			logger.info(TAG + " updatePassword :: errorCode     :" + errorCode);
			logger.info(TAG + " updatePassword :: errorMessage  :" + errorMessage);
			
			customerDetailModel.setErrorCode(errorCode);
			customerDetailModel.setErrorMessage(errorMessage);

			if (errorCode == null)
			{
				customerDetailModel.setStatus(true);
			}
			else
			{
				customerDetailModel.setStatus(false);
			}

		}
		catch (Exception e)
		{
			customerDetailModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			customerDetailModel.setErrorMessage(e.toString());
			logger.info(TAG+"updatePassword :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerDetailModel;
	}

	public FailureException setFailedException(String type, FailureException failureException)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CREATE_EXCEPTION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, failureException.getCountryId());
			callableStatement.setBigDecimal(2, failureException.getCompCd());
			callableStatement.setString(3, failureException.getUserType());
			callableStatement.setString(4, failureException.getCivilId());
			callableStatement.setString(5, failureException.getExceptionType());
			callableStatement.setString(6, failureException.getExceptionMsg());
			callableStatement.setString(7, failureException.getMobileNumber());
			callableStatement.setString(8, failureException.getEmailId());
			callableStatement.setBigDecimal(9, null);
			callableStatement.setBigDecimal(10, null);
			callableStatement.setBigDecimal(11, null);
			callableStatement.setBigDecimal(12, null);
			callableStatement.setBigDecimal(13, null);
			callableStatement.setBigDecimal(14, null);
			callableStatement.setString(15, failureException.getDeviceType());
			callableStatement.setString(16, failureException.getDeviceId());
			callableStatement.setString(17, failureException.getCivilId());
			callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			failureException = new FailureException();

			String errorCode = callableStatement.getString(18);
			String errorMessage = callableStatement.getString(19);

			failureException.setErrorCode(errorCode);
			failureException.setErrorMessage(errorMessage);

			if (errorCode == null)
			{
				failureException.setStatus(true);
			}
			else
			{
				failureException.setStatus(false);
			}
		}
		catch (Exception e)
		{
			failureException.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			failureException.setErrorMessage(e.toString());
			logger.info(TAG+"setFailedException :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return failureException;
	}

	private static java.sql.Date getCurrentDate()
	{
		java.util.Date todayNew = null;

		try
		{
			java.util.Date today = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SS");
			String strDate = sdf.format(today.getTime());
			todayNew = sdf.parse(strDate);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new java.sql.Date(todayNew.getTime());
	}

	private Connection getConnection()
	{
		try
		{
			if (null == connection || connection.isClosed())
			{
				connection = jdbcTemplate.getDataSource().getConnection();
			}
			return connection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	private void CloseConnection(CallableStatement callableStatement, Connection connection)
	{
		try
		{
			if (callableStatement != null)
			{
				callableStatement.close();
			}

			if (connection != null)
			{
				connection.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String formatDate(String inDate)
	{
		String outDate = "";
		SimpleDateFormat inSm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outSm = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss aaa");
		try
		{
			Date date = inSm.parse(inDate);
			outDate = outSm.format(date);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return outDate;
	}
}
