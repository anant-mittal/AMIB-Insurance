
package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.boot.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.user.UserSessionRegistry;
import org.springframework.stereotype.Repository;

import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.AddressTypeDto;
import com.amx.jax.models.Area;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.Business;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.Gender;
import com.amx.jax.models.Governorates;
import com.amx.jax.models.Nationality;


import org.springframework.beans.factory.annotation.Autowired;
import oracle.jdbc.OracleTypes;

@Repository
public class PersonalDetailsDao
{
	String TAG = "PersonalDetailsDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IMetaService metaService;
	
	Connection connection;
	


	public CustomerProfileDetailModel getProfileDetails(String civilId , String userType , BigDecimal custSeqNum, BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_PROFILE_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CustomerProfileDetailModel customerProfileDetailModel = null;

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, languageId);
			callableStatement.setBigDecimal(6, custSeqNum);
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
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(26, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(27, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(28, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(29, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(30, java.sql.Types.VARCHAR);
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
			customerProfileDetailModel.setLanguageId(callableStatement.getBigDecimal(22));
			customerProfileDetailModel.setAddressType(callableStatement.getString(23));
			customerProfileDetailModel.setAddressDesc(callableStatement.getString(24));
			customerProfileDetailModel.setBlock(callableStatement.getString(25));
			customerProfileDetailModel.setStreet(callableStatement.getString(26));
			customerProfileDetailModel.setBuilding(callableStatement.getString(27));
			customerProfileDetailModel.setFlat(callableStatement.getString(28));
			customerProfileDetailModel.setErrorCode(callableStatement.getString(29));
			customerProfileDetailModel.setErrorMessage(callableStatement.getString(30));
		}
		catch (Exception e)
		{
			customerProfileDetailModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			customerProfileDetailModel.setErrorMessage(e.toString());
			logger.info(TAG+"getProfileDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerProfileDetailModel;
	}

	public CustomerProfileDetailModel updateProfileDetails(CustomerProfileDetailModel customerProfileDetailModel , String civilId , String userType , BigDecimal custSeqNum, BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_UPDINS_PERSONALPROF_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, custSeqNum);
			callableStatement.setString(6, customerProfileDetailModel.getEnglishName());
			callableStatement.setString(7, customerProfileDetailModel.getNativeArabicName());
			callableStatement.setString(8, customerProfileDetailModel.getGenderCode());
			callableStatement.setDate(9, customerProfileDetailModel.getIdExpiryDate());
			callableStatement.setString(10, customerProfileDetailModel.getBusinessCode());
			callableStatement.setString(11, customerProfileDetailModel.getNatyCode());
			callableStatement.setString(12, customerProfileDetailModel.getGovCode());
			callableStatement.setString(13, customerProfileDetailModel.getAreaCode());
			callableStatement.setString(14, customerProfileDetailModel.getMobile());
			callableStatement.setString(15, customerProfileDetailModel.getEmail());
			callableStatement.setBigDecimal(16, languageId);
			callableStatement.setString(17, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(18, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(19, civilId);
			callableStatement.setString(20, customerProfileDetailModel.getAddressType());
			callableStatement.setString(21, customerProfileDetailModel.getBlock());
			callableStatement.setString(22, customerProfileDetailModel.getStreet());
			callableStatement.setString(23, customerProfileDetailModel.getBuilding());
			callableStatement.setString(24, customerProfileDetailModel.getFlat());

			callableStatement.registerOutParameter(5, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(26, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerProfileDetailModel = new CustomerProfileDetailModel();
			if (null != callableStatement.getBigDecimal(5))
			{
				//logger.info(TAG + " updateProfileDetails :: customerSequenceNumber :" + callableStatement.getBigDecimal(5));
				customerProfileDetailModel.setCustSequenceNumber(callableStatement.getBigDecimal(5));
			}

			customerProfileDetailModel.setErrorCode(callableStatement.getString(25));
			customerProfileDetailModel.setErrorMessage(callableStatement.getString(26));
		}
		catch (Exception e)
		{
			customerProfileDetailModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			customerProfileDetailModel.setErrorMessage(e.toString());
			logger.info(TAG+"updateProfileDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerProfileDetailModel;
	}

	public ArrayResponseModel getBusiness(BigDecimal languageId)
	{
		getConnection();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_BUSINESS(?,?,?,?,?,?)}";
		ArrayList<Business> businessArray = new ArrayList<Business>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, languageId);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Business business = new Business();
				business.setBusinessCode(rs.getString(1));
				business.setBusinessDesc(rs.getString(2));
				businessArray.add(business);
			}
			
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
			arrayResponseModel.setDataArray(businessArray);

		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getBusiness :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getNationality(BigDecimal languageId)
	{
		getConnection();

		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		String callProcedure = "{call IRB_GET_NATIONALITIES(?,?,?,?,?,?)}";
		ArrayList<Nationality> nationalityArray = new ArrayList<Nationality>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, languageId);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Nationality nationality = new Nationality();
				nationality.setNationalityCode(rs.getString(1));
				nationality.setNationalityDesc(rs.getString(2));
				nationalityArray.add(nationality);
			}
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
			arrayResponseModel.setDataArray(nationalityArray);

		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getNationality :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getGovernorates(BigDecimal languageId)
	{
		getConnection();

		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		String callProcedure = "{call IRB_GET_GOVERNORATES(?,?,?,?,?,?)}";
		ArrayList<Governorates> governoratesArray = new ArrayList<Governorates>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, languageId);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Governorates governorates = new Governorates();
				governorates.setGovCode(rs.getString(1));
				governorates.setGovDesc(rs.getString(2));
				governoratesArray.add(governorates);
			}
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
			arrayResponseModel.setDataArray(governoratesArray);
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getNationality :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getArea(String gov ,BigDecimal languageId)
	{
		getConnection();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_AREA(?,?,?,?,?,?,?)}";
		ArrayList<Area> areaArray = new ArrayList<Area>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, gov);
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);

			while (rs.next())
			{
				Area area = new Area();
				area.setAreaCode(rs.getString(1));
				area.setAreaDesc(rs.getString(2));
				areaArray.add(area);
			}
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setErrorMessage(callableStatement.getString(7));
			arrayResponseModel.setDataArray(areaArray);
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getArea :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getGender(BigDecimal languageId)
	{
		getConnection();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_GENDER(?,?,?,?,?,?)}";
		ArrayList<Gender> genderArray = new ArrayList<Gender>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, languageId);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Gender gender = new Gender();
				gender.setGenderCode(rs.getString(1));
				gender.setGenderDesc(rs.getString(2));
				genderArray.add(gender);
			}
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
			arrayResponseModel.setDataArray(genderArray);

		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getGender :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}
	
	public ArrayResponseModel getAddressType(BigDecimal languageId) {
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ADDRTYPE(?,?,?,?,?,?)}";
		
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, languageId);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			ResultSet rs = (ResultSet) callableStatement.getObject(4);
			ArrayList<AddressTypeDto> addressTypeDtoList = new ArrayList<>();
			while (rs.next())
			{
				AddressTypeDto addressTypeDto = new AddressTypeDto();
				addressTypeDto.setAddressType(rs.getString(1));
				addressTypeDto.setAddressDesc(rs.getString(2));
				addressTypeDtoList.add(addressTypeDto);
				
			}
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setDataArray(addressTypeDtoList);
			
			
		}catch(Exception e) {
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getAddressType :: exception :" + e);
			e.printStackTrace();
		}
		
		return arrayResponseModel;
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
}
