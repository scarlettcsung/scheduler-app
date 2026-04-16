package Main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Event.Event;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import GUI.AuthenticationPanel;
import Repository.UserRepository;
import Scheduler.Scheduler;
import User.User;
import UserCalendar.UserCalendar;
import IO.IO;
import Invite.Invite;

public class Main {
    
    private static List<User> users = new ArrayList<>();
    
    public static void main(String[] args) {
        // 1. Setup the Backend
        UserRepository repository = new UserRepository();
        Scheduler scheduler = new Scheduler(8, 23, 7, repository);
        IO ioHandler = new IO(); // Renamed for clarity as it handles both In/Out

        String filePath = "src/test/resources/testFileIO.json";

        // 2. Load Data
        try {
            users = ioHandler.readCalendar(filePath);
            for (User user : users) {
                repository.saveUser(user);
            }
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error loading file: " + e.getMessage());
        }

        User testUser1 = new User("nisa", "1234", null);
        User testUser2 = new User("remzi", "1234", null);
        UserCalendar testCalendar1 = new UserCalendar(testUser1.getUsername(), null);
        UserCalendar testCalendar2 = new UserCalendar(testUser2.getUsername(), null);
        testUser1.setCalendar(testCalendar1);
        testUser2.setCalendar(testCalendar2);

        Event dummyEvent = new Event(
            "Dummy Event",
            60,
            "This is a test event for delete screen",
            testUser1.getUsername(),
            false,
            null
        );

        dummyEvent.setEventTime(LocalDateTime.of(2026, 4, 8, 14, 0));
        dummyEvent.addInvite(new Invite(testUser2.getUsername(), dummyEvent.getEventID()), repository);

        testCalendar1.addEvent(dummyEvent);
        testCalendar2.addEvent(dummyEvent);
        
        System.out.println(testUser1.getCalendar().getEvents());
        
        repository.deleteUserData(testUser1.getUsername(), testUser1);
        repository.deleteUserData(testUser2.getUsername(), testUser2);
        repository.saveUser(testUser1);
        repository.saveUser(testUser2);
        
        // 3. Launch the UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Schedule System");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Change this!
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // 4. Add Window Listener for the "X" button
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        System.out.println("Saving data...");
                        // Get the latest list from repository to ensure all changes are saved
                        List<User> usersToSave = repository.getAll(); 
                        ioHandler.writeCalendar(usersToSave, filePath);
                        System.out.println("Save successful.");
                    } catch (IOException ex) {
                        System.err.println("Failed to save data: " + ex.getMessage());
                    } finally {
                        frame.dispose(); // Close the window
                        System.exit(0);  // Terminate the app
                    }
                }
            });

            AuthenticationPanel authPanel = new AuthenticationPanel(repository, scheduler);
            frame.setContentPane(authPanel);
            frame.setVisible(true);
        });
    }
}