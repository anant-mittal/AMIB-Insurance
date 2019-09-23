package com.amx.jax.paymentlink.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CustomerModel;
import com.amx.jax.dbmodel.OnlinePaymentModel;
import com.amx.jax.dbmodel.PaymentLinkModel;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IOnlinePaymentRepository;
import com.amx.jax.repository.IPaymentLinkRepository;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.services.PayMentService;
import com.amx.jax.ui.session.UserSession;
import com.amx.utils.Constants;

@Component
public class PaymentLinkDao {
	String TAG = "com.amx.jax.dao.PaymentLinkDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(PaymentLinkDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IMetaService metaService;

	@Autowired
	IPaymentLinkRepository iPaymentLinkRepository;

	@Autowired
	CustomizeQuoteService customizeQuoteService;

	@Autowired
	ICustomerRepository iCustomerRepository;

	@Autowired
	UserSession userSession;
	
	@Autowired
	PayMentService payMentService;
	
	@Autowired
	IOnlinePaymentRepository iOnlinePaymentRepository;

	public CustomizeQuoteModel validatePaymentLink(BigDecimal linkId, String verifyCode, BigDecimal languageId,HttpServletRequest request) {
		PaymentLinkModel paymentLinkModel = iPaymentLinkRepository.findOne(linkId);

		userSession.setCustomerSequenceNumber(paymentLinkModel.getCustSeqNo());
		CustomerModel customerModel = iCustomerRepository.findByCustSeqNo(paymentLinkModel.getCustSeqNo());
		userSession.setCivilId(customerModel.getIdNo());
		userSession.setLanguageId(languageId);

		AmxApiResponse<CustomizeQuoteModel, Object> customizeQuoteDetails = customizeQuoteService
				.getCustomizedQuoteDetails(paymentLinkModel.getQuoteSeqNo());

		CustomizeQuoteModel customizeQuoteModel = new CustomizeQuoteModel();

		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("GET_LINK_HASH");
		SqlParameterSource paramMap = new MapSqlParameterSource().addValue("P_CODE", verifyCode).addValue("P_LINKID",
				linkId);
		// First parameter is function output parameter type.
		String verifyHashCode = jdbcCall.executeFunction(String.class, paramMap);
		BigDecimal totalQuoteAmount = new BigDecimal(0);
		if (customizeQuoteDetails.getData() != null) {
			totalQuoteAmount = customizeQuoteDetails.getData().getTotalPremium().getTotalAmount();
		}

		Date now = new Date();
		Date linkDate = paymentLinkModel.getLinkDate();
		long diffInMilli = Math.abs(now.getTime() - linkDate.getTime());
		long diffInDays = TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS);
		AmxApiResponse<PaymentStatus, Object> resp = null;

		if (!(customizeQuoteDetails.getData() == null || (paymentLinkModel.getVerifyCode().equals(verifyHashCode)
				&& paymentLinkModel.getPaymentAmount().compareTo(totalQuoteAmount) == 0))) {
			paymentLinkModel.setIsActive(Constants.PAYMENT_LINK_INVALID);
			customizeQuoteModel.setPaymentLinkStatus(Constants.PAYMENT_LINK_INVALID);
		} else if (paymentLinkModel.getPaymentDate() != null) {
			paymentLinkModel.setIsActive(Constants.PAYMENT_LINK_PAID);
			customizeQuoteModel.setPaymentLinkStatus(Constants.PAYMENT_LINK_PAID);
			List<OnlinePaymentModel> onlinePaymentModelList = iOnlinePaymentRepository
					.findByQuoteSeqNo(paymentLinkModel.getQuoteSeqNo());
			for (OnlinePaymentModel onlinePaymentModel : onlinePaymentModelList) {
				if ("CAPTURED".equalsIgnoreCase(onlinePaymentModel.getResultCode())) {
					resp = payMentService.getPaymentStatus(onlinePaymentModel.getPaySeqNo());
				}
			}
			if (resp.getData() != null) {
				customizeQuoteModel.setPaymentStatus(resp.getData());
			}

		} else if (diffInDays > 1) {
			paymentLinkModel.setIsActive(Constants.PAYMENT_LINK_EXPIRED);
			customizeQuoteModel.setPaymentLinkStatus(Constants.PAYMENT_LINK_EXPIRED);
		} else {
			customizeQuoteModel.setPaymentLinkStatus(Constants.PAYMENT_LINK_ACTIVE);
			customizeQuoteModel.setCustomizeQuoteInfo(customizeQuoteDetails.getData().getCustomizeQuoteInfo());
			customizeQuoteModel.setQuotationDetails(customizeQuoteDetails.getData().getQuotationDetails());
			customizeQuoteModel.setQuoteAddPolicyDetails(customizeQuoteDetails.getData().getQuoteAddPolicyDetails());
			customizeQuoteModel.setTotalPremium(customizeQuoteDetails.getData().getTotalPremium());
			AmxApiResponse<?, Object> response = customizeQuoteService.saveCustomizeQuote(customizeQuoteModel, request);
			customizeQuoteModel.setRedirectUrl(response.getRedirectUrl());
		}
		iPaymentLinkRepository.save(paymentLinkModel);

		return customizeQuoteModel;
	}

}
