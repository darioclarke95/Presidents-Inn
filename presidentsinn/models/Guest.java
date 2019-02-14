package com.dario.presidentsinn.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Guest {
	@JsonProperty
	private int id;
	@JsonIgnore
	private int userId;
	@JsonProperty
	@Size(max=30)
	private String fname;
	@JsonProperty
	@Size(max=30)
	private String lname;
	@JsonProperty
	@Size(max=255)
	private String address;
	@JsonProperty
	@Size(min=13,max=19)
	private String creditCardNo; 
	@Size(min=7,max=7)
	private String creditCardExp;
	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public int getUserId() { return this.userId; }
	public void setUserId(int userId) { this.userId = userId; }
	
	public String getFname() { return this.fname; }
	public void setFname(String fname) { this.fname = fname; }
	
	public String getLname() { return this.lname; }
	public void setLname(String lname) { this.lname = lname; }
	
	public String getAddress() { return this.address; }
	public void setAddress(String address) { this.address = address; }
	
	public String getCreditCardNo() { return this.creditCardNo; }
	public void setCreditCardNo(String creditCardNo) { this.creditCardNo = creditCardNo; }
	
	public String getCreditCardExp() { return this.creditCardExp; }
	public void setCreditCardExp(String creditCardExp) { this.creditCardExp = creditCardExp; }
}
