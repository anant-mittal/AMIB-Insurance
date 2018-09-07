
package com.amx.jax.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CodeDesc;
import com.amx.jax.models.Colour;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.FuelType;
import com.amx.jax.models.ImageMandatoryResponse;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.Make;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.Model;
import com.amx.jax.models.Purpose;
import com.amx.jax.models.RequestQuoteInfo;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.models.Shape;
import com.amx.jax.models.VehicleCondition;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.models.VehicleDetailsGetModel;
import com.amx.jax.models.VehicleDetailsHeaderModel;
import com.amx.jax.models.VehicleDetailsUpdateModel;
import com.amx.jax.models.VehicleSession;
import oracle.jdbc.OracleTypes;

@Component
public class RequestQuoteDao
{
	String TAG = "com.insurance.vehicledetails.dao.VehicleDetailsDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Connection connection;

	@Autowired
	MetaData metaData;
	
	@Autowired
	VehicleSession vehicleSession;

	public IncompleteApplModel getIncompleteApplication()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_INCOMPLETE_APPL(?,?,?,?,?,?,?,?,?)}";
		IncompleteApplModel incompleteApplModel = new IncompleteApplModel();
		
		try
		{
			logger.info(TAG + " getIncompleteApplication :: metaData :" + metaData.toString());
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, metaData.getUserType());
			callableStatement.setString(4, metaData.getCivilId());
			callableStatement.setBigDecimal(5, metaData.getCustomerSequenceNumber());
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			incompleteApplModel.setAppSeqNumber(callableStatement.getBigDecimal(6));
			logger.info(TAG + " getIncompleteApplication :: callableStatement.getBigDecimal(6) :" + callableStatement.getBigDecimal(6));
			incompleteApplModel.setAppStage(callableStatement.getString(7));
			incompleteApplModel.setErrorCode(callableStatement.getString(8));
			incompleteApplModel.setErrorMessage(callableStatement.getString(9));

			logger.info(TAG + " getIncompleteApplication :: callableStatement.getBigDecimal(6) :" + callableStatement.getBigDecimal(6));
			
			if (callableStatement.getString(8) == null)
			{
				incompleteApplModel.setStatus(true);
			}
			else
			{
				incompleteApplModel.setStatus(false);
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
		return incompleteApplModel;
	}
	
	public ArrayResponseModel getMake()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_MAKES(?,?,?,?,?,?)}";
		ArrayList<Make> makeArray = new ArrayList<Make>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
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
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getModel(String make)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SUBMAKE(?,?,?,?,?,?,?)}";
		ArrayList<Model> modelArray = new ArrayList<Model>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, make);
			callableStatement.setBigDecimal(4, metaData.getLanguageId());
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
				logger.info(TAG + " getModel :: model :" + model.toString());
				modelArray.add(model);
			}
			arrayResponseModel.setDataArray(modelArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setErrorMessage(callableStatement.getString(7));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getFuleType()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_FUELTYPE(?,?,?,?,?,?)}";
		ArrayList<FuelType> fuelArray = new ArrayList<FuelType>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getPurpose()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_PURPOSE(?,?,?,?,?,?)}";
		ArrayList<Purpose> purposeArray = new ArrayList<Purpose>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getShape()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SHAPES(?,?,?,?,?,?)}";
		ArrayList<Shape> shapeArray = new ArrayList<Shape>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayResponseModel getColour()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COLORS(?,?,?,?,?,?)}";
		ArrayList<Colour> colourArray = new ArrayList<Colour>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
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
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public ArrayResponseModel getVehicleCondition()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_VEHCONDITION(?,?,?,?,?,?)}";
		ArrayList<VehicleCondition> vehicleConditionArray = new ArrayList<VehicleCondition>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	public ArrayList getMaxVehicleAgeAllowed()
	{
		getConnection();
		Statement statement = null;
		int ageAllowed = 0;
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
			int allowedAgeIsTillNextYear = year + 1;

			for (int i = ageAllowed; i > 0; i--)
			{
				CodeDesc codeDesc = new CodeDesc();
				codeDesc.setCode("" + (allowedAgeIsTillNextYear));
				codeDesc.setDesc("" + (allowedAgeIsTillNextYear));
				maxVehicleAgeArray.add(codeDesc);
				allowedAgeIsTillNextYear--;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(statement, connection);
		}
		return maxVehicleAgeArray;
	}

	public ArrayList getPolicyDuration()
	{
		getConnection();
		Statement statement = null;
		int maxAllowedYear = 0;
		ArrayList<CodeDesc> maxVehicleAgeArray = new ArrayList<CodeDesc>();
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
				maxVehicleAgeArray.add(codeDesc);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(statement, connection);
		}
		return maxVehicleAgeArray;
	}

	public ArrayResponseModel getAppVehicleDetails(RequestQuoteModel requestQuoteModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_APPLVEH_DTLS(?,?,?,?,?,?,?)}";
		ArrayList<VehicleDetailsGetModel> vehicleDetailsArray = new ArrayList<VehicleDetailsGetModel>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();
		try
		{
			requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
			logger.info(TAG + " getAppVehicleDetails :: getAppSeqNumber :" + requestQuoteInfo.getAppSeqNumber());
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, requestQuoteInfo.getAppSeqNumber());
			callableStatement.setBigDecimal(4, metaData.getLanguageId());
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
				vehicleDetailsModel.setMaxInsmat(rs.getBigDecimal(35));
				vehicleDetailsModel.setVehicleTypeDesc(rs.getString(36));
				vehicleDetailsArray.add(vehicleDetailsModel);
			}
			arrayResponseModel.setDataArray(vehicleDetailsArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(6));
			arrayResponseModel.setErrorMessage(callableStatement.getString(7));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}
	
	public VehicleDetailsHeaderModel setVehicleDetailsHeader(RequestQuoteModel requestQuoteModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		VehicleDetails vehicleDetails = null;
		VehicleDetailsHeaderModel vehicleDetailsHeaderModel = new VehicleDetailsHeaderModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();
		String callProcedure = "{call IRB_INSUPD_APPLICATION_HEADER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try
		{
			requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
			logger.info(TAG + " getAppVehicleDetails :: getAppSeqNumber :" + requestQuoteInfo.getAppSeqNumber());
			
			vehicleDetails = requestQuoteModel.getVehicleDetails();
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, requestQuoteInfo.getAppSeqNumber());
			if(null == requestQuoteInfo.getDocNumber())//Suggested By Ashok Sir
			{
				callableStatement.setString(4, HardCodedValues.NEW_POLICY);
			}
			else
			{
				callableStatement.setString(4, HardCodedValues.REN_POLICY);
			}
			callableStatement.setString(5, HardCodedValues.DOCATT);
			callableStatement.setBigDecimal(6, metaData.getCustomerSequenceNumber());
			callableStatement.setBigDecimal(7, vehicleDetails.getPolicyDuration());
			callableStatement.setBigDecimal(8, requestQuoteInfo.getDocNumber());
			callableStatement.setBigDecimal(9, metaData.getUserSequenceNumber());
			callableStatement.setString(10, HardCodedValues.ONLINE_LOC_CODE);
			callableStatement.setString(11, metaData.getDeviceType());
			callableStatement.setString(12, metaData.getDeviceId());
			callableStatement.setString(13, metaData.getCivilId());
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return vehicleDetailsHeaderModel;
	}
	
	public VehicleDetailsUpdateModel insUpdateVehicleDetails(RequestQuoteModel requestQuoteModel)
	{
		getConnection();
		CallableStatement callableStatement = null;
		VehicleDetails vehicleDetails = null;
		VehicleDetailsUpdateModel vehicleDetailsUpdateModel = new VehicleDetailsUpdateModel();
		RequestQuoteInfo requestQuoteInfo = new RequestQuoteInfo();
		String callProcedure = "{call IRB_INSUPD_VEHDTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		logger.info(TAG + " insUpdateVehicleDetails :: requestQuoteModel :" + requestQuoteModel.toString());
		logger.info(TAG + " insUpdateVehicleDetails :: metaData :" + metaData.toString());

		try
		{
			requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
			logger.info(TAG + " getAppVehicleDetails :: getAppSeqNumber :" + requestQuoteInfo.getAppSeqNumber());
			
			vehicleDetails = requestQuoteModel.getVehicleDetails();
			logger.info(TAG + " insUpdateVehicleDetails :: vehicleDetails :" + vehicleDetails.toString());
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, requestQuoteInfo.getAppSeqNumber());
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
			callableStatement.setString(14, null);//Hard Coded As Per Ashok Sir
			callableStatement.setBigDecimal(15, vehicleDetails.getSeatingCapacity());
			callableStatement.setBigDecimal(16, null);//Hard Coded As Per Ashok Sir
			callableStatement.setBigDecimal(17, null);//Hard Coded As Per Ashok Sir
			callableStatement.setString(18, null);//Hard Coded As Per Ashok Sir
			callableStatement.setBigDecimal(19, vehicleDetails.getVehicleValue());//Hard Coded As Per Ashok Sir
			callableStatement.setString(20, metaData.getDeviceType());
			callableStatement.setString(21, metaData.getDeviceId());
			callableStatement.setString(22, metaData.getCivilId());
			callableStatement.registerOutParameter(23, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(24, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			vehicleDetailsUpdateModel.setErrorCode(callableStatement.getString(23));
			vehicleDetailsUpdateModel.setErrorMessage(callableStatement.getString(24));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return vehicleDetailsUpdateModel;
	}
	
	public ArrayResponseModel getMandatoryImage()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ONLINE_DOCS(?,?,?,?,?,?)}";
		ArrayList<ImageMandatoryResponse> imageMandatoryArray = new ArrayList<ImageMandatoryResponse>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				ImageMandatoryResponse imageMandatoryModel = new ImageMandatoryResponse();
				imageMandatoryModel.setDocType(rs.getString(1));
				imageMandatoryModel.setDeCode(rs.getString(2));
				imageMandatoryModel.setRequiredCheck(rs.getString(3));
				imageMandatoryModel.setDispOrder(rs.getString(4));
				imageMandatoryModel.setDocStatus(rs.getString(5));
				imageMandatoryArray.add(imageMandatoryModel);
			}
			arrayResponseModel.setDataArray(imageMandatoryArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(4));
			arrayResponseModel.setErrorMessage(callableStatement.getString(5));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}
	
	
	public String checkIfImageAlreadyUploaded(String docType)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_IMAGE_UPLOADED(?,?,?,?)}";
		String result = null;

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setBigDecimal(2, metaData.getCountryId());
			callableStatement.setBigDecimal(3, metaData.getCompCd());
			//callableStatement.setBigDecimal(4, vehicleSession.getAppSeqNumber());
			callableStatement.setString(5, docType);
			callableStatement.executeUpdate();
			result = callableStatement.getString(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return result;
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
}
