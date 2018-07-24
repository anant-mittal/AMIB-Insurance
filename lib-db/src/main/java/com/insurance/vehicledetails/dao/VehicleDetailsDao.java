




package com.insurance.vehicledetails.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.insurance.vehicledetails.model.Colour;
import com.insurance.vehicledetails.model.FuelType;
import com.insurance.vehicledetails.model.Make;
import com.insurance.vehicledetails.model.Model;
import com.insurance.vehicledetails.model.Purpose;
import com.insurance.vehicledetails.model.Shape;
import oracle.jdbc.OracleTypes;

@Component
public class VehicleDetailsDao
{
	String TAG = "com.insurance.vehicledetails.dao.VehicleDetailsDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Connection connection;

	public ArrayList getMake()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_MAKES(?,?,?,?,?,?)}";
		ArrayList<Make> makeArray = new ArrayList<Make>();

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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return makeArray;
	}

	public ArrayList getModel(String make)
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SUBMAKE(?,?,?,?,?,?,?)}";
		ArrayList<Model> modelArray = new ArrayList<Model>();

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
			logger.info(TAG + " getModel :: End");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return modelArray;
	}

	public ArrayList getFuleType()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_FUELTYPE(?,?,?,?,?,?)}";
		ArrayList<FuelType> fuelArray = new ArrayList<FuelType>();

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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return fuelArray;
	}

	public ArrayList getPurpose()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_PURPOSE(?,?,?,?,?,?)}";
		ArrayList<Purpose> purposeArray = new ArrayList<Purpose>();

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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return purposeArray;
	}

	public ArrayList getShape()
	{
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_SHAPES(?,?,?,?,?,?)}";
		ArrayList<Shape> shapeArray = new ArrayList<Shape>();

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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return shapeArray;
	}

	public ArrayList getColour()
	{
		logger.info(TAG + " getColour :: ");
		
		getConnection();

		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COLORS(?,?,?,?,?,?)}";
		ArrayList<Colour> colourArray = new ArrayList<Colour>();
		
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return colourArray;
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
