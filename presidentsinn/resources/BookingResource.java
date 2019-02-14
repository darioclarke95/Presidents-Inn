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
import com.dario.presidentsinn.exceptions.OverCapacityException;
import com.dario.presidentsinn.models.Booking;
import com.dario.presidentsinn.models.DataResponse;
import com.dario.presidentsinn.models.ErrorMessage;
import com.dario.presidentsinn.models.Hold;
import com.dario.presidentsinn.models.LoginMessage;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.models.SuccessMessage;
import com.dario.presidentsinn.models.User;
import com.dario.presidentsinn.services.AuthenticationService;
import com.dario.presidentsinn.services.AuthorizationService;
import com.dario.presidentsinn.services.BookingService;
import com.dario.presidentsinn.services.RoomService;
import com.dario.presidentsinn.services.HoldService;

@Path("/bookings")
public class BookingResource {
	@Context ServletContext context;
	@Context HttpServletResponse response;
	private Logger log = LogManager.getLogger(this.getClass());
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBooking(@Context HttpHeaders httpHeaders, @Valid Booking booking)
	{
		log.info("#### Request received - GET to /bookings");
		AuthenticationService authService = new AuthenticationService();
		BookingService bookingService = new BookingService();
		
		String token = authService.getTokenHeader(httpHeaders);
		User user;
		try
		{
			user = authService.validateToken(token);
			booking.setUserId(user.getId());
			bookingService.createBooking(booking);
			SuccessMessage message = new SuccessMessage();
			message.setMessage("Booking has been made");
			log.info("#### Response sent - Status: 200 - Booking Created");
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
		catch(OverCapacityException ex)
		{
			ErrorMessage error = new ErrorMessage();
			error.setMessage(ex.getMessage());
			log.info("#### Response sent - Status: 400 - Room Over Capacity");
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
}
