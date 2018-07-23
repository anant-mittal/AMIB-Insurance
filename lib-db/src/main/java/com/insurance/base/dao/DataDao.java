




package com.insurance.base.dao;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import oracle.jdbc.OracleTypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import com.insurance.base.model.Business;
import com.insurance.base.model.Nationality;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DataDao
{
	String TAG = "com.insurance.base.dao.DataDao";

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public ArrayList getBusinessData()
	{
		System.out.println("\n\n===========================================BUSINESS==========================================");

		getConnection();

		ArrayList<Business> businessDataArray = new ArrayList<Business>();
		CallableStatement callableStatement = null;

		String getDBUSERByUserIdSql = "{call IRB_GET_BUSINESS(?,?)}";

		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.execute();
			ResultSet rs = (ResultSet) callableStatement.getObject(1);

			while (rs.next())
			{
				Business business = new Business();
				business.setCntrycd(rs.getInt(1));
				business.setCompcd(rs.getInt(2));
				business.setNationality(rs.getString(3));
				business.setDesc(rs.getString(4));
				System.out.println("DataDao :: getBusinessData :: rs.getString(3) :" + rs.getString(3));
				businessDataArray.add(business);
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return businessDataArray;
	}

	public ArrayList getBusinessDataDemo()
	{
		System.out.println("\n\n===========================================BUSINESS==========================================");

		getConnection();

		ArrayList<Business> businessDataArray = new ArrayList<Business>();
		CallableStatement callableStatement = null;

		String getDBUSERByUserIdSql = "{call IRB_GET_BUSINESS(?,?)}";

		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.execute();
			ResultSet rs = (ResultSet) callableStatement.getObject(1);

			while (rs.next())
			{
				Business business = new Business();
				business.setCntrycd(rs.getInt(1));
				business.setCompcd(rs.getInt(2));
				business.setNationality(rs.getString(3));
				business.setDesc((rs.getString(4)));
				System.out.println("DataDao :: getBusinessData :: rs.getString(3) :" + rs.getString(3));
				businessDataArray.add(business);
			}

			System.out.println("DataDao :: getBusinessData :: businessDataArray :" + businessDataArray);

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		
		return businessDataArray;
	}

	public ArrayList getNationalityData()
	{
		System.out.println("\n\n===========================================Nationality==========================================");

		getConnection();

		ArrayList<Nationality> nationalityDataArray = new ArrayList<Nationality>();
		CallableStatement callableStatement = null;

		String getDBUSERByUserIdSql = "{call IRB_GET_NATIONALITIES(?,?)}";

		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.execute();
			ResultSet rs = (ResultSet) callableStatement.getObject(1);

			while (rs.next())
			{
				Nationality nationality = new Nationality();
				nationality.setNationality(rs.getString(1));
				nationality.setDescription(rs.getString(2));
				nationalityDataArray.add(nationality);
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
		return nationalityDataArray;
	}

	public void fileUpload(MultipartFile aFile)
	{
		System.out.println("\n\n===========================================fileUpload==========================================");

		CallableStatement callableStatement = null;

		getConnection();

		try
		{

			PreparedStatement ps = connection.prepareStatement("insert into OI_INSCOMP_LOGO values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			ps.setInt(1, 91);
			ps.setInt(2, 01);
			ps.setInt(3, 100101);
			ps.setBinaryStream(4, aFile.getInputStream());
			ps.setString(5, "C");
			ps.setString(6, null);
			ps.setString(7, "abhi1427");
			ps.setDate(8, getCurrentDate());
			ps.setString(9, "IRBF067");
			ps.setString(10, null);
			ps.setString(11, null);
			ps.setString(12, null);
			ps.setString(13, null);
			
			ps.executeUpdate();
			connection.commit();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
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
