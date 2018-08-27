
package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerLoginModel;
import com.amx.jax.models.CustomerRegistrationModel;
import com.amx.jax.models.FailureException;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.Validate;
import oracle.jdbc.OracleTypes;

@Repository
public class CustomerRegistrationDao
{
	static String TAG = "com.insurance.user_registartion.dao :: CustomerRegistrationDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;

	@Autowired
	RegSession regSession;

	Connection connection;

	public ArrayList<CompanySetUp> getCompanySetUp(BigDecimal langId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COMPANY_SETUP(?,?,?,?,?)}";
		ArrayList<CompanySetUp> companySetUpArray = new ArrayList<CompanySetUp>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setString(1, "AMIB");
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
				companySetUp.setDecplc(rs.getBigDecimal(14));
				companySetUp.setCurrency(rs.getString(15));
				companySetUp.setLangId(rs.getBigDecimal(16));
				companySetUp.setAppName(rs.getString(17));
				companySetUp.setSmsSenderId(rs.getString(18));
				companySetUp.setHelpLineNumber(rs.getString(19));
				companySetUp.setWebSite(rs.getString(20));
				companySetUp.setEmailSenderId(rs.getString(21));

				companySetUpArray.add(companySetUp);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return companySetUpArray;
	}

	public boolean isValidCivilId(String civilid)
	{
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

			if (null == result)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return false;
	}

	public boolean isCivilIdExist(String civilid)
	{

		getConnection();

		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_ONLINE_USEREXIST(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, regSession.getCountryId());
			callableStatement.setBigDecimal(3, regSession.getCompCd());
			callableStatement.setString(4, regSession.getUserType());
			callableStatement.setString(5, civilid);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);

			if (result.equalsIgnoreCase("Y"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return false;
	}

	public Validate isValidMobileNumber(String mobileNumber)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_VALIDATE_MOBILE(?,?,?,?,?)}";
		Validate validate = new Validate();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, mobileNumber);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			String errorCode = callableStatement.getString(4);
			String errorMessage = callableStatement.getString(5);

			if (null == errorCode)
			{
				validate.setValid(true);
				return validate;
			}
			else
			{
				validate.setValid(false);
				validate.setErrorCode(errorCode);
				validate.setErrorMessage(errorMessage);
				return validate;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}

	public boolean isMobileNumberExist(String mobilenumber)
	{

		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_DUP_MOBILE(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaData.getCountryId());
			callableStatement.setBigDecimal(3, metaData.getCompCd());
			callableStatement.setString(4, metaData.getUserType());
			callableStatement.setString(5, mobilenumber);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);

			if (result.equalsIgnoreCase("Y"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return false;
	}

	public boolean isEmailIdExist(String email)
	{

		getConnection();

		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_DUP_EMAIL(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaData.getCountryId());
			callableStatement.setBigDecimal(3, metaData.getCompCd());
			callableStatement.setString(4, metaData.getUserType());
			callableStatement.setString(5, email);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);

			if (result.equalsIgnoreCase("Y"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return false;
	}

	public boolean isOtpEnabled(String civilId)
	{

		getConnection();

		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_OTP_ENABLED(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaData.getCountryId());
			callableStatement.setBigDecimal(3, metaData.getCompCd());
			callableStatement.setString(4, metaData.getUserType());
			callableStatement.setString(5, civilId);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);

			if (result.equalsIgnoreCase("Y"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return false;
	}

	public Validate setOtpCount(String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_UPDATE_OTP_ATTEMPT(?,?,?,?,?,?)}";
		Validate validate = new Validate();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, metaData.getUserType());
			callableStatement.setString(4, civilId);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			String errorCode = callableStatement.getString(5);
			String errorMessage = callableStatement.getString(6);

			if (null == errorCode)
			{
				validate.setValid(true);
				validate.setErrorCode(errorCode);
				validate.setErrorMessage(errorMessage);
				return validate;
			}
			else
			{
				validate.setValid(false);
				validate.setErrorCode(errorCode);
				validate.setErrorMessage(errorMessage);
				return validate;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}

	public CustomerRegistrationModel addNewCustomer(CustomerRegistrationModel customerRegistrationModel)
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
			callableStatement.setBigDecimal(10, regSession.getLanguageId());
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerRegistrationModel;
	}

	public CustomerDetailModel getUserDetails(String civilId)
	{
		getConnection();
		CustomerDetailModel customerDetailModel = null;
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_USERDTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, metaData.getUserType());
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, metaData.getLanguageId());
			callableStatement.setBigDecimal(6, metaData.getUserSequenceNumber());
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
			
			String lastDate = "";
			
			if(null != callableStatement.getTimestamp(12))
			{
				logger.info(TAG + " getUserDetails :: Last Login Date :" + callableStatement.getTimestamp(12));
				lastDate = formatDate(callableStatement.getTimestamp(12).toString());
				logger.info(TAG + " getUserDetails :: lastDate :" + lastDate);
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerDetailModel;
	}

	public CustomerLoginModel validateUserLogin(CustomerLoginModel customerLoginModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_LOGIN(?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, regSession.getCountryId());
			callableStatement.setBigDecimal(2, regSession.getCompCd());
			callableStatement.setString(3, regSession.getUserType());
			callableStatement.setString(4, customerLoginModel.getCivilId());
			callableStatement.setString(5, customerLoginModel.getPassword());
			callableStatement.setString(6, regSession.getDeviceId());
			callableStatement.setString(7, regSession.getDeviceType());
			callableStatement.registerOutParameter(8, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(9, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			BigDecimal userSequenceNumber = callableStatement.getBigDecimal(8);
			BigDecimal amibRef = callableStatement.getBigDecimal(9);
			String errorCode = callableStatement.getString(10);
			String errorMessage = callableStatement.getString(11);

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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerLoginModel;
	}

	public CustomerDetailModel updatePassword(CustomerDetailModel customerDetailModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_RESET_PWD(?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, regSession.getCountryId());
			callableStatement.setBigDecimal(2, regSession.getCompCd());
			callableStatement.setString(3, regSession.getUserType());
			callableStatement.setString(4, regSession.getCivilId());
			callableStatement.setString(5, customerDetailModel.getPassword());
			callableStatement.setBigDecimal(6, null);
			callableStatement.setDate(7, getCurrentDate());
			callableStatement.setString(8, regSession.getDeviceId());
			callableStatement.setString(9, regSession.getDeviceType());
			callableStatement.setString(10, regSession.getCivilId());
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerDetailModel = new CustomerDetailModel();

			String errorCode = callableStatement.getString(11);
			String errorMessage = callableStatement.getString(12);

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
			connection = jdbcTemplate.getDataSource().getConnection();
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
