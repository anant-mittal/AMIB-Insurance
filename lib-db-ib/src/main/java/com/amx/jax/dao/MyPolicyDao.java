package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.PolicyReceiptDetails;
import com.amx.jax.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import oracle.jdbc.OracleTypes;

@Repository
public class MyPolicyDao
{
	String TAG = "MyPolicyDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IMetaService metaService;
	
	Connection connection;

	public ArrayResponseModel getUserActivePolicy(BigDecimal userAmibCustRef , String civilId , String userType , BigDecimal custSeqNum , BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ACTIVE_POLICIES(?,?,?,?,?,?,?)}";
		ArrayList<ActivePolicyModel> activePolicyArray = new ArrayList<ActivePolicyModel>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();

		logger.info(TAG + " getUserActivePolicy :: userAmibCustRef :" + userAmibCustRef);
		try
		{
			if(null == userAmibCustRef)
			{
				ArrayResponseModel arrayRespCustAmibCode = getCustomerAmibCode(civilId ,userType ,custSeqNum);
				if(null != arrayRespCustAmibCode)
				{
					if(null == arrayRespCustAmibCode.getErrorCode())
					{
						userAmibCustRef = arrayRespCustAmibCode.getNumericData();
						if(null != userAmibCustRef)
						{
							logger.info(TAG + " getUserActivePolicy :: userAmibCustRef received :" + userAmibCustRef);
							arrayResponseModel.setData(userAmibCustRef.toString());
						}
					}
					else
					{
						return arrayRespCustAmibCode;
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, userAmibCustRef);
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			
			while (rs.next())
			{

				ActivePolicyModel activePolicyModel = new ActivePolicyModel();
				activePolicyModel.setCountryId(rs.getBigDecimal(1));
				activePolicyModel.setCompCd(rs.getBigDecimal(2));
				activePolicyModel.setDocNumber(rs.getBigDecimal(3));
				activePolicyModel.setDocDate(DateFormats.uiFormattedDate(rs.getDate(4)));
				activePolicyModel.setFinance(rs.getBigDecimal(5));
				activePolicyModel.setShowRoom(rs.getBigDecimal(6));
				activePolicyModel.setSalesman(rs.getBigDecimal(7));
				activePolicyModel.setUserAmibCustRef(rs.getBigDecimal(8));
				activePolicyModel.setInsuredEnglishName(rs.getString(9));
				activePolicyModel.setInsuredArabicName(rs.getString(10));
				activePolicyModel.setIdNumber(rs.getBigDecimal(11));
				activePolicyModel.setInsCompanyCode(rs.getBigDecimal(12));
				activePolicyModel.setInsCompanyDesc(rs.getString(13));
				activePolicyModel.setInsCompanyPrefix(rs.getString(14));
				activePolicyModel.setDbcust(rs.getBigDecimal(15));
				activePolicyModel.setMakeCode(rs.getString(16));
				activePolicyModel.setMakeDesc(rs.getString(17));
				activePolicyModel.setSubMakeCode(rs.getString(18));
				activePolicyModel.setSubMakeDesc(rs.getString(19));
				activePolicyModel.setModelYear(rs.getBigDecimal(20));
				activePolicyModel.setShapeCode(rs.getString(21));
				activePolicyModel.setShapeDesc(rs.getString(22));
				activePolicyModel.setColourCode(rs.getString(23));
				activePolicyModel.setColourDesc(rs.getString(24));
				activePolicyModel.setNoPass(rs.getBigDecimal(25));
				activePolicyModel.setChassis(rs.getString(26));
				activePolicyModel.setKtNumber(rs.getString(27));
				activePolicyModel.setVehicleConditionCode(rs.getString(28));
				activePolicyModel.setVehicleConditionDesc(rs.getString(29));
				activePolicyModel.setPurposeCode(rs.getString(30));
				activePolicyModel.setPurposeDesc(rs.getString(31));
				activePolicyModel.setVehicleSrNumber(rs.getBigDecimal(32));
				activePolicyModel.setPolicyTypeCode(rs.getString(33));
				activePolicyModel.setPolicyTypeDesc(rs.getString(34));
				activePolicyModel.setPolicyNumber(rs.getString(35));
				activePolicyModel.setMaxInsuredAmount(Utility.round(rs.getBigDecimal(36), metaService.getTenantProfile().getDecplc()));
				activePolicyModel.setStartDate(DateFormats.uiFormattedDate(rs.getDate(37)));
				activePolicyModel.setEndDate(DateFormats.uiFormattedDate(rs.getDate(38)));
				activePolicyModel.setSupervisionKey(rs.getBigDecimal(39));
				activePolicyModel.setIssueFee(Utility.round(rs.getBigDecimal(40), metaService.getTenantProfile().getDecplc()));
				activePolicyModel.setPremium(Utility.round(rs.getBigDecimal(41), metaService.getTenantProfile().getDecplc()));
				activePolicyModel.setDiscount(rs.getBigDecimal(42));
				activePolicyModel.setRenewalIndic(rs.getString(43));//Not Used On UI
				activePolicyModel.setFuelCode(rs.getString(44));
				activePolicyModel.setFuelDesc(rs.getString(45));
				if (null != rs.getString(43) && rs.getString(43).equalsIgnoreCase("N")) // Action is based on this
				{
					activePolicyModel.setRenewableApplCheck("N");
				}
				else
				{
					activePolicyModel.setRenewableApplCheck("Y");
				}
				activePolicyArray.add(activePolicyModel);
			}
			
			arrayResponseModel.setDataArray(activePolicyArray);
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getUserActivePolicy :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}
	
	
	public ArrayResponseModel getCustomerAmibCode(String civilId , String userType , BigDecimal custSeqNum)
	{
		logger.info(TAG + " getCustomerAmibCode :: civilId :" + civilId);
		logger.info(TAG + " getCustomerAmibCode :: userType :" + userType);
		logger.info(TAG + " getCustomerAmibCode :: custSeqNum :" + custSeqNum);
		logger.info(TAG + " getCustomerAmibCode :: getCountryId :" + metaService.getTenantProfile().getCountryId());
		logger.info(TAG + " getCustomerAmibCode :: getCompCd :" + metaService.getTenantProfile().getCompCd());
		
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_GET_AMIB_CUSTCD(?,?,?,?,?)}";
		BigDecimal userAmibCustRef = null; 
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(4, userType);
			callableStatement.setString(5, civilId);
			callableStatement.setBigDecimal(6, custSeqNum);
			callableStatement.executeUpdate();
			
			userAmibCustRef = callableStatement.getBigDecimal(1);
			logger.info(TAG + " getCustomerAmibCode :: userAmibCustRef :" + userAmibCustRef);
			arrayResponseModel.setNumericData(userAmibCustRef);
			
		}
		catch (SQLException e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getCustomerAmibCode :: exception :" + e);
			e.printStackTrace();
		}
		return arrayResponseModel;
	}
	
	
	
	public ArrayResponseModel checkRenewableApplicable(BigDecimal oldDocNumber , String civilId , String userType , BigDecimal custSeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_RENEW_APPLICABLE(?,?,?,?,?,?,?,?)}";
		String renewableApplError = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, custSeqNum);
			callableStatement.setBigDecimal(6, oldDocNumber);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			renewableApplError = callableStatement.getString(7);
			arrayResponseModel.setData(renewableApplError);

			logger.info(TAG + " checkRenewableApplicable :: oldDocNumber       :" + oldDocNumber);
			logger.info(TAG + " checkRenewableApplicable :: error code    :" + renewableApplError);
			logger.info(TAG + " checkRenewableApplicable :: error message :" + callableStatement.getString(8));
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"checkRenewableApplicable :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}
	
	public ArrayResponseModel downloadPolicyReceipt(BigDecimal docNumber, BigDecimal languageId)
	{
		getConnection();
		
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_ONLINE_POLICY_PRINT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		PolicyReceiptDetails policyReceiptDetails = new PolicyReceiptDetails();
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, docNumber);
			callableStatement.setBigDecimal(4, null);
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
			policyReceiptDetails.setCivilId(callableStatement.getString(13));
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
			logger.info("MyPolicyDao :: downloadPolicyReceipt :: getErrorMessage :" + arrayResponseModel.getErrorMessage());
			
			arrayResponseModel.setObject(policyReceiptDetails);
			
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"downloadPolicyReceipt :: exception :" + e);
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
}
