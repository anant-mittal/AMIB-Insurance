




package com.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractServiceFactory
{

	@Autowired
	protected MetaData metaData;

	public abstract AbstractService getUserService();

	public abstract AbstractService getCustomerService();

	public abstract AbstractService getCountryService();

}
