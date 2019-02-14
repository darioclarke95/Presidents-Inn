package com.dario.presidentsinn.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginMessage {
	@JsonProperty
	private String message;
	@JsonProperty
	private String token;
	public String getMessage(){return message;}
	public void setMessage(String message){this.message = message;}
	public String getToken(){return token;}
	public void setToken(String token){this.token = token;}
}
