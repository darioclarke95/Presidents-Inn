package com.dario.presidentsinn.resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dario.presidentsinn.exceptions.AuthenticationException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.models.ErrorMessage;
import com.dario.presidentsinn.models.LoginMessage;
import com.dario.presidentsinn.models.SuccessMessage;
import com.dario.presidentsinn.services.AuthenticationService;

@Path("/auth")
public class AuthenticationResource {
	@Context ServletContext context;
	@Context HttpServletResponse response;
	private Logger log = LogManager.getLogger(this.getClass());
	
	@Path("/login")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password)
	{
		log.info("#### Request received - GET to /login");
		try
		{
			AuthenticationService authService = new AuthenticationService();
			String token = authService.login(email, password);
			LoginMessage message = new LoginMessage();
			message.setMessage("You have successfully logged in");
			message.setToken(token);
			return Response.status(200).entity(message).build();
		}
		catch(AuthenticationException ex)
		{
			log.info("#### Response sent - Status: 401 - Invalid Credentials");
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			return Response.status(401).entity(error).build();
		}
		catch(DatabaseException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 500 - An error occured");
			return Response.status(500).entity(error).build();
		}
		
	}
	
	@Path("/logout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpHeaders httpHeaders)
	{
		log.info("#### Request received - POST to /logout");
		AuthenticationService authService = new AuthenticationService();
		String token = authService.getTokenHeader(httpHeaders);
		try
		{
			authService.validateToken(token);
			authService.logout(token);
			SuccessMessage message = new SuccessMessage();
			message.setMessage("Successfully Logged Out");
			return Response.status(200).entity(message).build();
		}
		catch(AuthenticationException ex)
		{
			log.info("#### Response sent - Status: 401 - Invalid Token");
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			return Response.status(401).entity(error).build();
		}
		catch(DatabaseException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 500 - An error occured");
			return Response.status(500).entity(error).build();
		}
	}
}
