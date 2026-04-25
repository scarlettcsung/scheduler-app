package test;

import junit.framework.TestCase;
import repository.UserRepository;
import user.AdminUser;
import user.User;
import user.calendar.UserCalendar;

import java.time.LocalDateTime;
import java.util.List;
import java.io.*;

import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;
import invite.Invite;
import io.IO;


/**
 * Unit tests for {@link io.Io}.
 *
 * @author AA NJ
 * @version 2
 */
public class TestIO extends TestCase {
	private UserRepository repository;
	private UserCalendar calendar;
	private IO input;
	private EventManager eventManager;
	private User john;
	
	//readUserList(String filePath)
	//writeUserList(List<User> userList, String filePath)
	//readCalendar(String filePath)
	//writeCalendar(String filePath)
	
	protected void setUp() throws Exception {
		super.setUp();
		input = new IO();
		calendar=new UserCalendar(null);
		LocalDateTime eventTime = LocalDateTime.parse("2026-04-08T09:00:00");
		eventManager = new EventManager();
		Event fakeevent= new CreatedEvent("meeting", 2, "test event", null);
		john = new User("John","1234",null);
		fakeevent.setEventTime(eventTime);
		eventManager.setOrganizer(fakeevent, john);
		calendar.addEvent(fakeevent);
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork", calendar));
		repository.saveUser(new AdminUser("James", "Bond", calendar));
	}
	
	//(String eventName, int eventDuration, String eventDescription,
    //String organizerUsername, Boolean isImported,  List<Invite> invites)
	public void testReadCalendar() throws IOException {
		//write a file
		input.writeUsers(repository.getAll(),"src/test/resources/testFileIO.json");
		
		//read the just written file
		List<User> imported = input.readUsers("src/test/resources/testFileIO.json");
		
		//compare read result with repository
		assertEquals(repository.getAll().size(), imported.size());
	}
	
	//(String eventName, LocalDateTime eventTime, int eventDuration, String eventDescription,]
	//User organizer, Boolean isImported,  List<Invite> invites)
	
	public void testReadUsers_FileNotFound_ReturnsEmptyList() {
	    List<User> result = input.readUsers("src/test/resources/doesNotExist.json");
	    
	    assertNotNull("Result should not be null", result);
	    assertTrue("Result should be empty when file is not found", result.isEmpty());
	}
	
	public void testWriteUsers_IOException_UnwritableFile() throws IOException {
	    File unwritable = new File("src/test/resources/unwritable.json");

	    // Create the file and make it unwritable
	    unwritable.createNewFile();
	    unwritable.setWritable(false);

	    try {
	        input.writeUsers(repository.getAll(), unwritable.getPath());
	        // If we reach here, the catch block handled it without throwing
	    } finally {
	        // Restore permissions and clean up
	        unwritable.setWritable(true);
	        unwritable.delete();
	    }
	}
	
	
	
}
