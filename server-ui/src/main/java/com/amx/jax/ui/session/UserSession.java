package com.amx.jax.ui.session;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map.Entry;
import org.redisson.api.RLocalCachedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import com.amx.jax.config.CustomerAuthProvider;
import com.amx.jax.config.WebSecurityConfig;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditService;
import com.amx.jax.scope.TenantContextHolder;
import com.sleepycat.je.utilint.Timestamp;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession
{
	//String TAG = "UserSession :: ";

	private static final Logger logger = LoggerFactory.getLogger(UserSession.class);
	
	private static final String USER_KEY_FORMAT = "%s#%s";
	
	@Autowired
	LoggedInUsers loggedInUsers;
	
	@Autowired
	private CommonHttpRequest httpService;
	
	boolean valid;

	private String referrer = null;

	private String civilId;

	private String motpPrefix;

	private String eotpPrefix;

	private String motp;

	private String eotp;

	private String customerMobileNumber;

	private String customerEmailId;

	private String changePasswordOtp;

	private String mOtpMobileNumber = "";

	private String eOtpEmailId = "";

	private BigDecimal customerSequenceNumber;

	private BigDecimal userSequenceNumber;

	private BigDecimal userAmibCustRef;
	
	private String returnUrl;
	
	private String uuidToken = null;
	
	private BigDecimal languageId;
	
	public BigDecimal getLanguageId() 
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) 
	{
		this.languageId = languageId;
	}
	
	public String getUuidToken() 
	{
		return uuidToken;
	}

	public void setUuidToken(String uuidToken) 
	{
		this.uuidToken = uuidToken;
	}

	public String getReturnUrl() 
	{
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public BigDecimal getUserAmibCustRef()
	{
		return userAmibCustRef;
	}

	public void setUserAmibCustRef(BigDecimal userAmibCustRef)
	{
		this.userAmibCustRef = userAmibCustRef;
	}

	public String getChangePasswordOtp()
	{
		return changePasswordOtp;
	}

	public void setChangePasswordOtp(String changePasswordOtp)
	{
		this.changePasswordOtp = changePasswordOtp;
	}

	public String getMotpPrefix()
	{
		return motpPrefix;
	}

	public void setMotpPrefix(String motpPrefix)
	{
		this.motpPrefix = motpPrefix;
	}

	public String getEotpPrefix()
	{
		return eotpPrefix;
	}

	public void setEotpPrefix(String eotpPrefix)
	{
		this.eotpPrefix = eotpPrefix;
	}

	public String getMotp()
	{
		return motp;
	}

	public void setMotp(String motp)
	{
		this.motp = motp;
	}

	public String getEotp()
	{
		return eotp;
	}

	public void setEotp(String eotp)
	{
		this.eotp = eotp;
	}

	public String getCustomerMobileNumber()
	{
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber)
	{
		this.customerMobileNumber = customerMobileNumber;
	}

	public String getCustomerEmailId()
	{
		return customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId)
	{
		this.customerEmailId = customerEmailId;
	}

	public BigDecimal getCustomerSequenceNumber()
	{
		return customerSequenceNumber;
	}

	public void setCustomerSequenceNumber(BigDecimal customerSequenceNumber)
	{
		this.customerSequenceNumber = customerSequenceNumber;
	}

	public BigDecimal getUserSequenceNumber()
	{
		return userSequenceNumber;
	}

	public void setUserSequenceNumber(BigDecimal userSequenceNumber)
	{
		this.userSequenceNumber = userSequenceNumber;
	}

	public String getmOtpMobileNumber()
	{
		return mOtpMobileNumber;
	}

	public void setmOtpMobileNumber(String mOtpMobileNumber)
	{
		this.mOtpMobileNumber = mOtpMobileNumber;
	}

	public String geteOtpEmailId()
	{
		return eOtpEmailId;
	}

	public void seteOtpEmailId(String eOtpEmailId)
	{
		this.eOtpEmailId = eOtpEmailId;
	}
	
	public boolean isValid()
	{
		return valid;
	}
	
	public void setUserValid(boolean valid)
	{
		this.valid = valid;
	}

	/*public boolean validateSessionUnique()
	{
		if (!isValid())
		{
			return false;
		}
		return true;
	}*/
	
	public boolean validateSessionUnique() 
	{
		if (isValid() && !this.indexedUser()) 
		{
			//logger.info(TAG + " validateSessionUnique :: false");
			return false;
		}
		//logger.info(TAG + " validateSessionUnique :: true");
		return true;
	}
	

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	public void authorize(String civilId, String password)
	{
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(civilId, password);
		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = this.customerAuthProvider.authenticate(token);
		this.indexUser(authentication);
		valid = true;
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public void setReferrer(String referrer)
	{
		this.referrer = referrer;
	}

	public String getReferrer()
	{
		return referrer;
	}
	
	public void unauthorize()
	{
		this.invalidate();
	}
	
	public void invalidate()
	{
		SecurityContextHolder.getContext().setAuthentication(null);
		HttpSession session = request.getSession(false);
		SecurityContextHolder.clearContext();
		session = request.getSession(false);
		if (session != null)
		{
			session.invalidate();
		}
		httpService.clearSessionCookie();
	}
	
	@Override
	public String toString() {
		return "UserSession [loggedInUsers=" + loggedInUsers + ", httpService=" + httpService
				+ ", valid=" + valid + ", referrer=" + referrer + ", civilId=" + civilId + ", motpPrefix=" + motpPrefix
				+ ", eotpPrefix=" + eotpPrefix + ", motp=" + motp + ", eotp=" + eotp + ", customerMobileNumber="
				+ customerMobileNumber + ", customerEmailId=" + customerEmailId + ", changePasswordOtp="
				+ changePasswordOtp + ", mOtpMobileNumber=" + mOtpMobileNumber + ", eOtpEmailId=" + eOtpEmailId
				+ ", customerSequenceNumber=" + customerSequenceNumber + ", userSequenceNumber=" + userSequenceNumber
				+ ", userAmibCustRef=" + userAmibCustRef + ", returnUrl=" + returnUrl + ", uuidToken=" + uuidToken
				+ ", languageId=" + languageId + ", request=" + request + ", customerAuthProvider="
				+ customerAuthProvider + "]";
	}

	
	/****************************************************************************/
	
	public void indexUser(Authentication authentication) 
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String userKeyString = getUserKeyString();
		if (userKeyString != null) 
		{
			RLocalCachedMap<String, String> map = loggedInUsers.map();
			uuidToken = String.valueOf(timestamp.getTime());
			map.fastPut(userKeyString, uuidToken);
		}
	}
	
	public Boolean indexedUser() 
	{
		String userKeyString = getUserKeyString();
		if (userKeyString != null) 
		{

			RLocalCachedMap<String, String> map = loggedInUsers.map();
			if (map.containsKey(userKeyString) && uuidToken != null) 
			{
				String uuidTokenTemp = map.get(userKeyString);
				return uuidToken.equals(uuidTokenTemp);
			}
		}
		return Boolean.FALSE;
	}
	
	public void unIndexUser() 
	{
		if (civilId != null) 
		{
			String userKeyString = getUserKeyString();
			if (userKeyString != null) 
			{
				RLocalCachedMap<String, String> map = loggedInUsers.map();
				map.fastRemove(userKeyString);
			}
		}
	}
	
	public boolean isRequestAuthorized() 
	{
		//logger.info(TAG + " isRequestAuthorized ::");
		
		if (WebSecurityConfig.isPublicUrl(request.getRequestURI())) 
		{
			return true;
		}
		
		if (!this.validateSessionUnique()) 
		{
			//logger.info(TAG + " isRequestAuthorized :: this.validateSessionUnique() is False");
			this.unauthorize();
			return false;
		}
		return true;
	}
	
	private String getUserKeyString() {
		if (civilId == null) {
			return null;
		}
		return String.format(USER_KEY_FORMAT, TenantContextHolder.currentSite().toString(), getCivilId());
	}
	
}
