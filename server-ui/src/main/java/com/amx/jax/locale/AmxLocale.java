package com.amx.jax.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.ui.session.UserSession;

@Component
public class AmxLocale implements IAmxLocale
{
	@Autowired
	UserSession userSession; 


	@Override
	public Language getLanguage() 
	{
		if(null != userSession.getLanguageId() && userSession.getLanguageId().toString().equals("1"))
		{
			return Language.AR;
		}
		return Language.EN;
	}


	@Override
	public String getCountry() 
	{
		if(getLanguage().equals(Language.AR))
		{
			return "الكويت";
		}
		return "KUWAIT";
	}
	
	@Override
	public String getregProcess()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "إعادة تسجيل ";
		}
		return "registration process";
	}
	
	@Override
	public String getOTP_used_for_reg()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "التسجيل ";
		}
		return "registration";
	}
	
	@Override
	public String resetpassProcess()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "إعادة تعيين كلمة السر ";
		}
		return "reset password process";
	}
	
	@Override
	public String getOTP_used_for_resetpass()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "إإعادة ضبط كلمة السر ";
		}
		return "password reset";
	}
	
	@Override
	public String updateprofileProcess()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "تحديث الملف ";
		}
		return "update profile";
	}
	
	@Override
	public String getOTP_used_for_updateprofile()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "تحديث التفاصيل ";
		}
		return "updating details";
	}
	
	@Override
	public String reg_OTP_email_subject()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "كلمة المرور مرة واحدة ";
		}
		return "One Time Password (OTP)";
	}
	
	@Override
	public String reg_success_email_subject()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "نجاح التسجيل";
		}
		return "Registration Success";
	}
	
	@Override
	public String reg_success_email_msg()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "الملا للتأمين  أكتمل التسجيل بنجاح ";
		}
		return "Al Mulla Insurance Registration Completed Successfully.";
	}
	
	@Override
	public String reg_faliure_email_subject()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "العميل الجديد غير مسجل ";
		}
		return "New Customer Incomplete Registration";
	}
	
	@Override
	public String email_cust_compile_request_msg()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "طلب عرض السعر -";
		}
		return "Quote Request - ";
	}
	
	@Override
	public String email_amib_compile_request_msg()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "طلب عرض السعر العميل -";     
		}
		return "Customer Quote Request - ";
	}
	
	@Override
	public String email_cust_success_payment_subject()
	{
		if(getLanguage().equals(Language.AR))
		{
			return "الملا للوساطة في التأمين  الدفع الناجح ";     
		}
		return "Al Mulla Insurance Brokerage Payment Success";
	}
	
	//Test
}
