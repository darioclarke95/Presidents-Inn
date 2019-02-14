package com.dario.presidentsinn.resources;

import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
import com.dario.presidentsinn.exceptions.AuthorizationException;
import com.dario.presidentsinn.exceptions.BookedException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.InvalidRoomException;
import com.dario.presidentsinn.models.CurrentGuest;
import com.dario.presidentsinn.models.DataResponse;
import com.dario.presidentsinn.models.ErrorMessage;
import com.dario.presidentsinn.models.Hold;
import com.dario.presidentsinn.models.LoginMessage;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.models.SuccessMessage;
import com.dario.presidentsinn.models.User;
import com.dario.presidentsinn.services.AuthenticationService;
import com.dario.presidentsinn.services.AuthorizationService;
import com.dario.presidentsinn.services.GuestService;
import com.dario.presidentsinn.services.RoomService;
import com.dario.presidentsinn.services.HoldService;

@Path("/holds")
public class HoldResource {
	@Context ServletContext context;
	@Context HttpServletResponse response;
	private Logger log = LogManager.getLogger(this.getClass());
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createHold(@Context HttpHeaders httpHeaders, @Valid Hold hold)
	{
		log.info("#### Request received - GET to /holds");
		AuthenticationService authService = new AuthenticationService();
		HoldService holdService = new HoldService();
		
		String token = authService.getTokenHeader(httpHeaders);
		User user;
		try
		{
			user = authService.validateToken(token);
			hold.setUserId(user.getId());
			holdService.createHold(hold);
			SuccessMessage message = new SuccessMessage();
			message.setMessage("Tentative booking has been made");
			log.info("#### Response sent - Status: 200 - Hold Created");
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
		catch(InvalidRoomException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 400 - Invalid room_id");
			return Response.status(400).entity(error).build();
		}
		catch(ParseException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 400 - Incorrect date format");
			return Response.status(400).entity(error).build();
		}
		catch(BookedException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 409 - The room has already been booked");
			return Response.status(409).entity(error).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllHolds(@Context HttpHeaders httpHeaders)
	{
		log.info("#### Request received - GET to /rooms");
		AuthenticationService authService = new AuthenticationService();
		AuthorizationService otherAuthService = new AuthorizationService();
		HoldService holdService = new HoldService();
		String token = authService.getTokenHeader(httpHeaders);
		User user;
		try
		{
			user = authService.validateToken(token);
			otherAuthService.hotelAgentOnly(user);
			Hold[] holds = holdService.getCurrent();
			DataResponse message = new DataResponse();
			message.setData(holds);
			log.info("#### Response sent - Status: 200 - Checked in Guests");
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
