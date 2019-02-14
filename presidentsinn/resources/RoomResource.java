package com.dario.presidentsinn.resources;

import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
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
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.models.DataResponse;
import com.dario.presidentsinn.models.ErrorMessage;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.models.User;
import com.dario.presidentsinn.services.AuthenticationService;
import com.dario.presidentsinn.services.AuthorizationService;
import com.dario.presidentsinn.services.RoomService;

@Path("/rooms")
public class RoomResource {
	@Context ServletContext context;
	@Context HttpServletResponse response;
	private Logger log = LogManager.getLogger(this.getClass());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpHeaders httpHeaders)
	{
		log.info("#### Request received - GET to /rooms");
		AuthenticationService authService = new AuthenticationService();
		AuthorizationService otherAuthService = new AuthorizationService();
		RoomService roomService = new RoomService();
		String token = authService.getTokenHeader(httpHeaders);
		User user;
		try
		{
			user = authService.validateToken(token);
			otherAuthService.hotelAgentOnly(user);
			Room[] rooms = roomService.getAllRooms();
			DataResponse message = new DataResponse();
			message.setData(rooms);
			log.info("#### Response sent - Status: 200 - All Rooms");
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/available")
	public Response getAvailable(@Context HttpHeaders httpHeaders, @QueryParam("start") @NotNull @Size(min=10,max=10)String start,
			@QueryParam("end") @NotNull @Size(min=10,max=10)String end)
	{
		AuthenticationService authService = new AuthenticationService();
		RoomService roomService = new RoomService();
		String token = authService.getTokenHeader(httpHeaders);
		User user;
		try
		{
			user = authService.validateToken(token);
			Room[] rooms = roomService.getAvailableRooms(start, end);
			DataResponse message = new DataResponse();
			message.setData(rooms);
			log.info("#### Response sent - Status: 200 - Available Rooms In Range");
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
		catch(ParseException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 400 - Incorrect date format");
			return Response.status(400).entity(error).build();
		}
	}
}
