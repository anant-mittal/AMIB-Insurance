package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.OnlinePaymentModel;

public interface IOnlinePaymentRepository extends CrudRepository<OnlinePaymentModel, Serializable>{
		OnlinePaymentModel findByPaySeqNo(BigDecimal paySeqNo);
		List<OnlinePaymentModel> findByQuoteSeqNo(BigDecimal quoteSeqNo);
}
