package com.dario.presidentsinn.services;


import java.text.ParseException;


import com.dario.presidentsinn.exceptions.BookedException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.InvalidRoomException;
import com.dario.presidentsinn.exceptions.OverCapacityException;
import com.dario.presidentsinn.models.Booking;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.services.DbService;

public class BookingService {
	public void createBooking(Booking booking) throws DatabaseException, ParseException, InvalidRoomException, BookedException, OverCapacityException
	{
		RoomService roomService = new RoomService();
		Room room = roomService.ValidRoom(booking.getRoomId());
		roomService.checkBooked(room, booking.getStart(), booking.getEnd());
		DbService service = new DbService();
		service.createBooking(booking);
	}
	
	public void checkRoomCapacity(Room room, Booking booking) throws OverCapacityException
	{
		if(booking.getOccupants() > room.getMaxOccupancy())
		{
			throw new OverCapacityException();
		}
	}
}
