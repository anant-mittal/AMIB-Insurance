
package com.amx.jax.dao;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CodeDesc;
import com.amx.jax.models.Colour;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.DownloadImageModel;
import com.amx.jax.models.FuelType;
import com.amx.jax.models.ImageDetails;
import com.amx.jax.models.ImageModel;
import com.amx.jax.models.ImageStatus;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.InsuranceCompanyDetails;
import com.amx.jax.models.Make;
import com.amx.jax.models.Model;
import com.amx.jax.models.Purpose;
import com.amx.jax.models.Shape;
import com.amx.jax.models.VehicleCondition;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.models.VehicleDetailsGetModel;
import com.amx.jax.models.VehicleDetailsHeaderModel;
import com.amx.jax.models.VehicleDetailsUpdateModel;
import com.amx.jax.utility.Utility;
import com.amx.utils.Constants;

import oracle.jdbc.OracleTypes;

@Component
public class RequestQuoteDao
{
	String TAG = "RequestQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Connection connection;

	@Autowired
	IMetaService metaService;

	public IncompleteApplModel getIncompleteApplication(String civilId , String userType , BigDecimal custSeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_INCOMPLETE_APPL(?,?,?,?,?,?,?,?,?)}";
		IncompleteApplModel incompleteApplModel = new IncompleteApplModel();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, custSeqNum);
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			incompleteApplModel.setAppSeqNumber(callableStatement.getBigDecimal(6));
			incompleteApplModel.setAppStage(callableStatement.getString(7));
			incompleteApplModel.setErrorCode(callableStatement.getString(8));
			incompleteApplModel.setErrorMessage(callableStatement.getString(9));
		}
		catch (Exception e)
		{
			incompleteApplModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			incompleteApplModel.setErrorMessage(e.toString());
			logger.info(TAG+"getIncompleteApplication :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return incompleteApplModel;
	}

	public ArrayResponseModel getMake(BigDecimal languageId)
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_MAKES(?,?,?,?,?,?)}";
		ArrayList<Make> makeArray = new ArrayList<Make>();
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
			while (rs.next())
			{
				Make make = new Make();
				make.setMakeCode(rs.getString(1));
				make.setMakeDesc(rs.getString(2));
				makeArray.add(make);
			}
			arrayResponseModel.setDataArray(makeArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));

		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getMake :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getModel(String make , BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SUBMAKE(?,?,?,?,?,?,?)}";
		ArrayList<Model> modelArray = new ArrayList<Model>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, make);
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			while (rs.next())
			{
				Model model = new Model();
				model.setMakeCode(rs.getString(1));
				model.setSubMakeCode(rs.getString(2));
				model.setSubMakeDesc(rs.getString(3));
				model.setSeatingCapacity(rs.getBigDecimal(4));
				model.setShapeCode(rs.getString(5));
				model.setVehicleTypeDesc(rs.getString(8));
				model.setShapeDesc(rs.getString(9));
				modelArray.add(model);
			}
			arrayResponseModel.setDataArray(modelArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setErrorMessage(callableStatement.getString(7));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getModel :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getFuleType(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_FUELTYPE(?,?,?,?,?,?)}";
		ArrayList<FuelType> fuelArray = new ArrayList<FuelType>();
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
			while (rs.next())
			{
				FuelType fuelType = new FuelType();
				fuelType.setFuelCode(rs.getString(1));
				fuelType.setFuelDesc(rs.getString(2));
				fuelArray.add(fuelType);
			}
			arrayResponseModel.setDataArray(fuelArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getFuleType :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getPurpose(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_PURPOSE(?,?,?,?,?,?)}";
		ArrayList<Purpose> purposeArray = new ArrayList<Purpose>();
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
			while (rs.next())
			{
				Purpose purpose = new Purpose();
				purpose.setPurposeCode(rs.getString(1));
				purpose.setPurposeDesc(rs.getString(2));
				purposeArray.add(purpose);
			}
			arrayResponseModel.setDataArray(purposeArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));

		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getPurpose :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getShape(BigDecimal languageId)
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SHAPES(?,?,?,?,?,?)}";
		ArrayList<Shape> shapeArray = new ArrayList<Shape>();
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
			while (rs.next())
			{
				Shape shape = new Shape();
				shape.setShapeCode(rs.getString(1));
				shape.setShapeDesc(rs.getString(2));
				shapeArray.add(shape);
			}
			arrayResponseModel.setDataArray(shapeArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getShape :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getColour(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COLORS(?,?,?,?,?,?)}";
		ArrayList<Colour> colourArray = new ArrayList<Colour>();
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
			while (rs.next())
			{
				Colour colour = new Colour();
				colour.setColourCode(rs.getString(1));
				colour.setColourDesc(rs.getString(2));
				colourArray.add(colour);
			}
			arrayResponseModel.setDataArray(colourArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getColour :: exception :" + e);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getVehicleCondition(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_VEHCONDITION(?,?,?,?,?,?)}";
		ArrayList<VehicleCondition> vehicleConditionArray = new ArrayList<VehicleCondition>();
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
			while (rs.next())
			{
				VehicleCondition vehicleCondition = new VehicleCondition();
				vehicleCondition.setVehicleConditionCode(rs.getString(1));
				vehicleCondition.setVehicleConditionDesc(rs.getString(2));
				vehicleConditionArray.add(vehicleCondition);
			}
			arrayResponseModel.setDataArray(vehicleConditionArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getVehicleCondition :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getMaxVehicleAgeAllowed()
	{
		getConnection();
		Statement statement = null;
		int ageAllowed = 0;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		ArrayList<CodeDesc> maxVehicleAgeArray = new ArrayList<CodeDesc>();
		String query = "SELECT A.MAX_ALLOWED_VEHICLE_AGE FROM IRB_V_ONLMAX_VEHAGE A";
		try
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				ageAllowed = rs.getInt("MAX_ALLOWED_VEHICLE_AGE");
			}
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			//int allowedAgeIsTillNextYear = year + 1;
			int allowedAgeIsTillNextYear = year; // Suggested By Ashok Sir After Production

			for (int i = ageAllowed; i > 0; i--)
			{
				CodeDesc codeDesc = new CodeDesc();
				codeDesc.setCode("" + (allowedAgeIsTillNextYear));
				codeDesc.setDesc("" + (allowedAgeIsTillNextYear));
				maxVehicleAgeArray.add(codeDesc);
				allowedAgeIsTillNextYear--;
			}
			arrayResponseModel.setDataArray(maxVehicleAgeArray);
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getMaxVehicleAgeAllowed :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(statement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getPolicyDuration()
	{
		getConnection();
		Statement statement = null;
		int maxAllowedYear = 0;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		ArrayList<CodeDesc> maxPolicyDuration = new ArrayList<CodeDesc>();
		String query = "SELECT MAX_ALLOWED_YEAR FROM IRB_V_ONLMAX_POLPERIOD";
		try
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				maxAllowedYear = rs.getInt("MAX_ALLOWED_YEAR");
			}
			for (int i = 1; i <= maxAllowedYear; i++)
			{
				CodeDesc codeDesc = new CodeDesc();
				codeDesc.setCode("" + i);
				codeDesc.setDesc("" + i);
				maxPolicyDuration.add(codeDesc);
			}
			arrayResponseModel.setDataArray(maxPolicyDuration);
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getPolicyDuration :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(statement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getAppVehicleDetails(BigDecimal appSeqNumber,BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_APPLVEH_DTLS(?,?,?,?,?,?,?)}";
		ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = new ArrayList<VehicleDetailsGetModel>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			while (rs.next())
			{
				VehicleDetailsGetModel vehicleDetailsModel = new VehicleDetailsGetModel();
				vehicleDetailsModel.setApplicationDate(DateFormats.uiFormattedDate(rs.getDate(1)));
				vehicleDetailsModel.setApplicationType(rs.getString(2));
				vehicleDetailsModel.setDocCategory(rs.getString(3));
				vehicleDetailsModel.setCustSeqNumber(rs.getBigDecimal(4));
				vehicleDetailsModel.setPolicyPeriod(rs.getBigDecimal(5));
				vehicleDetailsModel.setUserSeqNumber(rs.getBigDecimal(6));
				vehicleDetailsModel.setAppStatus(rs.getString(7));
				vehicleDetailsModel.setCreStage(rs.getString(8));
				vehicleDetailsModel.setOldDocNumber(rs.getBigDecimal(9));
				vehicleDetailsModel.setRefDocNumber(rs.getBigDecimal(10));
				vehicleDetailsModel.setOnLineLoccd(rs.getString(11));
				vehicleDetailsModel.setQuoteSeqNumber(rs.getBigDecimal(12));
				vehicleDetailsModel.setVerNumber(rs.getBigDecimal(13));
				vehicleDetailsModel.setVehSrNumber(rs.getBigDecimal(14));
				vehicleDetailsModel.setMakeCode(rs.getString(15));
				vehicleDetailsModel.setMakeDesc(rs.getString(16));
				vehicleDetailsModel.setSubMakeCode(rs.getString(17));
				vehicleDetailsModel.setSubMakeDesc(rs.getString(18));
				vehicleDetailsModel.setKtNumber(rs.getString(19));
				vehicleDetailsModel.setChasis(rs.getString(20));
				vehicleDetailsModel.setModelNumber(rs.getBigDecimal(21));
				vehicleDetailsModel.setVehicleConditionCode(rs.getString(22));
				vehicleDetailsModel.setVehicleConditionDesc(rs.getString(23));
				vehicleDetailsModel.setPurposeCode(rs.getString(24));
				vehicleDetailsModel.setPurposeDesc(rs.getString(25));
				vehicleDetailsModel.setShapeCode(rs.getString(26));
				vehicleDetailsModel.setShapeDesc(rs.getString(27));
				vehicleDetailsModel.setColourCode(rs.getString(28));
				vehicleDetailsModel.setColourDesc(rs.getString(29));
				vehicleDetailsModel.setFuelCode(rs.getString(30));
				vehicleDetailsModel.setFuelDesc(rs.getString(31));
				vehicleDetailsModel.setNoPass(rs.getBigDecimal(32));
				vehicleDetailsModel.setReplacementType(rs.getString(33));
				vehicleDetailsModel.setReplacementTypeDesc(rs.getString(34));
				vehicleDetailsModel.setMaxInsmat(Utility.round(rs.getBigDecimal(35), metaService.getTenantProfile().getDecplc()));
				vehicleDetailsModel.setVehicleTypeDesc(rs.getString(36));
				vehicleDetailsModel.setPolicyStartDate(DateFormats.uiFormattedDate(rs.getDate(37)));
				//logger.info(TAG + " getAppVehicleDetails :: vehicleDetailsModel :" + vehicleDetailsModel.toString());
				vehicleDetailsArray.add(vehicleDetailsModel);
			}
			arrayResponseModel.setDataArray(vehicleDetailsArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setErrorMessage(callableStatement.getString(7));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getAppVehicleDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public VehicleDetailsHeaderModel setVehicleDetailsHeader(BigDecimal appSeqNumber, VehicleDetails vehicleDetails, BigDecimal oldDocNumber , String civilId , BigDecimal custSeqNum , BigDecimal userSeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		VehicleDetailsHeaderModel vehicleDetailsHeaderModel = new VehicleDetailsHeaderModel();
		String callProcedure = "{call IRB_INSUPD_APPLICATION_HEADER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			if (null == oldDocNumber)// Suggested By Ashok
			{
				callableStatement.setString(4, HardCodedValues.NEW_POLICY);
			}
			else
			{
				callableStatement.setString(4, HardCodedValues.REN_POLICY);
			}
			callableStatement.setString(5, HardCodedValues.DOCATT);
			callableStatement.setBigDecimal(6, custSeqNum);
			callableStatement.setBigDecimal(7, vehicleDetails.getPolicyDuration());
			callableStatement.setBigDecimal(8, oldDocNumber);
			callableStatement.setBigDecimal(9, userSeqNum);
			callableStatement.setString(10, HardCodedValues.ONLINE_LOC_CODE);
			callableStatement.setString(11, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(12, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(13, civilId);
			callableStatement.registerOutParameter(3, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			vehicleDetailsHeaderModel.setErrorCode(callableStatement.getString(14));
			vehicleDetailsHeaderModel.setErrorMessage(callableStatement.getString(15));
			if (callableStatement.getString(3) != null)
			{
				vehicleDetailsHeaderModel.setAppSeqNumber(callableStatement.getBigDecimal(3));
			}
		}
		catch (Exception e)
		{
			vehicleDetailsHeaderModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			vehicleDetailsHeaderModel.setErrorMessage(e.toString());
			logger.info(TAG+"setVehicleDetailsHeader :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return vehicleDetailsHeaderModel;
	}

	public VehicleDetailsUpdateModel insUpdateVehicleDetails(BigDecimal appSeqNumber, VehicleDetails vehicleDetails, String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		VehicleDetailsUpdateModel vehicleDetailsUpdateModel = new VehicleDetailsUpdateModel();
		String callProcedure = "{call IRB_INSUPD_VEHDTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setString(4, vehicleDetails.getMakeCode());
			callableStatement.setString(5, vehicleDetails.getSubMakeCode());
			callableStatement.setString(6, vehicleDetails.getKtNumber());
			callableStatement.setString(7, vehicleDetails.getChasis());
			callableStatement.setBigDecimal(8, vehicleDetails.getModelYear());
			callableStatement.setString(9, vehicleDetails.getVehicleConditionCode());
			callableStatement.setString(10, vehicleDetails.getPurposeCode());
			callableStatement.setString(11, vehicleDetails.getShapeCode());
			callableStatement.setString(12, vehicleDetails.getColourCode());
			callableStatement.setString(13, vehicleDetails.getFuelCode());
			callableStatement.setString(14, null);// Hard Coded As Per Ashok Sir
			callableStatement.setBigDecimal(15, vehicleDetails.getSeatingCapacity());
			callableStatement.setBigDecimal(16, null);// As Per AshokSir
			callableStatement.setBigDecimal(17, null);// As Per AshokSir
			callableStatement.setString(18, null);// Hard Coded As Per Ashok Sir
			callableStatement.setBigDecimal(19, Utility.round(vehicleDetails.getVehicleValue(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setString(20, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(21, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(22, civilId);
			callableStatement.setDate(23, DateFormats.setDbSqlFormatDate(vehicleDetails.getPolicyStartDate().toString()));
			callableStatement.registerOutParameter(24, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			vehicleDetailsUpdateModel.setErrorCode(callableStatement.getString(24));
			vehicleDetailsUpdateModel.setErrorMessage(callableStatement.getString(25));
		}
		catch (Exception e)
		{
			vehicleDetailsUpdateModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			vehicleDetailsUpdateModel.setErrorMessage(e.toString());
			logger.info(TAG+"insUpdateVehicleDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return vehicleDetailsUpdateModel;
	}

	public ArrayResponseModel getImageMetaData(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ONLINE_DOCS(?,?,?,?,?,?)}";
		ArrayList<HashMap<String, String>> imageMetaInfoArray = new ArrayList<HashMap<String, String>>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		HashMap<String, String> imageMetaData = new HashMap<String, String>();

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
				imageMetaData.put(rs.getString(1), rs.getString(3));
			}
			imageMetaInfoArray.add(imageMetaData);
			arrayResponseModel.setDataArray(imageMetaInfoArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(4));
			arrayResponseModel.setErrorMessage(callableStatement.getString(5));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getAppVehicleDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getImageDetails(BigDecimal appSeqNumber, BigDecimal languageId,String vehicleConditionCode)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ONLINE_DOCS(?,?,?,?,?,?)}";
		ArrayList<ImageDetails> imageMetaInfoArray = new ArrayList<ImageDetails>();
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

			while (rs.next())
			{
				ImageDetails imageDetails = new ImageDetails();
				imageDetails.setDocTypeCode(rs.getString(1));
				imageDetails.setDocTypeDesc(rs.getString(2));
				
				ArrayResponseModel arrayResponseImageStatus = imageUploadedStatus(appSeqNumber, rs.getString(1));
				if(null != arrayResponseImageStatus.getErrorCode())
				{
					arrayResponseModel.setErrorCode(arrayResponseImageStatus.getErrorCode());
					arrayResponseModel.setErrorMessage(arrayResponseImageStatus.getErrorMessage());
					return arrayResponseModel;
				}
				
				ImageStatus imageStatus = (ImageStatus)arrayResponseImageStatus.getObject();
				if(null != rs.getString(3) && !rs.getString(3).equals(""))//Suggested By Ashok Sir
				{
					imageDetails.setIsImageMandatory(rs.getString(3));
				}
				else
				{
					imageDetails.setIsImageMandatory("N");
				}
				imageDetails.setDisplayOrder(rs.getBigDecimal(4));
				imageDetails.setStatus(rs.getString(5));
				imageDetails.setImageSubmittedDate(imageStatus.getImageDate());
				imageDetails.setDisplayVehicleCond(rs.getString(6));
				
				if (null != imageStatus.getDocSeqNumber() && !imageStatus.getDocSeqNumber().toString().equals("0") && !imageStatus.getDocSeqNumber().toString().equals(""))
				{
					imageDetails.setDocSeqNumber(imageStatus.getDocSeqNumber());
				}
				else
				{
					imageDetails.setDocSeqNumber(null);
				}
				
				logger.info("RequestQuoteDao :: getImageDetails :: imageDetails :" + imageDetails.toString());
				
				imageMetaInfoArray.add(imageDetails);
			}
			int i;
			
			if (Constants.NEWCAR.equalsIgnoreCase(vehicleConditionCode)) {
				for (i=0;i<imageMetaInfoArray.size();i++) {
					logger.info("Vehicle cond code is "+imageMetaInfoArray.get(i).getDocTypeCode());
					if (imageMetaInfoArray.get(i).getDisplayVehicleCond().equalsIgnoreCase(Constants.DISPLAY_VEHICLE_USED)) {
						imageMetaInfoArray.remove(i);
						i=-1;

					}
				}
			} else if("U".equalsIgnoreCase(vehicleConditionCode)){
				for (i=0;i<imageMetaInfoArray.size();i++) {
					if (imageMetaInfoArray.get(i).getDisplayVehicleCond().equalsIgnoreCase(Constants.DISPLAY_VEHICLE_NEW)) {
						imageMetaInfoArray.remove(i);

					}
				}
			}
			arrayResponseModel.setDataArray(imageMetaInfoArray);
			logger.info("RequestQuoteDao :: getImageDetails :: setErrorCode :" + callableStatement.getString(4));
			arrayResponseModel.setErrorCode(callableStatement.getString(4));
			logger.info("RequestQuoteDao :: getImageDetails :: setErrorMessage :" + callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(5));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getAppVehicleDetails :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel imageUploadedStatus(BigDecimal appSeqNumber, String docType)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ImageStatus imageStatus = new ImageStatus();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		
		String callProcedure = "{call IRB_CHECK_IMAGE_UPLOADED(?,?,?,?,?,?)}";
		String epochDate = null;
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setString(4, docType);
			callableStatement.registerOutParameter(5, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(6, java.sql.Types.TIMESTAMP);
			callableStatement.executeUpdate();

			if (null != callableStatement.getTimestamp(6))
			{
				epochDate = DateFormats.convertTimeStampToEpoc(callableStatement.getTimestamp(6).toString());
			}
			imageStatus.setDocSeqNumber(callableStatement.getBigDecimal(5));
			imageStatus.setImageDate(epochDate);

			arrayResponseModel.setObject(imageStatus);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public DownloadImageModel downloadVehicleImage(BigDecimal docSeqNumber)
	{
		getConnection();
		Blob imageBlob = null;
		String imageType;
		CallableStatement callableStatement = null;
		byte[] blobAsBytes = null;
		DownloadImageModel downloadImageModel = new DownloadImageModel();
		String callProcedure = "{call IRB_GET_UPLOADED_IMAGE(?,?,?,?,?,?,?)}";

		try
		{

			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, docSeqNumber);
			callableStatement.registerOutParameter(4, OracleTypes.BLOB);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			imageBlob = callableStatement.getBlob(4);
			imageType = callableStatement.getString(5);

			if (null != imageBlob)
			{
				int blobLength = (int) imageBlob.length();
				blobAsBytes = imageBlob.getBytes(1, blobLength);
				downloadImageModel.setImageByteArray(blobAsBytes);
				imageBlob.free();
			}
			else
			{
				downloadImageModel.setImageByteArray(null);
				return null;
			}
			downloadImageModel.setImageType(imageType);
		
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return downloadImageModel;
	}

	public ImageModel uploadVehicleImage(MultipartFile file, BigDecimal appSeqNumber, String docTypeCode, BigDecimal docSeqNumber , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_UPLOAD_IMAGE(?,?,?,?,?,?,?,?,?,?,?,?)}";
		ImageModel imageModel = new ImageModel();

		try
		{
			InputStream inputStream = file.getInputStream();
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setString(4, docTypeCode);
			callableStatement.setBigDecimal(5, docSeqNumber);
			callableStatement.setBlob(6, inputStream, inputStream.available());
			
			String imageType = file.getContentType().toString();
			logger.info(TAG + " uploadVehicleImage :: imageType :" + imageType);
			
			if (imageType.contains("image")) 
			{
				callableStatement.setString(7, file.getContentType().toString());
			} 
			else
			{
				String extension = getFileExtension(file.getOriginalFilename());
				logger.info(TAG + " uploadVehicleImage :: extension 3 :" + extension);
				if(extension.equalsIgnoreCase("jpeg"))
				{
					logger.info(TAG + " uploadVehicleImage :: jpeg");
					callableStatement.setString(7, "image/jpeg");
				}
				else if(extension.equalsIgnoreCase("jpg"))
				{
					logger.info(TAG + " uploadVehicleImage :: jpg");
					callableStatement.setString(7, "image/jpg");
				}
				else if(extension.equalsIgnoreCase("png"))
				{
					logger.info(TAG + " uploadVehicleImage :: png");
					callableStatement.setString(7, "image/png");
				}
			}
			
			
			callableStatement.setString(8, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(9, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(10, civilId);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.NUMERIC);
			callableStatement.executeUpdate();
			
			
			logger.info(TAG + " uploadVehicleImage :: setDocSeqNumber :" +callableStatement.getBigDecimal(5));
			imageModel.setDocSeqNumber(callableStatement.getBigDecimal(5));
			logger.info(TAG + " uploadVehicleImage :: setErrorCode :" +callableStatement.getString(11));
			imageModel.setErrorCode(callableStatement.getString(11));
			logger.info(TAG + " uploadVehicleImage :: setErrorMessage :" +callableStatement.getString(12));
			imageModel.setErrorMessage(callableStatement.getString(12));
		}
		catch (Exception e)
		{
			imageModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			imageModel.setErrorMessage(e.toString());
			logger.info(TAG+"uploadVehicleImage :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				file.getInputStream().close();
				CloseConnection(callableStatement, connection);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return imageModel;
	}

	public ArrayResponseModel getInsuranceCompanyDetails(BigDecimal appSeqNumber, BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ONLINE_INSCOMPANY(?,?,?,?,?,?)}";
		ArrayList<InsuranceCompanyDetails> insuranceCompanyArray = new ArrayList<InsuranceCompanyDetails>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		boolean defaultInsuCompSelected = true;
		
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
				InsuranceCompanyDetails insuranceCompanyDetails = new InsuranceCompanyDetails();
				insuranceCompanyDetails.setCompanyCode(rs.getBigDecimal(3));
				insuranceCompanyDetails.setCompanyName(rs.getString(5));
				insuranceCompanyDetails.setCompanyShortCode(rs.getString(15));
				String insuranceSelected = appSelectedProvider(rs.getBigDecimal(3), appSeqNumber);
				if(insuranceSelected.equalsIgnoreCase("Y"))
				{
					defaultInsuCompSelected = false;
				}
				insuranceCompanyDetails.setInsuranceSelected(insuranceSelected);
				insuranceCompanyArray.add(insuranceCompanyDetails);
			}
			
			InsuranceCompanyDetails amibCompanyDetails = new InsuranceCompanyDetails();
			amibCompanyDetails.setCompanyCode(new BigDecimal(HardCodedValues.COMPANY_CODE));
			amibCompanyDetails.setCompanyName(HardCodedValues.COMPANY_NAME);
			amibCompanyDetails.setCompanyShortCode(HardCodedValues.COMPANY_SHORT_CODE);
			if(defaultInsuCompSelected)
			{
				amibCompanyDetails.setInsuranceSelected("Y");
			}
			else
			{
				amibCompanyDetails.setInsuranceSelected("N");
			}
			insuranceCompanyArray.add(amibCompanyDetails);
			
			
			arrayResponseModel.setDataArray(insuranceCompanyArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(5));
			arrayResponseModel.setErrorMessage(callableStatement.getString(6));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"uploadVehicleImage :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public String appSelectedProvider(BigDecimal companyCode, BigDecimal appSeqNumber)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_PREFERRED_PROVIDER(?,?,?,?)}";
		String result = null;

		if (null == appSeqNumber)
		{
			return "N";
		}

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(4, appSeqNumber);
			callableStatement.setBigDecimal(5, companyCode);
			callableStatement.executeUpdate();
			result = callableStatement.getString(1);
			//logger.info(TAG + " appSelectedProvider :: result :" + result);
			if (result.toString().equalsIgnoreCase("Y"))
			{
				return "Y";
			}
			else
			{
				return "N";
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
		return "N";
	}

	public ArrayResponseModel updateInsuranceProvider(BigDecimal appSeqNumber, BigDecimal insuranceCompCode, String prefIndic , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_INSUPD_PREFERRED_PROVIDER(?,?,?,?,?,?,?,?,?,?)}";
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setBigDecimal(4, insuranceCompCode);
			callableStatement.setString(5, prefIndic);
			callableStatement.setString(6, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(7, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(8, civilId);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			arrayResponseModel.setErrorCode(callableStatement.getString(9));
			arrayResponseModel.setErrorMessage(callableStatement.getString(10));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"uploadVehicleImage :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel submitRequestQuote(BigDecimal appSeqNumber , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_SUBMIT_ONLINE_APPL(?,?,?,?,?,?,?,?)}";
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setString(4, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(5, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(6, civilId);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			arrayResponseModel.setErrorCode(callableStatement.getString(7));
			arrayResponseModel.setErrorMessage(callableStatement.getString(8));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"uploadVehicleImage :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public CustomerProfileDetailModel updateCustomerSequenceNumber(BigDecimal custSeqNumber, BigDecimal appSeqNumber , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		String callProcedure = "{call IRB_UPD_APPLHD_CUSTSEQNO(?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appSeqNumber);
			callableStatement.setBigDecimal(4, custSeqNumber);
			callableStatement.setString(5, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(6, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(7, civilId);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			customerProfileDetailModel.setErrorCode(callableStatement.getString(8));
			customerProfileDetailModel.setErrorMessage(callableStatement.getString(9));

		}
		catch (Exception e)
		{
			customerProfileDetailModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			customerProfileDetailModel.setErrorMessage(e.toString());
			logger.info(TAG+"updateCustomerSequenceNumber :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return customerProfileDetailModel;
	}

	public ArrayResponseModel getRenewPolicyVehicleDetails(BigDecimal appDocNumberDet, BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_POPULATE_RENEWAL_DTLS(?,?,?,?,?,?,?)}";
		ArrayList<VehicleDetails> vehicleDetailsArray = new ArrayList<VehicleDetails>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		BigDecimal companyCode = null;

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, appDocNumberDet);
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			while (rs.next())
			{
				companyCode = rs.getBigDecimal(5);

				VehicleDetails vehicleDetails = new VehicleDetails();
				vehicleDetails.setMakeCode(rs.getString(7));
				vehicleDetails.setSubMakeCode(rs.getString(9));
				vehicleDetails.setModelYear(rs.getBigDecimal(11));
				vehicleDetails.setShapeCode(rs.getString(12));
				vehicleDetails.setColourCode(rs.getString(14));
				vehicleDetails.setSeatingCapacity(rs.getBigDecimal(16));
				vehicleDetails.setChasis(rs.getString(17));
				vehicleDetails.setKtNumber(rs.getString(18));
				vehicleDetails.setVehicleConditionCode(rs.getString(19));
				vehicleDetails.setPurposeCode(rs.getString(21));
				vehicleDetails.setVehicleValue(rs.getBigDecimal(24));
				vehicleDetails.setFuelCode(rs.getString(25));
				vehicleDetails.setVehicleTypeDesc(rs.getString(27));
				vehicleDetails.setPolicyDuration(rs.getBigDecimal(28));
				vehicleDetails.setPolicyStartDate(DateFormats.uiFormattedDate(rs.getDate(29)));
				vehicleDetailsArray.add(vehicleDetails);
			}

			arrayResponseModel.setDataArray(vehicleDetailsArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setErrorMessage(callableStatement.getString(7));
			if (null != companyCode)
			{
				arrayResponseModel.setData(companyCode.toString());
			}
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"updateCustomerSequenceNumber :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
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

	private void CloseConnection(Statement statement, Connection connection)
	{
		try
		{
			if (statement != null)
			{
				statement.close();
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
	
	private static String getFileExtension(String fileName) 
	{
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
