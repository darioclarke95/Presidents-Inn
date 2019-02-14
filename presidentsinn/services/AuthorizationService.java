package com.dario.presidentsinn.services;

import com.dario.presidentsinn.exceptions.AuthorizationException;

import com.dario.presidentsinn.models.User;


/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
*/
public class AuthorizationService {
	
	public void hotelAgentOnly(User user) throws AuthorizationException
	{
		if(!user.getType().equals("HOTEL_AGENT"))
		{
			throw new AuthorizationException("You do not have permission to perform this action");
		}
	}

}
