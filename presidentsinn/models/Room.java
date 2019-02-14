package com.dario.presidentsinn.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {
	@JsonProperty
	private int id;
	@JsonProperty
	private String type;
	@JsonProperty
	private int maxOccupancy;
	@JsonProperty
	private float rate;
	@JsonProperty
	private String status;
	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
	
	public int getMaxOccupancy() { return this.maxOccupancy; }
	public void setMaxOccupancy(int maxOccupancy) { this.maxOccupancy = maxOccupancy; }
	
	public float getRate() { return this.rate; }
	public void setRate(float rate) { this.rate = rate; }
	
	public String getStatus() { return this.status; }
	public void setStatus(String status) { this.status = status; }
}
