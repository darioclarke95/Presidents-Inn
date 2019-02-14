package com.dario.presidentsinn.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

import com.dario.presidentsinn.exceptions.DatabaseException;
import com.dario.presidentsinn.exceptions.InvalidRoomException;
import com.dario.presidentsinn.models.Booking;
import com.dario.presidentsinn.models.CurrentGuest;
import com.dario.presidentsinn.models.Guest;
import com.dario.presidentsinn.models.Hold;
import com.dario.presidentsinn.models.Room;
import com.dario.presidentsinn.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DbService 
{
	private Logger log;
	private DataSource dataSource;
	Context context;
	String configPath;
	
	public DbService()
	{
		this.log = LogManager.getLogger(this.getClass());
		Connection conn = null;
		configPath = "java:/comp/env/jdbc/myDB";
		
		try 
		{
			context = new InitialContext();
			dataSource = (DataSource)context.lookup(configPath);
			conn = getConnection();
			if(conn != null)
			{
				log.info("Database successfully connected");
			}
		}
		catch (Exception ex)
		{
			log.fatal(ex);
		}
		finally 
		{
			try { conn.close(); } catch (Exception ex) { log.fatal(ex); }
		}
	}
	
	private Connection getConnection()
	{
		Connection conn = null;
		try
		{
			conn = dataSource.getConnection();
		}
		catch (SQLException ex)
		{
			log.fatal(ex);
			conn = null;
		}
		
		return conn;
	}
	
	private void closeCalls(Connection conn, Statement statement)
	{
		try { statement.close(); } catch(Exception ex) { log.fatal(ex); }
		try { conn.close(); } catch (Exception ex) { log.fatal(ex); }
		finally { try { conn.close(); } catch (Exception ex) { log.fatal(ex); } }
	}

	
	public String example()
	{
		String result = null;
		CallableStatement statement  = null;
		Connection conn = getConnection();
		
		try
		{
			String query = "show tables";
			statement  = conn.prepareCall(query);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next())
			{
				if(result == null)
				{
					result = "";
				}
				result += rs.getString("Tables_in_presidentsinn");
			}
		}
		catch (Exception ex)
		{
			log.fatal(ex.getMessage());
		}
		finally { closeCalls(conn,statement); }
		return result;
	}
	
	public void createUser(User user) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "insert into users (email, password, type) values (?, ?, ?)";
			statement = conn.prepareStatement(query);
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getType());
			
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not insert user");
		}
		finally { closeCalls(conn,statement); }
	}
	
	public void createGuestWithUser(Guest guest) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "insert into guests (user_id, fname, lname, address, credit_card_no, credit_card_exp) "
					+ "values (?, ?, ?, ?, ?, ?)";
			statement = conn.prepareStatement(query);
			statement.setInt(1, guest.getUserId());
			statement.setString(2, guest.getFname());
			statement.setString(3, guest.getLname());
			statement.setString(4, guest.getAddress());
			statement.setString(5, guest.getCreditCardNo());
			statement.setString(6, guest.getCreditCardExp());
			
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not insert guest");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public void createGuestWithOutUser(Guest guest) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "insert into guests (fname, lname, address, credit_card_no, credit_card_exp) "
					+ "values (?, ?, ?, ?, ?)";
			statement = conn.prepareStatement(query);
			statement.setString(1, guest.getFname());
			statement.setString(2, guest.getLname());
			statement.setString(3, guest.getAddress());
			statement.setString(4, guest.getCreditCardNo());
			statement.setString(5, guest.getCreditCardExp());
			
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not insert guest");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public boolean isUserEmailUnique(String email) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "select * from users where email = ?";
			statement = conn.prepareStatement(query);
			statement.setString(1, email);
			ResultSet rs = statement.executeQuery();
			if(rs.next())
			{
				return false;
			}
			else
			{
				return true;
			}
			
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not verify user");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public User validateUser(String email, String password) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "select * from users where email = ? AND password = ?";
			statement = conn.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			if(rs.next())
			{
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setType(rs.getString("type"));
				return user;
			}
			else
			{
				return null;
			}
			
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not verify user");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public void saveToken(String token, int userId) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "insert into tokens (token, user_id) "
					+ "values (?, ?)";
			statement = conn.prepareStatement(query);
			statement.setString(1, token);
			statement.setInt(2, userId);
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not save token");
		}
		finally { closeCalls(conn, statement); }
	}
	
	
	public User validateToken(String token) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "select * from users where id in (select user_id from tokens where token = ?)";
			statement = conn.prepareStatement(query);
			statement.setString(1, token);
			ResultSet rs = statement.executeQuery();
			if(rs.next())
			{
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setType(rs.getString("type"));		
				return user;
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not validate token");
		}
		finally { closeCalls(conn, statement); }
	}
	
	
	public void removeToken(String token) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "delete from tokens where token = ?";
			statement = conn.prepareStatement(query);
			statement.setString(1, token);
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not delete token");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public HashMap<Integer, Room> getAllRooms() throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
		
		//Rooms that are available
		try
		{
			String query = "select * from rooms";
			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				Room room = new Room();
				room.setId(rs.getInt("id"));
				room.setMaxOccupancy(rs.getInt("max_occupancy"));
				room.setRate(rs.getFloat("rate"));
				room.setType(rs.getString("type"));
				room.setStatus("available");
				rooms.put(room.getId(),room);
			}
			return rooms;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get rooms");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public HashMap<Integer,Room> getTentativelyBookedRooms(Date start, Date end) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		HashMap<Integer, Room> rooms = new HashMap<Integer,Room>();
		
		java.sql.Date startSql = new java.sql.Date(start.getTime());
		java.sql.Date endSql = new java.sql.Date(end.getTime());
		java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
		
		//Rooms that are available
		try
		{
			String query = "select * from rooms where id in (select room_id from holds where "
					+ "((expiry_date >= ?) and (((start_date <= ?) and (end_date >= ?)) or ((start_date <= ?) and (end_date >= ?)))))";
			statement = conn.prepareStatement(query);
			statement.setTimestamp(1, now);
			statement.setDate(2, startSql);
			statement.setDate(3, startSql);
			statement.setDate(4, endSql);
			statement.setDate(5, endSql);
			ResultSet rs = statement.executeQuery();
						
			while (rs.next())
			{
				Room room = new Room();
				room.setId(rs.getInt("id"));
				room.setMaxOccupancy(rs.getInt("max_occupancy"));
				room.setRate(rs.getFloat("rate"));
				room.setType(rs.getString("type"));
				room.setStatus("tentative");
				rooms.put(room.getId(), room);
			}
			return rooms;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get tentatively booked rooms");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public HashMap<Integer, Room> getBookedRooms(Date start, Date end) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
		java.sql.Date startSql = new java.sql.Date(start.getTime());
		java.sql.Date endSql = new java.sql.Date(end.getTime());
		
		//Rooms that are available
		try
		{
			String query = "select * from rooms where id in (select room_id from bookings where "
					+ "(((start_date <= ?) and (end_date >= ?)) or ((start_date <= ?) and (end_date >= ?))))";
			statement = conn.prepareStatement(query);
			statement.setDate(1, startSql);
			statement.setDate(2, startSql);
			statement.setDate(3, endSql);
			statement.setDate(4, endSql);
			ResultSet rs = statement.executeQuery();
						
			while (rs.next())
			{
				Room room = new Room();
				room.setId(rs.getInt("id"));
				room.setMaxOccupancy(rs.getInt("max_occupancy"));
				room.setRate(rs.getFloat("rate"));
				room.setType(rs.getString("type"));
				room.setStatus("unavailable");
				rooms.put(room.getId(), room);
			}
			return rooms;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get booked rooms");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public Room ValidRoom(int roomId) throws DatabaseException, InvalidRoomException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "select * from rooms where id = ?";
			statement = conn.prepareStatement(query);
			statement.setInt(1, roomId);
			ResultSet rs = statement.executeQuery();
			if(!rs.next())
			{
				throw new InvalidRoomException("There is no room with that number");
			}
			else
			{
				Room room = new Room();
				room.setId(rs.getInt("id"));
				room.setMaxOccupancy(rs.getInt("max_occupancy"));
				room.setRate(rs.getFloat("rate"));
				room.setType(rs.getString("type"));
				return room;
			}
			
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not verify room");
		}
		finally { closeCalls(conn, statement); }
	}
	
	
	public void ValidGuest(int guestId) throws DatabaseException, InvalidRoomException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		try
		{
			String query = "select * from guests where id = ?";
			statement = conn.prepareStatement(query);
			statement.setInt(1, guestId);
			ResultSet rs = statement.executeQuery();
			if(!rs.next())
			{
				throw new InvalidRoomException("There is no guest with that ID");
			}			
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not verify guest");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public void createHold(Hold hold) throws DatabaseException, ParseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		
		Date startDate = format.parse(hold.getStart());
		Date endDate = format.parse(hold.getEnd());
		java.sql.Date startSql = new java.sql.Date(startDate.getTime());
		java.sql.Date endSql = new java.sql.Date(endDate.getTime());
		try
		{
			String query = "insert into holds (user_id, room_id, creation_date, expiry_date, start_date, end_date) "
					+ "values (?, ?, (now()), (now() + interval 1 day), ?, ?)";
			statement = conn.prepareStatement(query);
			statement.setInt(1, hold.getUserId());
			statement.setInt(2, hold.getRoomId());
			statement.setDate(3, startSql);
			statement.setDate(4, endSql);
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not insert user");
		}
		finally { closeCalls(conn,statement); }
	}
	
	
	public void createBooking(Booking booking) throws DatabaseException, ParseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		
		Date startDate = format.parse(booking.getStart());
		Date endDate = format.parse(booking.getEnd());
		java.sql.Date startSql = new java.sql.Date(startDate.getTime());
		java.sql.Date endSql = new java.sql.Date(endDate.getTime());
		try
		{
			String query = "insert into bookings (user_id, guest_id, room_id, start_date, end_date, occupants) "
					+ "values (?, ?, ?, ?, ?, ?)";
			statement = conn.prepareStatement(query);
			statement.setInt(1, booking.getUserId());
			statement.setInt(2, booking.getGuestId());
			statement.setInt(3, booking.getRoomId());
			statement.setDate(4, startSql);
			statement.setDate(5, endSql);
			statement.setInt(6, booking.getOccupants());
			statement.execute();
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not insert user");
		}
		finally { closeCalls(conn,statement); }
	}
	
	public HashMap<Integer, CurrentGuest> getAllGuests() throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		HashMap<Integer, CurrentGuest> guests = new HashMap<Integer, CurrentGuest>();
		
		//Rooms that are available
		try
		{
			String query = "select * from guests";
			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				CurrentGuest guest = new CurrentGuest();
				guest.setId(rs.getInt("id"));
				guest.setUserId(rs.getInt("user_id"));
				guest.setFname(rs.getString("fname"));
				guest.setLname(rs.getString("lname"));
				guest.setAddress(rs.getString("address"));
				guest.setCreditCardNo(rs.getString("credit_card_no"));
				guest.setCreditCardExp(rs.getString("credit_card_exp"));
				guest.setRoomId(-1);
				guests.put(guest.getId(), guest);
			}
			return guests;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get guests");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public ArrayList<Booking> getCurrentBookings() throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		//Rooms that are available
		try
		{
			String query = "select * from bookings where end_date >= curdate()";
			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setRoomId(rs.getInt("room_id"));
				booking.setGuestId(rs.getInt("guest_id"));
				booking.setOccupants(rs.getInt("occupants"));
				booking.setStart(rs.getString("start_date"));
				booking.setEnd(rs.getString("end_date"));
				bookings.add(booking);
			}
			return bookings;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get bookings");
		}
		finally { closeCalls(conn, statement); }
	}

	public ArrayList<Hold> getCurrentHolds() throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		ArrayList<Hold> holds = new ArrayList<Hold>();
		
		//Rooms that are available
		try
		{
			String query = "select * from holds where end_date >= curdate() and expiry_date > now()";
			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				Hold hold = new Hold();
				hold.setId(rs.getInt("id"));
				hold.setUserId(rs.getInt("user_id"));
				hold.setRoomId(rs.getInt("room_id"));
				hold.setStart(rs.getString("start_date"));
				hold.setEnd(rs.getString("end_date"));
				holds.add(hold);
			}
			return holds;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get holds");
		}
		finally { closeCalls(conn, statement); }
	}
	
	public ArrayList<Booking> getBookingsForUser(int userId) throws DatabaseException
	{
		PreparedStatement statement = null;
		Connection conn = getConnection();
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		//Rooms that are available
		try
		{
			String query = "select * from bookings where user_id = ?";
			statement = conn.prepareStatement(query);
			statement.setInt(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setRoomId(rs.getInt("room_id"));
				booking.setGuestId(rs.getInt("guest_id"));
				booking.setOccupants(rs.getInt("occupants"));
				booking.setStart(rs.getString("start_date"));
				booking.setEnd(rs.getString("end_date"));
				bookings.add(booking);
			}
			return bookings;
		}
		catch(Exception ex)
		{
			log.fatal(ex);
			throw new DatabaseException("Could not get bookings");
		}
		finally { closeCalls(conn, statement); }
	}
}
