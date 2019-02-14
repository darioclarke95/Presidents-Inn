package com.dario.presidentsinn;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfiguration extends ResourceConfig{
	
	public AppConfiguration()
	{
		packages("com.dario.presidentsinn");
		register(JacksonFeature.class);
	}

}
