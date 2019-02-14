package com.dario.presidentsinn.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {
	@JsonProperty
	@Size(min=1,max=25)
	private String name;
	public String getName(){ return name; }
	public void setName(String name) { this.name = name; }

}
