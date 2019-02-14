package com.dario.presidentsinn.resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import org.json.JSONException;
import org.json.JSONObject;

import com.dario.presidentsinn.models.Test;
import com.dario.presidentsinn.services.DbService;


@Path("/hello")
public class TestResource 
{
	@Context ServletContext context;
	
	@Context HttpServletResponse response;
	String message;
	
	//Constructor
	public TestResource()
	{
		message = "Yow I working families";
	}
	
	@GET
	@Path("/db")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() throws JSONException
	{
		DbService dbDriver = new DbService();
		String result = dbDriver.example();
		return Response.status(200).entity(result).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response hello() throws JSONException
	{
		 JSONObject jsonObject = new JSONObject();
		 jsonObject.put("message", message);
		 return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloPost(@Valid Test test)
	{
		return Response.status(200).entity(test).build();
	}
	
	@GET
	@Path("/name/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response hello(@PathParam("param") String name)
	{
		JSONObject obj = null;
		Status status = Status.OK;
		
		try {
			obj = new JSONObject();
			obj.put("message", message + " " + name);
		}
		catch (JSONException ex)
		{
			status = Status.INTERNAL_SERVER_ERROR;
		}
		
		return Response.status(status).entity(obj.toString()).build();
	}
	
	@POST
	@Path("helloFromPost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloFromPost(String requestBody)
	{
		JSONObject obj = new JSONObject(requestBody);
		
		String name = obj.getString("name");
		obj.put("response", "Hello "+ name + " I am an endpoint");
		
		return Response.status(Status.OK).entity(obj.toString()).build();
	}
}
