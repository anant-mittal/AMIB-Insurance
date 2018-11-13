package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceiptModel;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.MetaData;

@Repository
public class PayMentDao
{
	String TAG = "com.amx.jax.dao.PayMentDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayMentDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;

	
	Connection connection;
	
	
	public PaymentDetails insertPaymentDetals(PaymentDetails insertPaymentDetails , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_INSERT_PAYMENT_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, insertPaymentDetails.getAppSeqNum());
			callableStatement.setBigDecimal(4, insertPaymentDetails.getQuoteSeqNum());
			callableStatement.setBigDecimal(5, insertPaymentDetails.getQouteVerNum());
			callableStatement.setBigDecimal(6, insertPaymentDetails.getCustSeqNum());
			callableStatement.setString(7, insertPaymentDetails.getPaymentMethod());//Suggested By Ashok Sir
			callableStatement.setBigDecimal(8, insertPaymentDetails.getPaymentAmount());
			//callableStatement.setString(9, insertPaymentDetails.getPaymentId());
			//callableStatement.setString(10, insertPaymentDetails.getPaymentToken());
			callableStatement.setString(9, metaData.getDeviceType());
			callableStatement.setString(10, metaData.getDeviceId());
			callableStatement.setString(11, civilId);
			callableStatement.registerOutParameter(12, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			insertPaymentDetails.setPaySeqNum(callableStatement.getBigDecimal(12));
			insertPaymentDetails.setErrorCode(callableStatement.getString(13));
			insertPaymentDetails.setErrorMessage(callableStatement.getString(14));

			logger.info(TAG + " insertPaymentDetals :: Error Code :" + callableStatement.getString(13));
			logger.info(TAG + " insertPaymentDetals :: Error Msg  :" + callableStatement.getString(14));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return insertPaymentDetails;
	}
	
	
	
	
	
	
	
	public PaymentDetails updatePaymentDetals(PaymentDetails insertPaymentDetails , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_UPDATE_PAYMENT_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try
		{
			logger.info(TAG + " updatePaymentDetals :: getPaySeqNum :" + insertPaymentDetails.getPaySeqNum());
			logger.info(TAG + " updatePaymentDetals :: getPaymentId :" + insertPaymentDetails.getPaymentId());
			logger.info(TAG + " updatePaymentDetals :: getApprovalNo :" + insertPaymentDetails.getApprovalNo());
			logger.info(TAG + " updatePaymentDetals :: getApprovalDate :" + insertPaymentDetails.getApprovalDate());
			logger.info(TAG + " updatePaymentDetals :: getResultCd :" + insertPaymentDetails.getResultCd());
			logger.info(TAG + " updatePaymentDetals :: getTransId :" + insertPaymentDetails.getTransId());
			logger.info(TAG + " updatePaymentDetals :: getRefId :" + insertPaymentDetails.getRefId());
			logger.info(TAG + " updatePaymentDetals :: getPaymentToken :" + insertPaymentDetails.getPaymentToken());
			
			logger.info(TAG + " updatePaymentDetals :: getCountryId    :" + metaData.getCountryId());
			logger.info(TAG + " updatePaymentDetals :: getCompCd       :" + metaData.getCompCd());
			logger.info(TAG + " updatePaymentDetals :: getDeviceType   :" + metaData.getDeviceType());
			logger.info(TAG + " updatePaymentDetals :: getDeviceId     :" + metaData.getDeviceId());
			logger.info(TAG + " updatePaymentDetals :: civilId         :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, insertPaymentDetails.getPaySeqNum());
			callableStatement.setString(4, insertPaymentDetails.getPaymentId());
			callableStatement.setString(5, insertPaymentDetails.getApprovalNo());
			callableStatement.setDate(6, insertPaymentDetails.getApprovalDate());
			callableStatement.setString(7, insertPaymentDetails.getResultCd());//Suggested By Ashok Sir
			callableStatement.setString(8, insertPaymentDetails.getTransId());
			callableStatement.setString(9, insertPaymentDetails.getRefId());
			callableStatement.setString(10, insertPaymentDetails.getPaymentToken());
			callableStatement.setString(11, metaData.getDeviceType());
			callableStatement.setString(12, metaData.getDeviceId());
			callableStatement.setString(13, civilId);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			insertPaymentDetails.setErrorCode(callableStatement.getString(14));
			insertPaymentDetails.setErrorMessage(callableStatement.getString(15));

			logger.info(TAG + " insertPaymentDetals :: Error Code :" + callableStatement.getString(14));
			logger.info(TAG + " insertPaymentDetals :: Error Msg  :" + callableStatement.getString(15));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return insertPaymentDetails;
	}
	
	
	public ResponseInfo cretaeAmibCust(BigDecimal custSeqNum , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_CREATE_AMIB_CUSTOMER(?,?,?,?,?,?)}";
		ResponseInfo validate = new ResponseInfo();
		try
		{
			logger.info(TAG + " cretaeAmibCust :: custSeqNum :" + custSeqNum);
			logger.info(TAG + " cretaeAmibCust :: civilId    :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, custSeqNum);
			callableStatement.setString(4, civilId);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			logger.info(TAG + " cretaeAmibCust :: Error Code :" + callableStatement.getString(5));
			logger.info(TAG + " cretaeAmibCust :: Error Msg  :" + callableStatement.getString(6));
			
			validate.setErrorCode(callableStatement.getString(5));
			validate.setErrorMessage(callableStatement.getString(6));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}
	
	public ResponseInfo processReceipt(BigDecimal custSeqNum , String civilId , BigDecimal paySeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_PROCESS_RECEIPT(?,?,?,?,?,?,?,?)}";
		ResponseInfo validate = new ResponseInfo();
		try
		{
			logger.info(TAG + " processReceipt :: paySeqNum :" + paySeqNum);
			logger.info(TAG + " processReceipt :: civilId   :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			callableStatement.setString(4, metaData.getDeviceType());
			callableStatement.setString(5, metaData.getDeviceId());
			callableStatement.setString(6, civilId);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			logger.info(TAG + " processReceipt :: Error Code :" + callableStatement.getString(7));
			logger.info(TAG + " processReceipt :: Error Msg  :" + callableStatement.getString(8));
			
			validate.setErrorCode(callableStatement.getString(7));
			validate.setErrorMessage(callableStatement.getString(8));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}
	
	
	public ResponseInfo createAmibPolicy(BigDecimal custSeqNum , String civilId , BigDecimal paySeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_CREATE_AMIB_POLICY(?,?,?,?,?,?,?,?)}";
		ResponseInfo validate = new ResponseInfo();
		try
		{
			logger.info(TAG + " createAmibPolicy :: paySeqNum :" + paySeqNum);
			logger.info(TAG + " createAmibPolicy :: civilId   :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			callableStatement.setString(4, metaData.getDeviceType());
			callableStatement.setString(5, metaData.getDeviceId());
			callableStatement.setString(6, civilId);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			logger.info(TAG + " createAmibPolicy :: Error Code :" + callableStatement.getString(7));
			logger.info(TAG + " createAmibPolicy :: Error Msg  :" + callableStatement.getString(8));
			
			validate.setErrorCode(callableStatement.getString(7));
			validate.setErrorMessage(callableStatement.getString(8));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}
	
	public ResponseInfo preparePrintData(BigDecimal paySeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_PREPARE_PRINT_DATA(?,?,?,?,?)}";
		ResponseInfo validate = new ResponseInfo();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			logger.info(TAG + " preparePrintData :: Error Code :" + callableStatement.getString(4));
			logger.info(TAG + " preparePrintData :: Error Msg  :" + callableStatement.getString(5));
			
			validate.setErrorCode(callableStatement.getString(4));
			validate.setErrorMessage(callableStatement.getString(5));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}
	
	
	
	
	
	public ArrayResponseModel getPaymentStatus(BigDecimal paySeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PAYMENT_STATUS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.DATE);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(9, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(10, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(11, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(12, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(13, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			PaymentStatus paymentStatus = new PaymentStatus();
			paymentStatus.setPaymentStatus(callableStatement.getString(4));
			paymentStatus.setPaymentDate(DateFormats.uiFormattedDate(callableStatement.getDate(5)));
			paymentStatus.setPayId(callableStatement.getString(6));
			paymentStatus.setRefId(callableStatement.getString(7));
			paymentStatus.setModePremium(callableStatement.getBigDecimal(8));
			paymentStatus.setSupervisionFees(callableStatement.getBigDecimal(9));
			paymentStatus.setIssueFees(callableStatement.getBigDecimal(10));
			paymentStatus.setAdditionalPremium(callableStatement.getBigDecimal(11));
			paymentStatus.setDiscount(callableStatement.getBigDecimal(12));
			paymentStatus.setTotalAmount(callableStatement.getBigDecimal(13));
			
			logger.info(TAG + " preparePrintData :: paymentStatus      :" + paymentStatus.toString());
			logger.info(TAG + " preparePrintData :: Error Code         :" + callableStatement.getString(14));
			logger.info(TAG + " preparePrintData :: Error Msg          :" + callableStatement.getString(15));
			
			arrayResponseModel.setObject(paymentStatus);
			arrayResponseModel.setErrorCode(callableStatement.getString(14));
			arrayResponseModel.setErrorMessage(callableStatement.getString(15));
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
	
	
	public PaymentReceiptModel paymentReceiptData(BigDecimal paySeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_ONLINE_RECEIPT_DATA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		PaymentReceiptModel paymentReceiptModel = new PaymentReceiptModel();
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			logger.info(TAG + " paymentReceiptData :: getCountryId   :" + metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			logger.info(TAG + " paymentReceiptData :: getCompCd      :" + metaData.getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			logger.info(TAG + " paymentReceiptData :: paySeqNum      :" + paySeqNum);
			callableStatement.setBigDecimal(4, metaData.getLanguageId());
			logger.info(TAG + " paymentReceiptData :: getLanguageId  :" + metaData.getLanguageId());
			
			
			callableStatement.registerOutParameter(5, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(6, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(7, java.sql.Types.DATE);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			
			callableStatement.registerOutParameter(9, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(16, java.sql.Types.NUMERIC);
			
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
			
			callableStatement.registerOutParameter(21, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(22, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(23, java.sql.Types.NUMERIC);
			
			callableStatement.registerOutParameter(24, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			paymentReceiptModel.setApplicationId(callableStatement.getBigDecimal(5));
			paymentReceiptModel.setCustomerId(callableStatement.getBigDecimal(6));
			
			String date = null;
			if (null != callableStatement.getDate(7))
			{
				date = DateFormats.uiFormattedDate(callableStatement.getDate(7));
			}
			paymentReceiptModel.setPaymentDate(date);
			paymentReceiptModel.setPaymentMode(callableStatement.getString(8));
			
			paymentReceiptModel.setAmountPaidNumber(callableStatement.getBigDecimal(9));
			paymentReceiptModel.setAmountPaidWord(callableStatement.getString(10));
			paymentReceiptModel.setPaymentId(callableStatement.getString(11));
			paymentReceiptModel.setCustomerName(callableStatement.getString(12));
			
			paymentReceiptModel.setCivilId(callableStatement.getString(13));
			paymentReceiptModel.setMobileNumber(callableStatement.getString(14));
			paymentReceiptModel.setEmialId(callableStatement.getString(15));
			paymentReceiptModel.setPolicyDuration(callableStatement.getBigDecimal(16));
			
			paymentReceiptModel.setGovernate(callableStatement.getString(17));
			paymentReceiptModel.setAreaDesc(callableStatement.getString(18));
			String address = callableStatement.getString(17) + "" + callableStatement.getString(18);
			paymentReceiptModel.setAddress(address);
			paymentReceiptModel.setMake(callableStatement.getString(19));
			paymentReceiptModel.setSubMake(callableStatement.getString(20));
			
			paymentReceiptModel.setKtNumber(callableStatement.getString(21));
			paymentReceiptModel.setChasisNumber(callableStatement.getString(22));
			paymentReceiptModel.setModelYear(callableStatement.getBigDecimal(23));
			
			paymentReceiptModel.setErrorCode(callableStatement.getString(24));
			paymentReceiptModel.setErrorMessage(callableStatement.getString(25));
			
			logger.info(TAG + " paymentReceiptData :: paymentReceiptModel  :" + paymentReceiptModel.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return paymentReceiptModel;
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
	
	
}