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

	public ArrayResponseModel getUserActivePolicy(BigDecimal userAmibCustRef , String civilId , String userType , BigDecimal custSeqNum)
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
				userAmibCustRef = getCustomerAmibCode(civilId ,userType ,custSeqNum);
				
				if(null != userAmibCustRef)
				{
					logger.info(TAG + " getUserActivePolicy :: userAmibCustRef received :" + userAmibCustRef);
					arrayResponseModel.setData(userAmibCustRef.toString());
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
			callableStatement.setBigDecimal(4, metaService.getTenantProfile().getLanguageId());
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			
			logger.info(TAG + " getUserActivePolicy :: rs :" + rs.getRow());

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
				
				logger.info(TAG + " getUserActivePolicy :: setRenewalIndic :"+rs.getString(43));
				if (null != rs.getString(43) && rs.getString(43).equalsIgnoreCase("N"))// Action is based on this
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
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}
	
	
	public BigDecimal getCustomerAmibCode(String civilId , String userType , BigDecimal custSeqNum)
	{
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_GET_AMIB_CUSTCD(?,?,?,?,?)}";
		BigDecimal userAmibCustRef = null; 

		try
		{
			logger.info(TAG + " getCustomerAmibCode :: userAmibCustRef 1 :" + userAmibCustRef);
			
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
			
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(3, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(4, userType);
			callableStatement.setString(5, civilId);
			callableStatement.setBigDecimal(6, custSeqNum);
			
			callableStatement.executeUpdate();
			userAmibCustRef = callableStatement.getBigDecimal(1);
			logger.info(TAG + " getCustomerAmibCode :: userAmibCustRef 2 :" + userAmibCustRef);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return userAmibCustRef;
	}
	
	
	
	public String checkRenewableApplicable(BigDecimal oldDocNumber , String civilId , String userType , BigDecimal custSeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_RENEW_APPLICABLE(?,?,?,?,?,?,?,?)}";
		String renewableApplError = null;

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

			logger.info(TAG + " checkRenewableApplicable :: oldDocNumber       :" + oldDocNumber);
			logger.info(TAG + " checkRenewableApplicable :: error code    :" + renewableApplError);
			logger.info(TAG + " checkRenewableApplicable :: error message :" + callableStatement.getString(8));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return renewableApplError;
	}
	
	public PolicyReceiptDetails downloadPolicyReceipt(BigDecimal docNumber)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_ONLINE_POLICY_PRINT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		
		PolicyReceiptDetails policyReceiptDetails = new PolicyReceiptDetails();
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, docNumber);
			callableStatement.setBigDecimal(4, metaService.getTenantProfile().getLanguageId());
			callableStatement.registerOutParameter(5, java.sql.Types.DATE);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.DATE);
			callableStatement.registerOutParameter(8, java.sql.Types.DATE);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(18, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(21, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(22, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(23, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(24, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(26, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(27, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(28, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(29, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(30, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(31, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(32, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(33, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(34, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(35, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			policyReceiptDetails.setPolicyIssueDate(DateFormats.uiFormattedDate(callableStatement.getDate(5)));
			policyReceiptDetails.setPolicyNumber(callableStatement.getString(6));
			policyReceiptDetails.setPolicyFromDate(DateFormats.uiFormattedDate(callableStatement.getDate(7)));
			policyReceiptDetails.setPolicyDueDate(DateFormats.uiFormattedDate(callableStatement.getDate(8)));
			policyReceiptDetails.setInsuranceCo(callableStatement.getString(9));
			policyReceiptDetails.setInsuredName(callableStatement.getString(10));
			policyReceiptDetails.setInsuredAddress(callableStatement.getString(11));
			//Civil ID
			policyReceiptDetails.setInsuredMobileNo(callableStatement.getString(13));
			logger.info(TAG + " downloadPolicyReceipt :: insuredMobileNo :" + callableStatement.getString(13));
			policyReceiptDetails.setMake(callableStatement.getString(14));
			policyReceiptDetails.setSubMake(callableStatement.getString(15));
			policyReceiptDetails.setKtNumber(callableStatement.getString(16));
			policyReceiptDetails.setChaisisNumber(callableStatement.getString(17));
			policyReceiptDetails.setModelYear(callableStatement.getBigDecimal(18));
			policyReceiptDetails.setPurpose(callableStatement.getString(19));
			policyReceiptDetails.setColour(callableStatement.getString(20));
			policyReceiptDetails.setShape(callableStatement.getString(21));
			policyReceiptDetails.setCapacity(callableStatement.getBigDecimal(22));
			policyReceiptDetails.setFuelType(callableStatement.getString(23));
			policyReceiptDetails.setVehicleCondition(callableStatement.getString(24));
			logger.info(TAG + " downloadPolicyReceipt :: setAdditionalCoverage :" + callableStatement.getString(25));
			policyReceiptDetails.setAdditionalCoverage(callableStatement.getString(25));
			policyReceiptDetails.setVehicleValue(callableStatement.getBigDecimal(26));
			policyReceiptDetails.setPolicyContribution(callableStatement.getBigDecimal(27));
			policyReceiptDetails.setSupervisionFees(callableStatement.getBigDecimal(28));
			policyReceiptDetails.setIssueFees(callableStatement.getBigDecimal(29));
			policyReceiptDetails.setEndrosMentFees(callableStatement.getBigDecimal(30));
			policyReceiptDetails.setDiscountAmount(callableStatement.getBigDecimal(31));
			policyReceiptDetails.setAmountPaidInNum(callableStatement.getBigDecimal(32));
			policyReceiptDetails.setAmountPaidInWord(callableStatement.getString(33));
			
			logger.info(TAG + " downloadPolicyReceipt :: getString(34) :" + callableStatement.getString(34));
			logger.info(TAG + " downloadPolicyReceipt :: getString(35) :" + callableStatement.getString(35));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return policyReceiptDetails;
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
