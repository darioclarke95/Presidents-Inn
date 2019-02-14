package com.dario.presidentsinn.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataResponse {
	@JsonProperty
	private Object data;
	public Object getData(){return data;}
	public void setData(Object data){this.data = data;}
}
