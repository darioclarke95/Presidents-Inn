package com.dario.presidentsinn.services;

import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.DuplicateException;
import com.dario.presidentsinn.models.Booking;
import com.dario.presidentsinn.models.Guest;
import com.dario.presidentsinn.models.Registration;
import com.dario.presidentsinn.models.User;
import com.dario.presidentsinn.services.DbService;


public class UserService {
	
	public void createGuestUser(Registration input) throws DatabaseException, DuplicateException
	{
		User newUser = input.getAccountDetails();
		newUser.setType("TRAVELLER");
		
		
		DbService dbDriver = new DbService();
		if(dbDriver.isUserEmailUnique(newUser.getEmail()))
		{
			dbDriver.createUser(newUser);
		}
		else 
		{
			throw new DuplicateException("A user with that email already exists");
		}		
		
		newUser = dbDriver.validateUser(newUser.getEmail(), newUser.getPassword());
		Guest newGuest = input.getGuestDetails();	
		newGuest.setUserId(newUser.getId());
		dbDriver.createGuestWithUser(newGuest);
	}
	
	public Booking[] getBookings(int userId) throws DatabaseException
	{
		DbService dbService = new DbService();
		return dbService.getBookingsForUser(userId).toArray(new Booking[1]);
	}

}
