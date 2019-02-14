package com.dario.presidentsinn.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hold {
	@JsonProperty
	private int id;
	@JsonProperty
	private int userId;
	@JsonProperty
	private int roomId;
	@JsonProperty
	@NotNull
	@Size(min=10,max=10)
	private String start;
	@JsonProperty
	@NotNull
	@Size(min=10,max=10)
	private String end;

	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public int getUserId() { return this.userId; }
	public void setUserId(int userId) { this.userId = userId; }
	
	public int getRoomId() { return this.roomId; }
	public void setRoomId(int roomId) { this.roomId = roomId; }
	
	public String getStart() { return this.start; }
	public void setStart(String start) { this.start = start; }
	
	public String getEnd() { return this.end; }
	public void setEnd(String end) { this.end = end; }
}
