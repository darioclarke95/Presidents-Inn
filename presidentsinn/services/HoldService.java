package com.dario.presidentsinn.services;


import java.text.ParseException;

import com.dario.presidentsinn.exceptions.BookedException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.InvalidRoomException;
import com.dario.presidentsinn.models.Hold;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.services.DbService;

public class HoldService {
	public void createHold(Hold hold) throws DatabaseException, ParseException, InvalidRoomException, BookedException
	{
		RoomService roomService = new RoomService();
		Room room = roomService.ValidRoom(hold.getRoomId());
		roomService.checkBooked(room, hold.getStart(), hold.getEnd());
		DbService service = new DbService();
		service.createHold(hold);
	}
	
	public Hold[] getCurrent() throws DatabaseException
	{
		DbService service = new DbService();
		return service.getCurrentHolds().toArray(new Hold[1]);
	}
}
