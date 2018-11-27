package com.amx.jax.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.PolicyReceiptDetails;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.services.MyPolicyService;
import com.amx.jax.utility.Utility;
import com.amx.utils.ArgUtil;

@RestController
public class MyPolicyController
{
	String TAG = "com.amx.jax.controllers :: MyPolicyController :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyController.class);

	@Autowired
	private MyPolicyService myPolicyService;
	
	@Autowired
	private PostManService postManService;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	MetaData metaData;
	

	@RequestMapping(value = "/api/mypolicy/get-activepolicy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ActivePolicyModel, Object> getUserActivePolicy()
	{
		logger.info(TAG + " getUserActivePolicy :: ");
		return myPolicyService.getUserActivePolicy();
	}
	
	@RequestMapping(value = "/api/mypolicy/renew-policy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> renewInsuranceOldPolicy(@RequestParam(name = "renewAppDocNumber") String renewAppDocNumber)
	{
		BigDecimal renewAppDocNumberDet = null;
		if (null != renewAppDocNumber && !renewAppDocNumber.equals("") && !renewAppDocNumber.equalsIgnoreCase("null"))
		{
			renewAppDocNumberDet = ArgUtil.parseAsBigDecimal(renewAppDocNumber);
		}
		logger.info(TAG + " renewInsuranceOldPolicy :: renewAppDocNumberDet :" + renewAppDocNumberDet);
		return myPolicyService.renewInsuranceOldPolicy(renewAppDocNumberDet);
	}
	
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
			
			PolicyReceiptDetails policyReceiptDetails = myPolicyService.downloadPolicyReceipt(docNumberDet);
			logger.info(TAG + " downloadPolicyReceipt :: policyReceiptDetails :" + policyReceiptDetails.toString());
			
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
			model.put("vehicleValue", Utility.getAmountInCurrency(policyReceiptDetails.getVehicleValue(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("purpose", policyReceiptDetails.getPurpose());
			model.put("colour", policyReceiptDetails.getColour());
			model.put("shape", policyReceiptDetails.getShape());
			model.put("capacity", policyReceiptDetails.getCapacity());
			model.put("fuelType", policyReceiptDetails.getFuelType());
			model.put("vehicleCondition", policyReceiptDetails.getVehicleCondition());
			model.put("insuredName", policyReceiptDetails.getInsuredName());
			model.put("insuredAddress", policyReceiptDetails.getInsuredAddress());
			model.put("insuredMobileNo", policyReceiptDetails.getInsuredMobileNo());
			model.put("policyContribution", Utility.getAmountInCurrency(policyReceiptDetails.getPolicyContribution(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("supervisionFees",Utility.getAmountInCurrency(policyReceiptDetails.getSupervisionFees(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("issueFees", Utility.getAmountInCurrency(policyReceiptDetails.getIssueFees(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("endrosMentFees",Utility.getAmountInCurrency(policyReceiptDetails.getEndrosMentFees(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("discountAmount", Utility.getAmountInCurrency(policyReceiptDetails.getDiscountAmount(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("amountPaidInNum", Utility.getAmountInCurrency(policyReceiptDetails.getAmountPaidInNum(), metaData.getDecplc() , metaData.getCurrency()));
			model.put("amountPaidInWord", (metaData.getCurrency() +" "+ policyReceiptDetails.getAmountPaidInWord()));
			
			dataList.add(model);
			wrapper.put("results", dataList);
			
			file = postManService.processTemplate(new File(TemplatesIB.POLICY_RECEIPT, wrapper, File.Type.PDF)).getResult();
			file.create(response, true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
