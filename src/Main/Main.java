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
            users = ioHandler.readUsers(filePath);
            for (User user : users) {
                repository.saveUser(user);
                System.out.println("Data loaded successfully.");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading file: " + e.getMessage());
        }

       
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
                        ioHandler.writeUsers(usersToSave, filePath);
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