package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceipt;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.models.PolicyReceiptDetails;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.DateFormats;

@Repository
public class PayMentDao
{
	String TAG = "com.amx.jax.dao.PayMentDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayMentDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IMetaService metaService;
	
	Connection connection;
	
	
	public PaymentDetails insertPaymentDetals(PaymentDetails insertPaymentDetails , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_INSERT_PAYMENT_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, insertPaymentDetails.getAppSeqNum());
			callableStatement.setBigDecimal(4, insertPaymentDetails.getQuoteSeqNum());
			callableStatement.setBigDecimal(5, insertPaymentDetails.getQouteVerNum());
			callableStatement.setBigDecimal(6, insertPaymentDetails.getCustSeqNum());
			callableStatement.setString(7, insertPaymentDetails.getPaymentMethod());//Suggested By Ashok Sir
			callableStatement.setBigDecimal(8, insertPaymentDetails.getPaymentAmount());
			callableStatement.setString(9, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(10, metaService.getUserDeviceInfo().getDeviceId());
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
			insertPaymentDetails.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			insertPaymentDetails.setErrorMessage(e.toString());
			logger.info(TAG+"insertPaymentDetals :: exception :" + e);
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
		logger.info("insert payment details are : "+insertPaymentDetails);
		logger.info("civilId for paymemt is "+civilId);
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PROCESS_QUOTE.IRB_UPDATE_PAYMENT_DTLS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try
		{
			logger.info(TAG + " updatePaymentDetals :: PaymentDetails       :" + insertPaymentDetails.toString());
			logger.info(TAG + " updatePaymentDetals :: getTenantProfile     :" + metaService.getTenantProfile().toString());
			logger.info(TAG + " updatePaymentDetals :: getUserDeviceInfo    :" + metaService.getUserDeviceInfo().toString());
			logger.info(TAG + " updatePaymentDetals :: civilId              :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, insertPaymentDetails.getPaySeqNum());
			callableStatement.setString(4, insertPaymentDetails.getPaymentId());
			callableStatement.setString(5, insertPaymentDetails.getApprovalNo());
			callableStatement.setDate(6, insertPaymentDetails.getApprovalDate());
			callableStatement.setString(7, insertPaymentDetails.getResultCd());//Suggested By Ashok Sir
			callableStatement.setString(8, insertPaymentDetails.getTransId());
			callableStatement.setString(9, insertPaymentDetails.getRefId());
			callableStatement.setString(10, insertPaymentDetails.getPaymentToken());
			callableStatement.setString(11, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(12, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(13, civilId);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			insertPaymentDetails.setErrorCode(callableStatement.getString(14));
			insertPaymentDetails.setErrorMessage(callableStatement.getString(15));

			logger.info(TAG + " updatePaymentDetals :: Error Code :" + callableStatement.getString(14));
			logger.info(TAG + " updatePaymentDetals :: Error Msg  :" + callableStatement.getString(15));
			
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
			//logger.info(TAG + " cretaeAmibCust :: custSeqNum :" + custSeqNum);
			//logger.info(TAG + " cretaeAmibCust :: civilId    :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
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
			logger.info(TAG+"cretaeAmibCust :: exception :" + e);
			validate.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			validate.setErrorMessage(e.toString());
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
			//logger.info(TAG + " processReceipt :: paySeqNum :" + paySeqNum);
			//logger.info(TAG + " processReceipt :: civilId   :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			callableStatement.setString(4, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(5, metaService.getUserDeviceInfo().getDeviceId());
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
			logger.info(TAG+"processReceipt :: exception :" + e);
			validate.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			validate.setErrorMessage(e.toString());
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
			//logger.info(TAG + " createAmibPolicy :: paySeqNum :" + paySeqNum);
			//logger.info(TAG + " createAmibPolicy :: civilId   :" + civilId);
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, paySeqNum);
			callableStatement.setString(4, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(5, metaService.getUserDeviceInfo().getDeviceId());
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
			logger.info(TAG+"createAmibPolicy :: exception :" + e);
			validate.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			validate.setErrorMessage(e.toString());
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
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
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
			logger.info(TAG+"preparePrintData :: exception :" + e);
			validate.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			validate.setErrorMessage(e.toString());
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}
	
	
	
	public ArrayResponseModel getPaymentStatus(BigDecimal paySeqNum)
	{
		logger.info("payseqno is "+paySeqNum);
		logger.info("countryid "+metaService.getTenantProfile().getCompCd());
		logger.info("compcd is "+paySeqNum);
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_PAYMENT_STATUS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
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
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
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
			paymentStatus.setTransactionId(callableStatement.getString(14));
			paymentStatus.setPayToken(callableStatement.getString(15));
			paymentStatus.setAppSeqNumber(callableStatement.getBigDecimal(16));
			
			//logger.info(TAG + " preparePrintData :: paymentStatus      :" + paymentStatus.toString());
			logger.info(TAG + " preparePrintData :: Error Code         :" + callableStatement.getString(17));
			logger.info(TAG + " preparePrintData :: Error Msg          :" + callableStatement.getString(18));
			
			arrayResponseModel.setObject(paymentStatus);
			arrayResponseModel.setErrorCode(callableStatement.getString(17));
			arrayResponseModel.setErrorMessage(callableStatement.getString(18));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.info(TAG+"preparePrintData :: exception :" + e);
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}
	
	
	public ArrayResponseModel paymentReceiptData(BigDecimal paySeqNum, BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;

		String callProcedure = "{call IRB_ONLINE_POLICY_PRINT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		PolicyReceiptDetails policyReceiptDetails = new PolicyReceiptDetails();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try {
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, null);
			callableStatement.setBigDecimal(4, paySeqNum);
			callableStatement.setBigDecimal(5, languageId);

			callableStatement.registerOutParameter(6, java.sql.Types.DATE);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.DATE);
			callableStatement.registerOutParameter(9, java.sql.Types.DATE);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(19, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(21, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(22, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(23, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(24, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(26, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(27, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(28, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(29, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(30, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(31, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(32, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(33, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(34, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(35, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(36, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(37, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			policyReceiptDetails.setPolicyIssueDate(DateFormats.uiFormattedDate(callableStatement.getDate(6)));
			policyReceiptDetails.setPolicyNumber(callableStatement.getString(7));
			policyReceiptDetails.setPolicyFromDate(DateFormats.uiFormattedDate(callableStatement.getDate(8)));
			policyReceiptDetails.setPolicyDueDate(DateFormats.uiFormattedDate(callableStatement.getDate(9)));
			policyReceiptDetails.setInsuranceCo(callableStatement.getString(10));
			policyReceiptDetails.setInsuredName(callableStatement.getString(11));
			policyReceiptDetails.setInsuredAddress(callableStatement.getString(12));
			// Civil ID
			policyReceiptDetails.setInsuredMobileNo(callableStatement.getString(14));
			policyReceiptDetails.setMake(callableStatement.getString(15));
			policyReceiptDetails.setSubMake(callableStatement.getString(16));
			policyReceiptDetails.setKtNumber(callableStatement.getString(17));
			policyReceiptDetails.setChaisisNumber(callableStatement.getString(18));
			policyReceiptDetails.setModelYear(callableStatement.getBigDecimal(19));
			policyReceiptDetails.setPurpose(callableStatement.getString(20));
			policyReceiptDetails.setColour(callableStatement.getString(21));
			policyReceiptDetails.setShape(callableStatement.getString(22));
			policyReceiptDetails.setCapacity(callableStatement.getBigDecimal(23));
			policyReceiptDetails.setFuelType(callableStatement.getString(24));
			policyReceiptDetails.setVehicleCondition(callableStatement.getString(25));
			policyReceiptDetails.setAdditionalCoverage(callableStatement.getString(26));
			policyReceiptDetails.setVehicleValue(callableStatement.getBigDecimal(27));
			policyReceiptDetails.setPolicyContribution(callableStatement.getBigDecimal(28));
			policyReceiptDetails.setSupervisionFees(callableStatement.getBigDecimal(29));
			policyReceiptDetails.setIssueFees(callableStatement.getBigDecimal(30));
			policyReceiptDetails.setEndrosMentFees(callableStatement.getBigDecimal(31));
			policyReceiptDetails.setDiscountAmount(callableStatement.getBigDecimal(32));
			policyReceiptDetails.setAmountPaidInNum(callableStatement.getBigDecimal(33));
			policyReceiptDetails.setAmountPaidInWord(callableStatement.getString(34));
			policyReceiptDetails.setReceiptReference(callableStatement.getString(35));
			arrayResponseModel.setErrorCode(callableStatement.getString(36));
			arrayResponseModel.setErrorMessage(callableStatement.getString(37));

			logger.info("MyPolicyDao :: downloadPolicyReceipt :: getErrorCode :" + arrayResponseModel.getErrorCode());
			logger.info(
					"MyPolicyDao :: downloadPolicyReceipt :: getErrorMessage :" + arrayResponseModel.getErrorMessage());

			arrayResponseModel.setObject(policyReceiptDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(TAG + "paymentReceiptData :: exception :" + e);
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
		} finally {
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
}