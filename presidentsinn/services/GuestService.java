package com.dario.presidentsinn.services;

import java.util.*;

import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.models.Booking;
import com.dario.presidentsinn.models.CurrentGuest;
import com.dario.presidentsinn.models.Guest;
import com.dario.presidentsinn.services.DbService;

public class GuestService {
	
	public void createGuest(Guest input) throws DatabaseException
	{		
		DbService dbDriver = new DbService();		
		dbDriver.createGuestWithOutUser(input);
	}
	
	public CurrentGuest[] getAll() throws DatabaseException
	{
		DbService dbDriver = new DbService();		
		HashMap<Integer, CurrentGuest> guests = dbDriver.getAllGuests();
		ArrayList<Booking> bookings = dbDriver.getCurrentBookings();
		guests = this.filterBooked(guests, bookings);
		return this.toArray(guests);
	}
	
	private HashMap<Integer, CurrentGuest> filterBooked(HashMap<Integer, CurrentGuest> guests, ArrayList<Booking> bookings)
	{
		HashMap<Integer, CurrentGuest> result = new HashMap<Integer, CurrentGuest>();
		for(Booking booking : bookings)
		{
			Integer id = booking.getGuestId();
			int roomId = booking.getRoomId();
			if(guests.containsKey(id))
			{
				CurrentGuest guest = guests.get(id);
				guest.setRoomId(roomId);
				guests.put(id, guest);
			}
		}
		
		for(Map.Entry<Integer, CurrentGuest> pair : guests.entrySet())
		{
			Integer key = pair.getKey();
			CurrentGuest value = pair.getValue();
			if(value.getRoomId() != -1)
			{
				result.put(key, value);
			}
		}
		return result;		
			
	}

	private CurrentGuest[] toArray(HashMap<Integer, CurrentGuest> guestsMap)
	{
		ArrayList<CurrentGuest> guests = new ArrayList<CurrentGuest>();
		
		for(Map.Entry<Integer, CurrentGuest> pair : guestsMap.entrySet() )
		{
			guests.add(pair.getValue());
		}
		
		return guests.toArray(new CurrentGuest[1]);
	}
}
