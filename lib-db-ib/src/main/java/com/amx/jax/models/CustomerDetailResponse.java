
package com.amx.jax.models;


public class CustomerDetailResponse
{
	CustomerDetails customerDetails;

	CompanySetUp companySetUp;

	public CustomerDetails getCustomerDetails()
	{
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails)
	{
		this.customerDetails = customerDetails;
	}

	public CompanySetUp getCompanySetUp()
	{
		return companySetUp;
	}

	public void setCompanySetUp(CompanySetUp companySetUp)
	{
		this.companySetUp = companySetUp;
	}

}
