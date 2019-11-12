package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerModel;

public interface ICustomerRepository extends CrudRepository<CustomerModel, Serializable> {
		CustomerModel findByCustSeqNo(BigDecimal custSeqNo);
}
