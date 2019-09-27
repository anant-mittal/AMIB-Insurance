package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="IRB_ONLINE_CUSTOMERS")
public class CustomerModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CUST_SEQNO")
	private BigDecimal custSeqNo;
	
	@Column(name="AMIB_CUSTCD")
	private BigDecimal amibCustCode;
	
	@Column(name="CRE_BY")
	private BigDecimal createdBy;
	
	@Column(name="CRE_ON")
	private Date createdOn;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="ID_NO")
	private String idNo;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="USER_SEQNO")
	private BigDecimal userSeqNo;
	
	public BigDecimal getUserSeqNo() {
		return userSeqNo;
	}
	public void setUserSeqNo(BigDecimal userSeqNo) {
		this.userSeqNo = userSeqNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public BigDecimal getCustSeqNo() {
		return custSeqNo;
	}
	public void setCustSeqNo(BigDecimal custSeqNo) {
		this.custSeqNo = custSeqNo;
	}
	
	public BigDecimal getAmibCustCode() {
		return amibCustCode;
	}
	public void setAmibCustCode(BigDecimal amibCustCode) {
		this.amibCustCode = amibCustCode;
	}
	
	public BigDecimal getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	

}
