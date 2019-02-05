package com.amx.jax.locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dict.Language;
import com.amx.jax.ui.session.UserSession;

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
	
}
