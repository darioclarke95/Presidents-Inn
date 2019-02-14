package com.dario.presidentsinn.resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dario.presidentsinn.exceptions.AuthenticationException;
import com.dario.presidentsinn.exceptions.AuthorizationException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.DuplicateException;
import com.dario.presidentsinn.models.Booking;
import com.dario.presidentsinn.models.DataResponse;
import com.dario.presidentsinn.models.ErrorMessage;
import com.dario.presidentsinn.models.Registration;
import com.dario.presidentsinn.models.SuccessMessage;
import com.dario.presidentsinn.models.User;
import com.dario.presidentsinn.services.AuthenticationService;
import com.dario.presidentsinn.services.AuthorizationService;
import com.dario.presidentsinn.services.UserService;


@Path("/user")
public class UserResource {
	@Context ServletContext context;
	@Context HttpServletResponse response;
	private Logger log = LogManager.getLogger(this.getClass());
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGuestUser(@Valid Registration input)
	{
		log.info("#### Request received - POST to /user");
		try
		{
			UserService service = new UserService();
			service.createGuestUser(input);
			SuccessMessage message = new SuccessMessage();
			message.setMessage("The new guest account was successfully created");
			log.info("#### Response sent - Status: 200 - The new guest account was successfully created");
			return Response.status(200).entity(message).build();
		}
		catch(DatabaseException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 500 - An error occured");
			return Response.status(500).entity(error).build();
		}
		catch(DuplicateException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 409 - Duplicate entry");
			return Response.status(409).entity(error).build();
		}
	}
	
	@Path("/{user}/bookings")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserBookings(@Context HttpHeaders httpHeaders, @PathParam("user") int userId)
	{
		log.info("#### Request received - GET to /{user}/bookings");
		AuthenticationService authService = new AuthenticationService();
		AuthorizationService otherAuthService = new AuthorizationService();
		UserService userService = new UserService();
		String token = authService.getTokenHeader(httpHeaders);
		User user;
		try
		{
			user = authService.validateToken(token);
			if(user.getId() != userId)
			{
				otherAuthService.hotelAgentOnly(user);
			}
			Booking[] bookings  = userService.getBookings(userId);
			DataResponse message = new DataResponse();
			message.setData(bookings);
			log.info("#### Response sent - Status: 200 - Bookings made by this user");
			return Response.status(200).entity(message).build();
		}
		catch(AuthorizationException ex)
		{
			log.info("#### Response sent - Status: 403 - Forbidden");
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			return Response.status(403).entity(error).build();
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
