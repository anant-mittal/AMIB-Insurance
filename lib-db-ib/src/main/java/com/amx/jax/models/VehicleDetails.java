package com.amx.jax.models;

import java.math.BigDecimal;
import java.sql.Date;

import com.amx.jax.swagger.ApiMockModelProperty;

public class VehicleDetails
{
	@ApiMockModelProperty(example = "ACU", value = "Vehicle Details Make Code ")
	private String makeCode;

	@ApiMockModelProperty(example = "TV6", value = "Vehicle Details Sub Make Code")
	private String subMakeCode;

	@ApiMockModelProperty(example = "MOTOR OWN DAMAGE", value = "Insurance policy type Desc")
	public String vehicleTypeDesc;

	@ApiMockModelProperty(example = "2017", value = "Vehicle Model Year")
	private BigDecimal modelYear;

	@ApiMockModelProperty(example = "1200.000", value = "Vehicle Value")
	private BigDecimal vehicleValue;

	@ApiMockModelProperty(example = "1", value = "Vehicle policy duration")
	private BigDecimal policyDuration;

	@ApiMockModelProperty(example = "BR", value = "Vehicle Purpose Code")
	private String purposeCode;

	@ApiMockModelProperty(example = "ADC", value = "Vehicle Colour Code")
	private String colourCode;

	@ApiMockModelProperty(example = "SAL", value = "Vehicle Shape Code")
	private String shapeCode;

	@ApiMockModelProperty(example = "4", value = "Vehicle Seating Capacity/ Nubmber of Passanger")
	private BigDecimal seatingCapacity;

	@ApiMockModelProperty(example = "B", value = "Vehicle Fuel Type Code")
	private String fuelCode;

	@ApiMockModelProperty(example = "12356451514541554", value = "Vehicle Chasis Number")
	private String chasis;

	@ApiMockModelProperty(example = "N", value = "Vehicle Condition Code")
	private String vehicleConditionCode;

	@ApiMockModelProperty(example = "12345", value = "Vehicle KT Number")
	private String ktNumber;
	
	@ApiMockModelProperty(example = "NEW", value = "Vehicle KT Number")
	private String applicationType;
	
	@ApiMockModelProperty(example = "13-Nov-18", value = "Expected Policy Start Date")
	private String policyStartDate;

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getApplicationType()
	{
		return applicationType;
	}

	public void setApplicationType(String applicationType)
	{
		this.applicationType = applicationType;
	}

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
				+ ", shapeCode=" + shapeCode + ", seatingCapacity=" + seatingCapacity + ", fuelCode=" + fuelCode + ", chasis=" + chasis + ", vehicleConditionCode=" + vehicleConditionCode + ", ktNumber=" + ktNumber + ", applicationType=" + applicationType + "]";
	}

	
	

}
