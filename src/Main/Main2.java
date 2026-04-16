package Main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import GUI.AuthenticationPanel;
import Repository.UserRepository;
import Scheduler.Scheduler;
import User.User;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import IO.IO;

public class Main2 {
	
	private static User users1;
	private static List<User> users = new ArrayList<>();
	
    public static void main(String[] args) {
        // 1. Setup the Backend
        UserRepository repository = new UserRepository();
        Scheduler scheduler = new Scheduler(8, 23, 7, repository);
        
        IO input = new IO();

        try {
            users = input.readCalendar("src/test/resources/testFileIO.json");
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error loading file: " + e.getMessage());
        }

        for (User user : users) {
            repository.saveUser(user);
        }

        // 3. Launch the UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Schedule System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null); // Centers window

            // Start with the Authentication Panel
            AuthenticationPanel authPanel = new AuthenticationPanel(repository, scheduler);
            frame.setContentPane(authPanel);
            
            frame.setVisible(true);
        });
    }
}
