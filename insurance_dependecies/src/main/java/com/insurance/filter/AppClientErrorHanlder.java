




package com.insurance.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.insurance.error.AmxApiError;
import com.insurance.error.AmxApiException;
import com.insurance.error.ExceptionFactory;
import com.insurance.utils.ArgUtil;
import com.insurance.utils.JsonUtil;

@Component
public class AppClientErrorHanlder implements ResponseErrorHandler
{

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException
	{
		if (response.getStatusCode() != HttpStatus.OK)
		{
			return true;
		}
		String apiErrorJson = (String) response.getHeaders().getFirst("apiErrorJson");
		if (!ArgUtil.isEmpty(apiErrorJson))
		{
			return true;
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException
	{

		String apiErrorJson = (String) response.getHeaders().getFirst("apiErrorJson");
		AmxApiError apiError = JsonUtil.fromJson(apiErrorJson, AmxApiError.class);

		AmxApiException defExcp = ExceptionFactory.get(apiError.getErrorClass());

		if (defExcp == null)
		{
			defExcp = ExceptionFactory.get(apiError.getErrorId());
		}

		if (defExcp != null)
		{
			throw defExcp.getInstance(apiError);
		}

	}

	static
	{
		ExceptionFactory.readExceptions();
	}
}
