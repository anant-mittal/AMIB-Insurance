package com.amx.jax.models;

import java.math.BigDecimal;

public class VehicleDetails
{
	private String makeCode;

	private String subMakeCode;

	public String vehicleTypeDesc;

	private BigDecimal modelYear;

	private BigDecimal vehicleValue;

	private BigDecimal policyDuration;

	private String purposeCode;

	private String colourCode;

	private String shapeCode;

	private BigDecimal seatingCapacity;

	private String fuelCode;

	private String chasis;

	private String vehicleConditionCode;

	private String ktNumber;

	public String getMakeCode()
	{
		return makeCode;
	}

	public void setMakeCode(String makeCode)
	{
		this.makeCode = makeCode;
	}

	public String getSubMakeCode()
	{
		return subMakeCode;
	}

	public void setSubMakeCode(String subMakeCode)
	{
		this.subMakeCode = subMakeCode;
	}

	public String getVehicleTypeDesc()
	{
		return vehicleTypeDesc;
	}

	public void setVehicleTypeDesc(String vehicleTypeDesc)
	{
		this.vehicleTypeDesc = vehicleTypeDesc;
	}

	public BigDecimal getModelYear()
	{
		return modelYear;
	}

	public void setModelYear(BigDecimal modelYear)
	{
		this.modelYear = modelYear;
	}

	public BigDecimal getVehicleValue()
	{
		return vehicleValue;
	}

	public void setVehicleValue(BigDecimal vehicleValue)
	{
		this.vehicleValue = vehicleValue;
	}

	public BigDecimal getPolicyDuration()
	{
		return policyDuration;
	}

	public void setPolicyDuration(BigDecimal policyDuration)
	{
		this.policyDuration = policyDuration;
	}

	public String getPurposeCode()
	{
		return purposeCode;
	}

	public void setPurposeCode(String purposeCode)
	{
		this.purposeCode = purposeCode;
	}

	public String getColourCode()
	{
		return colourCode;
	}

	public void setColourCode(String colourCode)
	{
		this.colourCode = colourCode;
	}

	public String getShapeCode()
	{
		return shapeCode;
	}

	public void setShapeCode(String shapeCode)
	{
		this.shapeCode = shapeCode;
	}

	public BigDecimal getSeatingCapacity()
	{
		return seatingCapacity;
	}

	public void setSeatingCapacity(BigDecimal seatingCapacity)
	{
		this.seatingCapacity = seatingCapacity;
	}

	public String getFuelCode()
	{
		return fuelCode;
	}

	public void setFuelCode(String fuelCode)
	{
		this.fuelCode = fuelCode;
	}

	public String getChasis()
	{
		return chasis;
	}

	public void setChasis(String chasis)
	{
		this.chasis = chasis;
	}

	public String getVehicleConditionCode()
	{
		return vehicleConditionCode;
	}

	public void setVehicleConditionCode(String vehicleConditionCode)
	{
		this.vehicleConditionCode = vehicleConditionCode;
	}

	public String getKtNumber()
	{
		return ktNumber;
	}

	public void setKtNumber(String ktNumber)
	{
		this.ktNumber = ktNumber;
	}
	
	@Override
	public String toString()
	{
		return "VehicleDetails [makeCode=" + makeCode + ", subMakeCode=" + subMakeCode + ", vehicleTypeDesc=" + vehicleTypeDesc + ", modelYear=" + modelYear + ", vehicleValue=" + vehicleValue + ", policyDuration=" + policyDuration + ", purposeCode=" + purposeCode + ", colourCode=" + colourCode
				+ ", shapeCode=" + shapeCode + ", seatingCapacity=" + seatingCapacity + ", fuelCode=" + fuelCode + ", chasis=" + chasis + ", vehicleConditionCode=" + vehicleConditionCode + ", ktNumber=" + ktNumber + "]";
	}


}
