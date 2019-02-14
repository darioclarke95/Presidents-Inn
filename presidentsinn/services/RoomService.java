package com.dario.presidentsinn.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.dario.presidentsinn.exceptions.BookedException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.InvalidRoomException;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.services.DbService;

public class RoomService {
	public Room[] getAllRooms() throws DatabaseException
	{
		DbService service = new DbService();
		HashMap<Integer, Room> rooms = service.getAllRooms();
		HashMap<Integer, Room> tentative = service.getTentativelyBookedRooms(new Date(), new Date());
		HashMap<Integer, Room> booked = service.getBookedRooms(new Date(), new Date());
		
		rooms = this.markTentative(rooms, tentative);
		rooms = this.markBooked(rooms, booked);
		return this.toArray(rooms);
	}
	
	public Room[] getAvailableRooms(String start, String end) throws DatabaseException, ParseException
	{
		DbService service = new DbService();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		
		Date startDate = format.parse(start);
		Date endDate = format.parse(end);
		HashMap<Integer, Room> rooms = service.getAllRooms();
		HashMap<Integer, Room> tentative = service.getTentativelyBookedRooms(startDate, endDate);
		HashMap<Integer, Room> booked = service.getBookedRooms(startDate, endDate);
		
		rooms = this.markTentative(rooms, tentative);
		rooms = this.removeBooked(rooms, booked);
		return this.toArray(rooms);
	}
	
	public Room ValidRoom(int roomId) throws DatabaseException, InvalidRoomException
	{
		DbService service = new DbService();
		return service.ValidRoom(roomId);
	}
	
	public void checkBooked(Room room, String start, String end) throws DatabaseException, BookedException, ParseException
	{
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		Date startDate = format.parse(start);
		Date endDate = format.parse(end);
		DbService service = new DbService();
		HashMap<Integer, Room> booked = service.getBookedRooms(startDate, endDate);
		if(booked.containsKey(room.getId()))
		{
			throw new BookedException("The room has already been booked");
		}
		
	}
	
	private HashMap<Integer, Room> markTentative(HashMap<Integer, Room> rooms, HashMap<Integer, Room> tentative)
	{
		for(Map.Entry<Integer, Room> pair : tentative.entrySet())
		{
			Integer key = pair.getKey();
			Room value = pair.getValue();
			
			if(rooms.containsKey(key))
			{
				rooms.put(key, value);
			}
		}
		return rooms;
	}
	
	private HashMap<Integer, Room> markBooked(HashMap<Integer, Room> rooms, HashMap<Integer, Room> booked)
	{
		for(Map.Entry<Integer, Room> pair : booked.entrySet())
		{
			Integer key = pair.getKey();
			Room value = pair.getValue();
			
			if(rooms.containsKey(key))
			{
				rooms.put(key, value);
			}
		}
		return rooms;
	}
	
	private HashMap<Integer, Room> removeBooked(HashMap<Integer, Room> rooms, HashMap<Integer, Room> booked)
	{
		for(Map.Entry<Integer, Room> pair : booked.entrySet())
		{
			Integer key = pair.getKey();
			
			if(rooms.containsKey(key))
			{
				rooms.remove(key);
			}
		}
		return rooms;
	}
	
	private Room[] toArray(HashMap<Integer, Room> rooms)
	{
		ArrayList<Room> list = new ArrayList<Room>();
		
		for(Map.Entry<Integer, Room> pair : rooms.entrySet())
		{
			list.add(pair.getValue());
		}
		return list.toArray(new Room[1]);
	}
	
	
}
