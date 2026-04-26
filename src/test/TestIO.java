package test;

import junit.framework.TestCase;
import repository.*;
import user.AdminUser;
import user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import event.*;
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
		LocalDateTime eventTime = LocalDateTime.parse("2026-04-08T09:00:00");
		eventManager = new EventManager();
		Event fakeevent= new CreatedEvent("meeting", 2, "test event", null);
		john = new User("John","1234");
		fakeevent.setEventTime(eventTime);
		fakeevent.setOrganizer(john.getUsername());
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork"));
		repository.saveUser(new AdminUser("James", "Bond"));
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
	
	public void testWriteEventsSuccess() throws IOException {
	    String path = "src/test/resources/writeTest.json";
	    
	    List<Event> events = new ArrayList<>();
	    events.add(new ImportedEvent("Imported Meeting", 60, "From Google", new ArrayList<>()));
	    events.add(new CreatedEvent("Local Meeting", 30, "Manual", new ArrayList<>()));

	    input.writeEvents(events, path);

	    File file = new File(path);
	    assertTrue(file.exists());
	}
	
	public void testReadEventsFail() {
	    // Folder instead of file
	    String directoryPath = "src/test/resources"; 
	    
	    List<Event> result = input.readEvents(directoryPath);
	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	}
	
	public void testWriteEventsFail() {
	    String impossiblePath = ""; 

	    List<Event> events = new ArrayList<>();
	    input.writeEvents(events, impossiblePath);
	}
	
	public void testPolymorphismRead() throws IOException {
	    String path = "src/test/resources/polymorphismTest.json";
	    
	    String json = "[" +
	        // isImportedField: true
	        "{\"isImportedField\":true, \"eventName\":\"A\"}," +
	        
	        // legacy)
	        "{\"isImportedField\":false, \"isImported\":true, \"eventName\":\"B\"}," +
	        
	        // false
	        "{\"eventName\":\"C\"}" +
	    "]";

	    java.io.FileWriter fw = new java.io.FileWriter(path);
	    fw.write(json);
	    fw.close();

	    List<Event> result = input.readEvents(path);

	    assertTrue(result.get(0) instanceof ImportedEvent);
	    assertTrue(result.get(1) instanceof ImportedEvent);
	    assertTrue(result.get(2) instanceof CreatedEvent);
	}
	
	public void testReadUsersIOError() throws IOException {
	    java.io.File file = new java.io.File("src/test/resources/locked.json");
	    file.createNewFile();
	    file.setReadable(false); 

	    try {
	        List<User> result = input.readUsers(file.getPath());
	        assertTrue(result.isEmpty());
	    } finally {
	        file.setReadable(true);
	        file.delete();
	    }
	}
	
	
}
