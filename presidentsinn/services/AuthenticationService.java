package com.dario.presidentsinn.services;


import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;

import com.dario.presidentsinn.exceptions.AuthenticationException;
import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.models.User;

public class AuthenticationService {
	public String login(String email, String password) throws AuthenticationException, DatabaseException
	{
		DbService dbService = new DbService();
		User user = dbService.validateUser(email, password);
		String token;
	
		if(user == null)
		{
			throw new AuthenticationException("Invalid username or password");
		}
		else
		{
			token = UUID.randomUUID().toString();
			dbService.saveToken(token, user.getId());
			return token;
		}
	}
	
	public void logout(String token) throws DatabaseException
	{
		DbService dbService = new DbService();
		dbService.removeToken(token);
	}
	
	public User validateToken(String token) throws AuthenticationException, DatabaseException
	{
		DbService dbService = new DbService();
		User user = dbService.validateToken(token);
		if(user == null)
		{
			throw new AuthenticationException("Invalid token");
		}
		else
		{
			return user;
		}
	}
	
	public String getTokenHeader(HttpHeaders httpHeaders)
	{
		String token = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
		token = token.substring("Bearer".length()).trim();
		return token;
	}
}
