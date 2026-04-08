package test;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.List;
import java.io.*;

import Event.Event;
import Invite.Invite;
import User.User;
import UserRepository.UserRepository;
import UserCalendar.UserCalendar;
import IO.IO;



public class testIO extends TestCase {
	private UserRepository repository;
	private UserCalendar calendar;
	private IO input;
	
	//readUserList(String filePath)
	//writeUserList(List<User> userList, String filePath)
	//readCalendar(String filePath)
	//writeCalendar(String filePath)
	
	protected void setUp() throws Exception {
		super.setUp();
		input = new IO();
		calendar=new UserCalendar("John", null);
		LocalDateTime eventTime = LocalDateTime.parse("2026-04-08T09:00:00");
		Event fakeevent= new Event("meeting", eventTime, 2, "test event", "John", false, null);
		calendar.addEvent(fakeevent);
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork", calendar, false));
		repository.saveUser(new User("James", "Bond", calendar, true));
	}
	public void testreadcalendar() throws IOException {
		//write a file
		input.writeCalendar(repository.getListUsers(),"src/test/resources/testFileIO.json");
		
		//read the just written file
		List<User> imported = input.readCalendar("src/test/resources/testFileIO.json");
		
		
		
		
		//compare read result with repository
		assertEquals(repository.getListUsers().size(), imported.size());
	}
	
	//(String eventName, LocalDateTime eventTime, int eventDuration, String eventDescription,]
	//User organizer, Boolean isImported,  List<Invite> invites)
	
	
}
