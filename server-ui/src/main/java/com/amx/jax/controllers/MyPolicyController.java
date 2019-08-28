package com.amx.jax.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.ClaimDetails;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.InsuranceClaimDetails;
import com.amx.jax.models.PolicyReceiptDetails;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.services.MyPolicyService;
import com.amx.jax.swagger.ApiMockParam;
import com.amx.jax.utility.Utility;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class MyPolicyController {
	private static final Logger logger = LoggerFactory.getLogger(MyPolicyController.class);

	@Autowired
	private MyPolicyService myPolicyService;

	@Autowired
	private PostManService postManService;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	IMetaService metaService;

	@ApiOperation(value = "returns the list of policy of the customer" , notes = "based on customer_sequence_number which you get from user details will return list of policy details, here customer sequence number is taken from session so no need to send it in api.")
	@RequestMapping(value = "/api/mypolicy/get-activepolicy", method = RequestMethod.POST)
	public AmxApiResponse<ActivePolicyModel, Object> getUserActivePolicy() {
		return myPolicyService.getUserActivePolicy();
	}

	@ApiOperation(value = "this api is to renew the old policy taken by customer" , notes = "this api will renew the old policy taken by the customer")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR , WebAppStatusCodes.SUCCESS , WebAppStatusCodes.CIVIL_ID_EXPIRED })
	@ApiMockParam(example = "124", value = "old doc number of insurance quote")
	@RequestMapping(value = "/api/mypolicy/renew-policy", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> renewInsuranceOldPolicy(
			@RequestParam(name = "renewAppDocNumber") String renewAppDocNumber) {
		BigDecimal renewAppDocNumberDet = null;
		if (null != renewAppDocNumber && !renewAppDocNumber.equals("") && !renewAppDocNumber.equalsIgnoreCase("null")) {
			renewAppDocNumberDet = ArgUtil.parseAsBigDecimal(renewAppDocNumber);
		}
		return myPolicyService.renewInsuranceOldPolicy(renewAppDocNumberDet);
	}

	@ApiOperation(value = "api to download policy receipt pdf of entered doc number" , notes = "this api will download the policy receipt of given doc number")
	@ApiMockParam(example = "1244", value = "doc number of insurance quote")
	@RequestMapping(value = "/api/mypolicy/policy-receipt", method = { RequestMethod.GET })
	public String downloadPolicyReceipt(@RequestParam String docNumber) 
	{
		File file = null;
		try 
		{

			BigDecimal docNumberDet = null;
			if (null != docNumber && !docNumber.equals("") && !docNumber.equalsIgnoreCase("null"))
			{
				docNumberDet = ArgUtil.parseAsBigDecimal(docNumber, null);
			}

			logger.info("MyPolicyController :: downloadPolicyReceipt :: docNumberDet :" + docNumberDet);
			
			ArrayResponseModel arrayResponseModel = myPolicyService.downloadPolicyReceipt(docNumberDet);
			if(arrayResponseModel.getErrorCode() == null)
			{
				PolicyReceiptDetails policyReceiptDetails = (PolicyReceiptDetails) arrayResponseModel.getObject();
				
				logger.info("MyPolicyController :: downloadPolicyReceipt :: policyReceiptDetails :" + policyReceiptDetails.toString());
				
				Map<String, Object> wrapper = new HashMap<String, Object>();
				Map<String, Object> model = new HashMap<String, Object>();
				ArrayList<Map> dataList = new ArrayList<>();

				model.put("policyNumber", policyReceiptDetails.getPolicyNumber());
				model.put("policyIssueDate", policyReceiptDetails.getPolicyIssueDate());
				model.put("policyFromDate", policyReceiptDetails.getPolicyFromDate());
				model.put("policyDueDate", policyReceiptDetails.getPolicyDueDate());
				model.put("additionalCoverage", policyReceiptDetails.getAdditionalCoverage());
				model.put("insuranceCo", policyReceiptDetails.getInsuranceCo());
				model.put("make", policyReceiptDetails.getMake());
				model.put("subMake", policyReceiptDetails.getSubMake());
				model.put("modelYear", policyReceiptDetails.getModelYear());
				model.put("chaisisNumber", policyReceiptDetails.getChaisisNumber());
				model.put("ktNumber", policyReceiptDetails.getKtNumber());
				model.put("vehicleValue", Utility.getAmountInCurrency(policyReceiptDetails.getVehicleValue(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("purpose", policyReceiptDetails.getPurpose());
				model.put("colour", policyReceiptDetails.getColour());
				model.put("shape", policyReceiptDetails.getShape());
				model.put("capacity", policyReceiptDetails.getCapacity());
				model.put("fuelType", policyReceiptDetails.getFuelType());
				model.put("vehicleCondition", policyReceiptDetails.getVehicleCondition());
				model.put("insuredName", policyReceiptDetails.getInsuredName());
				model.put("insuredAddress", policyReceiptDetails.getInsuredAddress());
				model.put("civilId", policyReceiptDetails.getCivilId());
				model.put("insuredMobileNo", policyReceiptDetails.getInsuredMobileNo());
				model.put("policyContribution", Utility.getAmountInCurrency(policyReceiptDetails.getPolicyContribution(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("supervisionFees", Utility.getAmountInCurrency(policyReceiptDetails.getSupervisionFees(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("issueFees", Utility.getAmountInCurrency(policyReceiptDetails.getIssueFees(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("endrosMentFees", Utility.getAmountInCurrency(policyReceiptDetails.getEndrosMentFees(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("discountAmount", Utility.getAmountInCurrency(policyReceiptDetails.getDiscountAmount(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("amountPaidInNum", Utility.getAmountInCurrency(policyReceiptDetails.getAmountPaidInNum(),
						metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
				model.put("amountPaidInWord", policyReceiptDetails.getAmountPaidInWord());
				model.put("trnsReceiptRef", policyReceiptDetails.getReceiptReference());
				logger.info("model is "+model.toString());
				dataList.add(model);
				wrapper.put("results", dataList);

				file = postManService.processTemplate(new File(TemplatesIB.POLICY_RECEIPT, wrapper, File.Type.PDF))
						.getResult();
				file.create(response, true);
			}
			else
			{
				return arrayResponseModel.getErrorCode();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return ApiConstants.TECHNICAL_ERROR_ON_SERVER;
		}
		return null;
	}
	
	@ApiOperation(value = "send email to the claim dept" , notes = "send email to the claim dept")
	@RequestMapping(value = "/api/mypolicy/send-claimInfo", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object>sendClaimNotification(@RequestBody ClaimDetails claimDetails) {
		BoolRespModel emailResponse=myPolicyService.sendClaimNotification(claimDetails);
		return  AmxApiResponse.build(emailResponse);

	}
	
	
}
