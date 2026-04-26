package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import gui.AuthenticationPanel;
import event.Event;
import io.IO;
import repository.EventRepository;
import repository.UserRepository;
import scheduler.Scheduler;
import user.AdminUser;
import user.User;

/**
 * The entry point for the SmartCalendar application.
 * <p>
 * This class is responsible for initializing the backend components (Repository, Scheduler, Io),
 * loading existing user data from storage, and launching the graphical interface.
 * It also puts data in storage to use for the next start of the program.
 * </p>
 * * @NS CR 
 * @version 3
 */
public class Main {
    /**
     * Main method that launches the application.
     * * <ol>
     * <li>Initializes core services: {@link UserRepository}, {@link Scheduler}, and {@link iO}.</li>
     * <li>Loads user data from a predefined JSON file path.</li>
     * <li>Sets up the main {@link JFrame} and attaches a {@link WindowAdapter} to handle 
     * data persistence upon closing.</li>
     * <li>Displays the {@link AuthenticationPanel} to the user.</li>
     * </ol>
     * * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // 1. Setup the Backend
        UserRepository repository = new UserRepository();
        EventRepository eventRepository = new EventRepository();
        repository.setEventRepository(eventRepository);
        Scheduler scheduler = new Scheduler(8, 23, 7, repository,eventRepository);
        IO ioHandler = new IO(); 

        String filePath = "src/filestorage/userStorage.json";

        // 2. Load Data
        List<User> loadedUsers = ioHandler.readUsers(filePath);
        for (User user : loadedUsers) {
            if (user.getUsername().equals("admin")) {
            	AdminUser adminUser = new AdminUser(user.getUsername(), user.getPassword());
            	repository.saveUser(adminUser);
            } else {
                repository.saveUser(user);
            }
        }
        
        String filePathEvent = "src/filestorage/eventStorage.json";
        List<Event> loadedEvents = ioHandler.readEvents(filePathEvent);
        for (Event event : loadedEvents) {
        	eventRepository.save(event);
            }

        System.out.println("Data loaded successfully.");

        // 3. Launch the UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Schedule System");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

         // 4. Add Window Listener for the "X" button
            frame.addWindowListener(new WindowAdapter() {
                /**
                 * Intercepts the window closing event to save current repository data
                 * to the JSON file before exiting.
                 * @param e The window event.
                 */
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Saving data...");
                    List<User> usersToSave = repository.getAll();
                    List<Event> eventsToSave = eventRepository.getAll();
                    ioHandler.writeUsers(usersToSave, filePath);
                    ioHandler.writeEvents(eventsToSave, filePathEvent);
                    System.out.println("Save successful.");
                    frame.dispose();
                    System.exit(0);
                }
            });

            AuthenticationPanel authPanel = new AuthenticationPanel(repository, scheduler, eventRepository);
            frame.setContentPane(authPanel);
            frame.setVisible(true);
        });
    }
}
