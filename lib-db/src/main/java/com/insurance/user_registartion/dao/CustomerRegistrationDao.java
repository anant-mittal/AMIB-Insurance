




package com.insurance.user_registartion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.insurance.user_registartion.interfaces.ICustomerRegistration;
import com.insurance.user_registartion.model.CompanySetUp;
import com.insurance.user_registartion.model.CustomerPersonalDetail;

import oracle.jdbc.OracleTypes;

@Repository
public class CustomerRegistrationDao implements ICustomerRegistration
{
	String TAG = "com.insurance.user_registartion.dao :: CustomerRegistrationDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public String addNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		logger.info(TAG + " addNewCustomer :: customerRegistrationDetails :" + customerPersonalDetail.toString());

		CallableStatement cs = null;

		getConnection();

		String callProcedure = "{call IRB_REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		int countyId = customerPersonalDetail.getCountryId();
		int compCd = customerPersonalDetail.getCompCd();
		int userSeqNumber = customerPersonalDetail.getUserSeqNumber();
		String userType = customerPersonalDetail.getUserType();
		String userCivilId = customerPersonalDetail.getCivilId();
		String userPassword = customerPersonalDetail.getPassword();
		String mobileNumber = customerPersonalDetail.getMobile();
		String emailId = customerPersonalDetail.getEmail();
		String mobVerificationCode = customerPersonalDetail.getMobVerificationCode();
		String emailVerificationCode = customerPersonalDetail.getEmailVerificationCode();
		String mobileVerified = customerPersonalDetail.getMobileVerified();
		String emailVerified = customerPersonalDetail.getEmailVerified();
		String refAmibcd = customerPersonalDetail.getRefAmibcd();
		String status = customerPersonalDetail.getStatus();
		String deviceType = customerPersonalDetail.getDeviceType();
		Date date = customerPersonalDetail.getDate();
		String createdDeviceId = customerPersonalDetail.getCreatedDeviceId();
		String createdBy = customerPersonalDetail.getCreatedBy();
		Date updateOn = customerPersonalDetail.getUpdateOn();
		String updateDeviceId = customerPersonalDetail.getUpdateDeviceId();

		Map<String, Object> output = null;

		try
		{
			cs = connection.prepareCall(callProcedure);

			cs.setInt(1, countyId);
			cs.setInt(2, compCd);
			cs.setString(3, userType);
			cs.setString(4, userCivilId);
			cs.setString(5, userPassword);
			cs.setString(6, mobileNumber);
			cs.setString(7, mobVerificationCode);
			cs.setString(8, emailId);
			cs.setString(9, emailVerificationCode);
			cs.setInt(10, 0);
			cs.setString(11, deviceType);
			cs.setDate(12, getCurrentDate());
			cs.setString(13, createdDeviceId);
			cs.setString(14, createdBy);
			cs.registerOutParameter(15, java.sql.Types.INTEGER);
			cs.registerOutParameter(16, java.sql.Types.VARCHAR);
			cs.registerOutParameter(17, java.sql.Types.VARCHAR);

			cs.executeUpdate();

			int userSequenceNumber = cs.getInt(15);

			logger.info(TAG + " addNewCustomer :: userSequenceNumber :" + userSequenceNumber);

			String recd = cs.getString(16);

			String error = cs.getString(17);
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		logger.info(TAG + " addNewCustomer :: output :" + output);

		return "success";

	}

	public ArrayList getCompanySetUp(int langid)
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COMPANY_SETUP(?,?,?,?)}";
		ArrayList<CompanySetUp> companySetUpArray = new ArrayList<CompanySetUp>();

		logger.info(TAG + " getUserDetails ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 0);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(2);

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

	private static java.sql.Date getCurrentDate()
	{
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
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
