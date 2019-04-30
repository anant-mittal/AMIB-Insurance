package com.amx.jax.models;

import java.math.BigDecimal;

public class PolicyReceiptDetails {
	private String policyNumber;

	private String policyIssueDate;

	private String policyFromDate;

	private String policyDueDate;

	private String additionalCoverage;

	private String insuranceCo;

	private String make;

	private String subMake;

	private BigDecimal modelYear;

	private String chaisisNumber;

	private String ktNumber;

	private BigDecimal vehicleValue;

	private String purpose;

	private String colour;

	private String shape;

	private BigDecimal capacity;

	private String fuelType;

	private String vehicleCondition;

	private String insuredName;

	private String insuredAddress;

	private String insuredMobileNo;

	private BigDecimal policyContribution;

	private BigDecimal supervisionFees;

	private BigDecimal issueFees;

	private BigDecimal endrosMentFees;

	private BigDecimal discountAmount;

	private BigDecimal amountPaidInNum;

	private String amountPaidInWord;
	
	private String receiptReference;

	public String getReceiptReference() {
		return receiptReference;
	}

	public void setReceiptReference(String receiptReference) {
		this.receiptReference = receiptReference;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyIssueDate() {
		return policyIssueDate;
	}

	public void setPolicyIssueDate(String policyIssueDate) {
		this.policyIssueDate = policyIssueDate;
	}

	public String getPolicyFromDate() {
		return policyFromDate;
	}

	public void setPolicyFromDate(String policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public String getPolicyDueDate() {
		return policyDueDate;
	}

	public void setPolicyDueDate(String policyDueDate) {
		this.policyDueDate = policyDueDate;
	}

	public String getAdditionalCoverage() {
		return additionalCoverage;
	}

	public void setAdditionalCoverage(String additionalCoverage) {
		this.additionalCoverage = additionalCoverage;
	}

	public String getInsuranceCo() {
		return insuranceCo;
	}

	public void setInsuranceCo(String insuranceCo) {
		this.insuranceCo = insuranceCo;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getSubMake() {
		return subMake;
	}

	public void setSubMake(String subMake) {
		this.subMake = subMake;
	}

	public BigDecimal getModelYear() {
		return modelYear;
	}

	public void setModelYear(BigDecimal modelYear) {
		this.modelYear = modelYear;
	}

	public String getChaisisNumber() {
		return chaisisNumber;
	}

	public void setChaisisNumber(String chaisisNumber) {
		this.chaisisNumber = chaisisNumber;
	}

	public String getKtNumber() {
		return ktNumber;
	}

	public void setKtNumber(String ktNumber) {
		this.ktNumber = ktNumber;
	}

	public BigDecimal getVehicleValue() {
		return vehicleValue;
	}

	public void setVehicleValue(BigDecimal vehicleValue) {
		this.vehicleValue = vehicleValue;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsuredAddress() {
		return insuredAddress;
	}

	public void setInsuredAddress(String insuredAddress) {
		this.insuredAddress = insuredAddress;
	}

	public String getInsuredMobileNo() {
		return insuredMobileNo;
	}

	public void setInsuredMobileNo(String insuredMobileNo) {
		this.insuredMobileNo = insuredMobileNo;
	}

	public BigDecimal getPolicyContribution() {
		return policyContribution;
	}

	public void setPolicyContribution(BigDecimal policyContribution) {
		this.policyContribution = policyContribution;
	}

	public BigDecimal getSupervisionFees() {
		return supervisionFees;
	}

	public void setSupervisionFees(BigDecimal supervisionFees) {
		this.supervisionFees = supervisionFees;
	}

	public BigDecimal getIssueFees() {
		return issueFees;
	}

	public void setIssueFees(BigDecimal issueFees) {
		this.issueFees = issueFees;
	}

	public BigDecimal getEndrosMentFees() {
		return endrosMentFees;
	}

	public void setEndrosMentFees(BigDecimal endrosMentFees) {
		this.endrosMentFees = endrosMentFees;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getAmountPaidInNum() {
		return amountPaidInNum;
	}

	public void setAmountPaidInNum(BigDecimal amountPaidInNum) {
		this.amountPaidInNum = amountPaidInNum;
	}

	public String getAmountPaidInWord() {
		return amountPaidInWord;
	}

	public void setAmountPaidInWord(String amountPaidInWord) {
		this.amountPaidInWord = amountPaidInWord;
	}

	public String getVehicleCondition() {
		return vehicleCondition;
	}

	public void setVehicleCondition(String vehicleCondition) {
		this.vehicleCondition = vehicleCondition;
	}
	
	@Override
	public String toString() {
		return "PolicyReceiptDetails [policyNumber=" + policyNumber + ", policyIssueDate=" + policyIssueDate
				+ ", policyFromDate=" + policyFromDate + ", policyDueDate=" + policyDueDate + ", additionalCoverage="
				+ additionalCoverage + ", insuranceCo=" + insuranceCo + ", make=" + make + ", subMake=" + subMake
				+ ", modelYear=" + modelYear + ", chaisisNumber=" + chaisisNumber + ", ktNumber=" + ktNumber
				+ ", vehicleValue=" + vehicleValue + ", purpose=" + purpose + ", colour=" + colour + ", shape=" + shape
				+ ", capacity=" + capacity + ", fuelType=" + fuelType + ", vehicleCondition=" + vehicleCondition
				+ ", insuredName=" + insuredName + ", insuredAddress=" + insuredAddress + ", insuredMobileNo="
				+ insuredMobileNo + ", policyContribution=" + policyContribution + ", supervisionFees="
				+ supervisionFees + ", issueFees=" + issueFees + ", endrosMentFees=" + endrosMentFees
				+ ", discountAmount=" + discountAmount + ", amountPaidInNum=" + amountPaidInNum + ", amountPaidInWord="
				+ amountPaidInWord + ", reciptReference="+ receiptReference+ "]";
	}
	
}
