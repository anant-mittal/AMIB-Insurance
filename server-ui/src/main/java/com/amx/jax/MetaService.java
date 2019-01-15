package com.amx.jax;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.def.CacheForThis;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.meta.TenantProfile;
import com.amx.jax.meta.UserDeviceInfos;
import com.amx.jax.models.CompanySetUp;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaService implements IMetaService {

	private static final Logger logger = LoggerFactory.getLogger(MetaService.class);
	private static final long serialVersionUID = 8260115321460438947L;

	@Autowired
	private TenantProfile tenantProfile;

	@Autowired
	private UserDeviceInfos userDeviceInfos;

	@Autowired
	private WebConfig webConfig;

	@Autowired
	CommonHttpRequest httpService;

	@Autowired
	private CustomerRegistrationDao customerRegistrationDao;

	@Override
	// @CacheForThis
	public TenantProfile getTenantProfile() 
	{
		if (tenantProfile.getCountryId() == null) 
		{
			ArrayList<CompanySetUp> getCompanySetUp = customerRegistrationDao.getCompanySetUp(laguageSetUp(),webConfig.getAppCompCode());
			tenantProfile.setCountryId(getCompanySetUp.get(0).getCntryCd());
			tenantProfile.setCompCd(getCompanySetUp.get(0).getCompCd());
			tenantProfile.setContactUsHelpLineNumber(getCompanySetUp.get(0).getHelpLineNumber());
			tenantProfile.setContactUsEmail(getCompanySetUp.get(0).getEmail());
			tenantProfile.setAmibWebsiteLink(getCompanySetUp.get(0).getWebSite());
			tenantProfile.setDecplc(getCompanySetUp.get(0).getDecimalPlaceUpTo());
			tenantProfile.setCurrency(getCompanySetUp.get(0).getCurrency());
			tenantProfile.setCompanyName(getCompanySetUp.get(0).getCompanyName());
		}
		return tenantProfile;
	}

	@Override
	public UserDeviceInfos getUserDeviceInfo() 
	{
		logger.info("MetaService :: getUserDeviceInfo :: getIPAddress :" + httpService.getIPAddress());
		
		if (userDeviceInfos.getDeviceId() == null)
		{
			userDeviceInfos.setDeviceId(httpService.getIPAddress());
		}
		
		if(userDeviceInfos.getDeviceType() == null)
		{
			if(null != httpService.getUserDevice().getAppType())
			{
				if(httpService.getUserDevice().getAppType().toString().equalsIgnoreCase("WEB"))
				{
					userDeviceInfos.setDeviceType(HardCodedValues.DEVICE_TYPE_WEB);
				}
				else if(httpService.getUserDevice().getAppType().toString().equalsIgnoreCase("ANDROID"))
				{
					userDeviceInfos.setDeviceType(HardCodedValues.DEVICE_TYPE_ANDROID);
				}
				else if(httpService.getUserDevice().getAppType().toString().equalsIgnoreCase("IOS"))
				{
					userDeviceInfos.setDeviceType(HardCodedValues.DEVICE_TYPE_IOS);
				}
				else
				{
					userDeviceInfos.setDeviceType(HardCodedValues.DEVICE_TYPE_WEB);
				}
			}
		}
		
		return userDeviceInfos;
	}

	public BigDecimal laguageSetUp() 
	{
		return new BigDecimal(0);
	}
}
