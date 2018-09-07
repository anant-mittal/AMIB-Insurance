package com.amx.jax.models;

import java.math.BigDecimal;

public class Model
{
	private String makeCode;

	private String subMakeCode;

	private String subMakeDesc;

	private BigDecimal seatingCapacity;

	private String shapeCode;

	private String shapeDesc;

	private String vehicleTypeDesc;

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

	public String getSubMakeDesc()
	{
		return subMakeDesc;
	}

	public void setSubMakeDesc(String subMakeDesc)
	{
		this.subMakeDesc = subMakeDesc;
	}

	public BigDecimal getSeatingCapacity()
	{
		return seatingCapacity;
	}

	public void setSeatingCapacity(BigDecimal seatingCapacity)
	{
		this.seatingCapacity = seatingCapacity;
	}

	public String getShapeCode()
	{
		return shapeCode;
	}

	public void setShapeCode(String shapeCode)
	{
		this.shapeCode = shapeCode;
	}

	public String getShapeDesc()
	{
		return shapeDesc;
	}

	public void setShapeDesc(String shapeDesc)
	{
		this.shapeDesc = shapeDesc;
	}

	public String getVehicleTypeDesc()
	{
		return vehicleTypeDesc;
	}

	public void setVehicleTypeDesc(String vehicleTypeDesc)
	{
		this.vehicleTypeDesc = vehicleTypeDesc;
	}
	
	@Override
	public String toString()
	{
		return "Model [makeCode=" + makeCode + ", subMakeCode=" + subMakeCode + ", subMakeDesc=" + subMakeDesc + ", seatingCapacity=" + seatingCapacity + ", shapeCode=" + shapeCode + ", shapeDesc=" + shapeDesc + ", vehicleTypeDesc=" + vehicleTypeDesc + "]";
	}

	
}
