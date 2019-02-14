package com.dario.presidentsinn.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonProperty
	private int id;
	@JsonProperty
	@Size(min=1,max=30)
	private String email;
	@JsonProperty
	@Size(min=1,max=30)
	private String password;
	@JsonProperty
	@Size(max=25)
	private String type;
	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	public String  getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return this.password; }
	public void setPassword(String password) { this.password = password; }
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
}
