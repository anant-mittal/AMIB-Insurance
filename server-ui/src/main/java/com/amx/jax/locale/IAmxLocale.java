package com.amx.jax.locale;

import com.amx.jax.dict.Language;

public interface IAmxLocale 
{
	public Language getLanguage();
	
	public String getCountry();
	
	public String getregProcess();
	
	public String getOTP_used_for_reg();
	
	public String resetpassProcess();
	
	public String getOTP_used_for_resetpass();
	
	public String updateprofileProcess();
	
	public String getOTP_used_for_updateprofile();
	
	public String reg_OTP_email_subject();
	
	public String reg_success_email_subject();
	
	public String reg_success_email_msg();
	
	public String reg_faliure_email_subject();
	
	public String email_cust_compile_request_msg();
	
	public String email_amib_compile_request_msg();
	
	public String email_cust_success_payment_subject();
	
	public String email_amib_claim_info(String insCompName);
	
}
