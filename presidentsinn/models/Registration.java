package com.dario.presidentsinn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Registration {
	@JsonProperty
	private User accountDetails;
	@JsonProperty
	private Guest guestDetails;
	public User getAccountDetails() { return this.accountDetails; }
	public void setAccountDetails(User accountDetails) { this.accountDetails = accountDetails; }
	public Guest getGuestDetails() { return this.guestDetails; }
	public void setGuestDetails(Guest guestDetails){ this.guestDetails = guestDetails; }
}
