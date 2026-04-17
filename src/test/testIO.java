package test;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.List;
import java.io.*;

import Event.Event;
import Invite.Invite;
import Repository.UserRepository;
import User.AdminUser;
import User.User;
import UserCalendar.UserCalendar;
import IO.IO;


/**
 * Unit tests for {@link IO.IO}.
 *
 * @author AA NJ
 * @version 2
 */
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
		calendar=new UserCalendar(null);
		LocalDateTime eventTime = LocalDateTime.parse("2026-04-08T09:00:00");
		Event fakeevent= new Event("meeting", 2, "test event", "John", false, null);
		fakeevent.setEventTime(eventTime);
		calendar.addEvent(fakeevent);
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork", calendar));
		repository.saveUser(new AdminUser("James", "Bond", calendar));
	}
	
	//(String eventName, int eventDuration, String eventDescription,
    //String organizerUsername, Boolean isImported,  List<Invite> invites)
	public void testreadcalendar() throws IOException {
		//write a file
		input.writeUsers(repository.getAll(),"src/test/resources/testFileIO.json");
		
		//read the just written file
		List<User> imported = input.readUsers("src/test/resources/testFileIO.json");
		
		//compare read result with repository
		assertEquals(repository.getAll().size(), imported.size());
	}
	
	//(String eventName, LocalDateTime eventTime, int eventDuration, String eventDescription,]
	//User organizer, Boolean isImported,  List<Invite> invites)
	
	
}
