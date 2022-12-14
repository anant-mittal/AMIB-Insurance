package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.PaymentLinkModel;

public interface IPaymentLinkRepository extends CrudRepository<PaymentLinkModel, Serializable> {
		
	PaymentLinkModel findByQuoteSeqNo(BigDecimal quoteSeqNo);
}
