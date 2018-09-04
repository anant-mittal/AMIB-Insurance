package com.amx.jax.models;

import java.math.BigDecimal;

public class VehicleDetailsUpdateRequest
{
	String make;

	String subMake;

	String ktNumber;

	String chasis;

	BigDecimal modelYear;

	String vehicleCondition;

	String purpose;

	String shape;

	String colour;

	String fuelType;

	String engine;

	BigDecimal seatingCapacity;

	BigDecimal vehiclePower;

	BigDecimal weight;

	String replType;

	BigDecimal maxInsuAmount;

	public String getMake()
	{
		return make;
	}

	public void setMake(String make)
	{
		this.make = make;
	}

	public String getSubMake()
	{
		return subMake;
	}

	public void setSubMake(String subMake)
	{
		this.subMake = subMake;
	}

	public String getKtNumber()
	{
		return ktNumber;
	}

	public void setKtNumber(String ktNumber)
	{
		this.ktNumber = ktNumber;
	}

	public String getChasis()
	{
		return chasis;
	}

	public void setChasis(String chasis)
	{
		this.chasis = chasis;
	}

	public BigDecimal getModelYear()
	{
		return modelYear;
	}

	public void setModelYear(BigDecimal modelYear)
	{
		this.modelYear = modelYear;
	}

	public String getVehicleCondition()
	{
		return vehicleCondition;
	}

	public void setVehicleCondition(String vehicleCondition)
	{
		this.vehicleCondition = vehicleCondition;
	}

	public String getPurpose()
	{
		return purpose;
	}

	public void setPurpose(String purpose)
	{
		this.purpose = purpose;
	}

	public String getShape()
	{
		return shape;
	}

	public void setShape(String shape)
	{
		this.shape = shape;
	}

	public String getColour()
	{
		return colour;
	}

	public void setColour(String colour)
	{
		this.colour = colour;
	}

	public String getFuelType()
	{
		return fuelType;
	}

	public void setFuelType(String fuelType)
	{
		this.fuelType = fuelType;
	}

	public String getEngine()
	{
		return engine;
	}

	public void setEngine(String engine)
	{
		this.engine = engine;
	}

	public BigDecimal getSeatingCapacity()
	{
		return seatingCapacity;
	}

	public void setSeatingCapacity(BigDecimal seatingCapacity)
	{
		this.seatingCapacity = seatingCapacity;
	}

	public BigDecimal getVehiclePower()
	{
		return vehiclePower;
	}

	public void setVehiclePower(BigDecimal vehiclePower)
	{
		this.vehiclePower = vehiclePower;
	}

	public BigDecimal getWeight()
	{
		return weight;
	}

	public void setWeight(BigDecimal weight)
	{
		this.weight = weight;
	}

	public String getReplType()
	{
		return replType;
	}

	public void setReplType(String replType)
	{
		this.replType = replType;
	}

	public BigDecimal getMaxInsuAmount()
	{
		return maxInsuAmount;
	}

	public void setMaxInsuAmount(BigDecimal maxInsuAmount)
	{
		this.maxInsuAmount = maxInsuAmount;
	}
}
