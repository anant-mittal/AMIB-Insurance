package com.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.rest.RestService;

@Component
public class SMService
{
	@Autowired
	RestService restService;

	public void sendMessage(String phone, String text)
	{
		System.out.println("CHECK SMS SENT HERE :: phone : "+phone);
		String response = restService.ajax("https://applications2.almullagroup.com/Login_Enhanced/LoginEnhancedServlet").field("destination_mobile", phone).field("message_to_send", text).postForm().asString();
		System.out.println("CHECK SMS SENT HERE :: response : "+response);
	}
}
