
package com.amx.jax.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.amx.jax.models.Validate;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.ApiConstants;
import oracle.jdbc.OracleTypes;

@Repository
public class CustomerRegistrationDao
{
	static String TAG = "com.insurance.user_registartion.dao :: CustomerRegistrationDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public ArrayList<CompanySetUp> getCompanySetUp(int langId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COMPANY_SETUP(?,?,?,?,?)}";
		ArrayList<CompanySetUp> companySetUpArray = new ArrayList<CompanySetUp>();

		logger.info(TAG + " getCompanySetUp ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setString(1, "AMIB");
			callableStatement.setInt(2, langId);
			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(3);

			while (rs.next())
			{
				CompanySetUp companySetUp = new CompanySetUp();

				companySetUp.setCntryCd(rs.getInt(1));
				companySetUp.setCompCd(rs.getInt(2));
				companySetUp.setCompanyName(rs.getString(3));
				companySetUp.setCbox(rs.getString(4));
				companySetUp.setCpo(rs.getString(5));
				companySetUp.setTeli(rs.getString(6));
				companySetUp.setTeli2(rs.getString(7));
				companySetUp.setFax(rs.getString(8));
				companySetUp.setEmail(rs.getString(9));
				companySetUp.setRegNumber(rs.getString(10));
				companySetUp.setMainAct(rs.getInt(11));
				companySetUp.setMainActCenter(rs.getInt(12));
				companySetUp.setHeading(rs.getString(13));
				companySetUp.setDecplc(rs.getInt(14));
				companySetUp.setCurrency(rs.getString(15));
				companySetUp.setLangId(rs.getInt(16));
				companySetUp.setAppName(rs.getString(17));
				companySetUp.setSmsSenderId(rs.getString(18));
				
				logger.info(TAG + " getCompanySetUp :: companySetUp :" + companySetUp.toString());

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
			logger.info(TAG + " isValidCivilId :: result :" + result);

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
		logger.info(TAG + " isCivilIdExist :: civilid :" + civilid);

		getConnection();

		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_ONLINE_USEREXIST(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setInt(2, 1);
			callableStatement.setInt(3, 10);
			callableStatement.setString(4, "D");
			callableStatement.setString(5, civilid);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			logger.info(TAG + " isCivilIdExist :: result :" + result);

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
		logger.info(TAG + " isValidMobileNumber :: civilid :" + mobileNumber);

		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_VALIDATE_MOBILE(?,?,?,?,?)}";
		Validate validate = new Validate();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setString(1, DetailsConstants.CNTRYCD);
			callableStatement.setString(2, DetailsConstants.COMPCD);
			callableStatement.setString(3, mobileNumber);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			String errorCode = callableStatement.getString(4);
			String errorMessage = callableStatement.getString(5);

			logger.info(TAG + " isValidMobileNumber :: errorCode :" + errorCode);
			logger.info(TAG + " isValidMobileNumber :: errorMessage :" + errorMessage);

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
		logger.info(TAG + " isMobileNumberExist :: mobilenumber :" + mobilenumber);

		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_DUP_MOBILE(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setInt(2, 1);
			callableStatement.setInt(3, 10);
			callableStatement.setString(4, "D");
			callableStatement.setString(5, mobilenumber);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			logger.info(TAG + " isMobileNumberExist :: result :" + result);

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

	public CustomerRegistrationModel addNewCustomer(CustomerRegistrationModel customerRegistrationModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		int countyId = customerRegistrationModel.getCountryId();
		int compCd = customerRegistrationModel.getCompCd();
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

			callableStatement.setInt(1, countyId);
			callableStatement.setInt(2, compCd);
			callableStatement.setString(3, userType);
			callableStatement.setString(4, userCivilId);
			callableStatement.setString(5, userPassword);
			callableStatement.setString(6, mobileNumber);
			callableStatement.setString(7, "");
			callableStatement.setString(8, emailId);
			callableStatement.setString(9, "");
			callableStatement.setInt(10, 0);
			callableStatement.setString(11, deviceType);
			callableStatement.setDate(12, getCurrentDate());
			callableStatement.setString(13, createdDeviceId);
			callableStatement.setString(14, createdBy);
			callableStatement.registerOutParameter(15, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			int userSequenceNumber = callableStatement.getInt(15);
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

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 10);
			callableStatement.setString(3, "D");
			callableStatement.setString(4, civilId);
			callableStatement.setInt(5, 0);
			callableStatement.setBigDecimal(6, null);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.DATE);
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
			logger.info(TAG + " getUserDetails :: callableStatement.getString(8) :" + callableStatement.getString(8));
			customerDetailModel.setLanguageId(callableStatement.getInt(9));
			customerDetailModel.setMobileVerify(callableStatement.getString(10));
			customerDetailModel.setMailVerify(callableStatement.getString(11));
			customerDetailModel.setLastLogin(callableStatement.getDate(12));
			customerDetailModel.setDeviceId(callableStatement.getString(13));
			customerDetailModel.setDeviceType(callableStatement.getString(14));
			customerDetailModel.setCivilId(callableStatement.getString(15));
			customerDetailModel.setDbStatus(callableStatement.getString(16));
			customerDetailModel.setUserSequenceNumber(callableStatement.getInt(17));
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
		String callProcedure = "{call IRB_CHECK_LOGIN(?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, customerLoginModel.getCountryId());
			callableStatement.setInt(2, customerLoginModel.getCompCd());
			callableStatement.setString(3, customerLoginModel.getUserType());
			callableStatement.setString(4, customerLoginModel.getCivilId());
			callableStatement.setString(5, customerLoginModel.getPassword());
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			int userSequenceNumber = callableStatement.getInt(6);
			int amibRef = callableStatement.getInt(7);
			String errorCode = callableStatement.getString(8);
			String errorMessage = callableStatement.getString(9);
			
			logger.info(TAG + " validateUserLogin :: errorCode :" + errorCode);
			logger.info(TAG + " validateUserLogin :: errorMessage :" + errorMessage);

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
			
			callableStatement.setInt(1, customerDetailModel.getCountryId());
			logger.info(TAG + " updatePassword :: getCountryId :" + customerDetailModel.getCountryId());
			callableStatement.setInt(2, customerDetailModel.getCompCd());
			logger.info(TAG + " updatePassword :: getCompCd :" + customerDetailModel.getCompCd());
			callableStatement.setString(3, customerDetailModel.getUserType());
			logger.info(TAG + " updatePassword :: getUserType :" + customerDetailModel.getUserType());
			callableStatement.setString(4, customerDetailModel.getCivilId());
			logger.info(TAG + " updatePassword :: getCivilId :" + customerDetailModel.getCivilId());
			callableStatement.setString(5, customerDetailModel.getPassword());
			logger.info(TAG + " updatePassword :: getPassword :" + customerDetailModel.getPassword());
			callableStatement.setBigDecimal(6, null);
			callableStatement.setDate(7, getCurrentDate());
			callableStatement.setString(8, customerDetailModel.getDeviceId());//Device ID
			logger.info(TAG + " updatePassword :: getDeviceId :" + customerDetailModel.getDeviceId());
			callableStatement.setString(9, customerDetailModel.getDeviceType());//Device Type
			callableStatement.setString(10, customerDetailModel.getCivilId());//CivilId
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerDetailModel = new CustomerDetailModel();
			
			String errorCode = callableStatement.getString(11);
			String errorMessage = callableStatement.getString(12);
			
			logger.info(TAG + " updatePassword :: errorCode :" + errorCode);
			logger.info(TAG + " updatePassword :: errorMessage :" + errorMessage);
			
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
		

	private static java.sql.Date getCurrentDate()
	{
		java.util.Date todayNew = null;

		try
		{
			java.util.Date today = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SS");
			String strDate = sdf.format(today.getTime());
			logger.info(TAG + " getCurrentDate :: strDate :" + strDate);
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
}
