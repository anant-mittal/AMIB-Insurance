
package com.amx.jax.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.models.Area;
import com.amx.jax.models.Business;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.CustomerProfileDetailRequest;
import com.amx.jax.models.Governorates;
import com.amx.jax.models.Nationality;

import org.springframework.beans.factory.annotation.Autowired;
import oracle.jdbc.OracleTypes;

@Repository
public class PersonalDetailsDao
{
	String TAG = "com.insurance.personaldetails.dao.PersonalDetailsDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public CustomerProfileDetailModel getUserProfileDetails(CustomerProfileDetailModel customerProfileDetailModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_PROFILE_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			//callableStatement.setInt(1, customerProfileDetailModel.getCountryId());
			//callableStatement.setInt(2, customerProfileDetailModel.getCompCd());
			callableStatement.setString(3, customerProfileDetailModel.getUserType());
			callableStatement.setString(4, customerProfileDetailModel.getCivilId());
			//callableStatement.setInt(5, customerProfileDetailModel.getLanguageId());
			callableStatement.setBigDecimal(6, customerProfileDetailModel.getCustSequenceNumber());

			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.DATE);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(21, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(22, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(23, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(24, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerProfileDetailModel = new CustomerProfileDetailModel();

			customerProfileDetailModel.setEnglishName(callableStatement.getString(7));
			customerProfileDetailModel.setNativeArabicName(callableStatement.getString(8));
			customerProfileDetailModel.setGenderCode(callableStatement.getString(9));
			customerProfileDetailModel.setGenderDesc(callableStatement.getString(10));
			customerProfileDetailModel.setIdExpiryDate(callableStatement.getDate(11));
			customerProfileDetailModel.setBusinessCode(callableStatement.getString(12));
			customerProfileDetailModel.setBusinessDesc(callableStatement.getString(13));
			customerProfileDetailModel.setNatyCode(callableStatement.getString(14));
			customerProfileDetailModel.setNatyDesc(callableStatement.getString(15));
			customerProfileDetailModel.setGovCode(callableStatement.getString(16));
			customerProfileDetailModel.setGovDesc(callableStatement.getString(17));
			customerProfileDetailModel.setAreaCode(callableStatement.getString(18));
			customerProfileDetailModel.setAreaDesc(callableStatement.getString(19));
			customerProfileDetailModel.setMobile(callableStatement.getString(20));
			customerProfileDetailModel.setEmail(callableStatement.getString(21));
			//customerProfileDetailModel.setLanguageId(callableStatement.getInt(22));
			customerProfileDetailModel.setErrorCode(callableStatement.getString(23));
			customerProfileDetailModel.setErrorMessage(callableStatement.getString(24));

			if (callableStatement.getString(23) == null)
			{
				customerProfileDetailModel.setStatus(true);
			}
			else
			{
				customerProfileDetailModel.setStatus(false);
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
		return customerProfileDetailModel;
	}

	public ArrayList getBusiness()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_BUSINESS(?,?,?,?,?,?)}";
		ArrayList<Business> businessArray = new ArrayList<Business>();

		logger.info(TAG + " getBusiness ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 10);
			callableStatement.setInt(3, 0);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Business business = new Business();
				business.setBusinessCode(rs.getString(1));
				logger.info(TAG + " getBusiness :: Business code :" + rs.getString(1).toString());
				business.setBusinessDesc(rs.getString(2));
				logger.info(TAG + " getBusiness :: Business Arabic  disc :" + rs.getString(2).toString());
				businessArray.add(business);
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

		return businessArray;
	}

	public ArrayList getNationality()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_NATIONALITIES(?,?,?,?,?,?)}";
		ArrayList<Nationality> nationalityArray = new ArrayList<Nationality>();

		logger.info(TAG + " getNationality ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 10);
			callableStatement.setInt(3, 1);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Nationality nationality = new Nationality();
				nationality.setNationalityCode(rs.getString(1));
				logger.info(TAG + " getNationality :: Nationality code :" + rs.getString(1));
				nationality.setNationalityDesc(rs.getString(2));
				logger.info(TAG + " getNationality :: Nationality disc :" + rs.getString(2));
				nationalityArray.add(nationality);
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

		return nationalityArray;
	}

	public ArrayList getGovernorates()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_GOVERNORATES(?,?,?,?,?,?)}";
		ArrayList<Governorates> governoratesArray = new ArrayList<Governorates>();

		logger.info(TAG + " getGovernorates ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 10);
			callableStatement.setInt(3, 0);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Governorates governorates = new Governorates();
				governorates.setGovCode(rs.getString(1));
				logger.info(TAG + " getNationality :: gov code :" + rs.getString(1));
				governorates.setGovDesc(rs.getString(2));
				logger.info(TAG + " getNationality :: gov disc :" + rs.getString(2));
				governoratesArray.add(governorates);
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

		return governoratesArray;
	}

	public ArrayList getArea(String gov)
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_AREA(?,?,?,?,?,?,?)}";
		ArrayList<Area> areaArray = new ArrayList<Area>();

		logger.info(TAG + " getArea ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 10);
			callableStatement.setString(3, gov);
			callableStatement.setInt(4, 0);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);

			while (rs.next())
			{
				Area area = new Area();
				area.setAreaCode(rs.getString(1));
				logger.info(TAG + " getArea :: Area code :" + rs.getString(1).toString());
				area.setAreaDesc(rs.getString(2));
				logger.info(TAG + " getArea :: Area disc :" + rs.getString(2).toString());
				areaArray.add(area);
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

		return areaArray;
	}

	public ArrayList getGender()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_GENDER(?,?,?,?,?,?,?)}";
		ArrayList<Area> areaArray = new ArrayList<Area>();

		logger.info(TAG + " getArea ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 10);
			callableStatement.setInt(3, 0);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Area area = new Area();
				area.setAreaCode(rs.getString(1));
				logger.info(TAG + " getArea :: Area code :" + rs.getString(1).toString());
				area.setAreaDesc(rs.getString(2));
				logger.info(TAG + " getArea :: Area disc :" + rs.getString(2).toString());
				areaArray.add(area);
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

		return areaArray;
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
