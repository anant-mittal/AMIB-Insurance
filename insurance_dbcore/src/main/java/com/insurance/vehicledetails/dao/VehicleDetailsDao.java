




package com.insurance.vehicledetails.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.insurance.base.model.Business;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;
import com.insurance.user_registartion.controller.CustomerRegistrationController;
import com.insurance.vehicledetails.model.Colour;
import com.insurance.vehicledetails.model.FuelType;
import com.insurance.vehicledetails.model.Make;
import com.insurance.vehicledetails.model.Model;
import com.insurance.vehicledetails.model.Purpose;
import com.insurance.vehicledetails.model.Shape;
import com.sleepycat.je.Cursor;

import oracle.jdbc.OracleTypes;

@Repository
public class VehicleDetailsDao extends AbstractService
{
	String TAG = "com.insurance.vehicledetails.dao.VehicleDetailsDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public ApiResponse getMake()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_MAKES(?,?,?,?,?,?)}";
		List<Make> makeArray = new ArrayList<Make>();

		logger.info(TAG + " getMake ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Make make = new Make();
				make.setMakeCode(rs.getString(1));
				logger.info(TAG + " getMake :: make code :" + rs.getString(1));
				make.setMakeDesc(rs.getString(2));
				logger.info(TAG + " getMake :: make disc :" + rs.getString(2));
				makeArray.add(make);
			}

			response.getData().getValues().addAll(makeArray);
			response.getData().setType("make");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return response;
	}

	public ApiResponse getModel(String make)
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SUBMAKE(?,?,?,?,?,?,?)}";
		List<Model> modelArray = new ArrayList<Model>();

		logger.info(TAG + " getModel :: make :" + make);

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setString(3, make);
			callableStatement.setInt(4, 3);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			logger.info(TAG + " getModel :: make 1 :" + make);

			ResultSet rs = (ResultSet) callableStatement.getObject(5);

			logger.info(TAG + " getModel :: rs :" + rs);

			while (rs.next())
			{
				Model model = new Model();
				model.setModelCode(rs.getString(1));
				logger.info(TAG + " getMake :: model code :" + rs.getString(1));
				model.setModelDesc(rs.getString(2));
				logger.info(TAG + " getMake :: model disc :" + rs.getString(2));
				modelArray.add(model);
			}

			logger.info(TAG + " getModel :: modelArray is Not Empty()");
			response.getData().getValues().addAll(modelArray);
			response.getData().setType("model");
			response.setResponseStatus(ResponseStatus.OK);

			logger.info(TAG + " getModel :: End");

		}
		catch (Exception e)
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return response;
	}

	public ApiResponse getFuleType()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_FUELTYPE(?,?,?,?,?,?)}";
		List<FuelType> fuelArray = new ArrayList<FuelType>();

		logger.info(TAG + " getFuleType ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				FuelType fuelType = new FuelType();
				fuelType.setFuelCode(rs.getString(1));
				logger.info(TAG + " getFuleType :: fueltype code :" + rs.getString(1));
				fuelType.setFuelDesc(rs.getString(2));
				logger.info(TAG + " getFuleType :: fueltype disc :" + rs.getString(2));
				fuelArray.add(fuelType);
			}

			response.getData().getValues().addAll(fuelArray);
			response.getData().setType("fueltype");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return response;
	}
	
	public ApiResponse getPurpose()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_PURPOSE(?,?,?,?,?,?)}";
		List<Purpose> purposeArray = new ArrayList<Purpose>();

		logger.info(TAG + " getPurpose ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Purpose purpose = new Purpose();
				purpose.setPurposeCode(rs.getString(1));
				logger.info(TAG + " getPurpose :: purpose code :" + rs.getString(1));
				purpose.setPurposeDesc(rs.getString(2));
				logger.info(TAG + " getPurpose :: purpose disc :" + rs.getString(2));
				purposeArray.add(purpose);
			}

			response.getData().getValues().addAll(purposeArray);
			response.getData().setType("purpose");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return response;
	}
	
	
	public ApiResponse getShape()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SHAPES(?,?,?,?,?,?)}";
		List<Shape> shapeArray = new ArrayList<Shape>();

		logger.info(TAG + " getShape ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Shape shape = new Shape();
				shape.setShapeCode(rs.getString(1));
				logger.info(TAG + " getShape :: shape code :" + rs.getString(1));
				shape.setShapeDesc(rs.getString(2));
				logger.info(TAG + " getShape :: shape disc :" + rs.getString(2));
				shapeArray.add(shape);
			}

			response.getData().getValues().addAll(shapeArray);
			response.getData().setType("shape");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return response;
	}
	
	public ApiResponse getColour()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COLORS(?,?,?,?,?,?)}";
		List<Colour> colourArray = new ArrayList<Colour>();

		logger.info(TAG + " getColour ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Colour colour = new Colour();
				colour.setColourCode(rs.getString(1));
				logger.info(TAG + " getColour :: colour code :" + rs.getString(1));
				colour.setColourDesc(rs.getString(2));
				logger.info(TAG + " getColour :: colour disc :" + rs.getString(2));
				colourArray.add(colour);
			}

			response.getData().getValues().addAll(colourArray);
			response.getData().setType("colour");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return response;
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

	@Override
	public String getModelType()
	{
		return null;
	}
}
