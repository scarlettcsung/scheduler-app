package main;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import GUI.AuthenticationPanel;
import event.Event;
import io.IO;
import repository.EventRepository;
import repository.UserRepository;
import scheduler.Scheduler;
import user.AdminUser;
import user.User;

public class Main {
    public static void main(String[] args) {
        // 1. Setup the Backend
        UserRepository repository = new UserRepository();
        EventRepository eventRepository = new EventRepository();
        repository.setEventRepository(eventRepository);
        Scheduler scheduler = new Scheduler(8, 23, 7, repository, eventRepository);
        IO ioHandler = new IO();

        // 2. Load Data
        List<User> loadedUsers = ioHandler.readUsers();
        for (User user : loadedUsers) {
            if (user.getUsername().equals("admin")) {
                AdminUser adminUser = new AdminUser(user.getUsername(), user.getPassword(), user.getCalendar());
                repository.saveUser(adminUser);
            } else {
                repository.saveUser(user);
            }
        }
        for (User user : repository.getAll()) {
            if (user.getCalendar() == null || user.getCalendar().getEvents() == null) {
                continue;
            }
            for (Event event : user.getCalendar().getEvents()) {
                if (eventRepository.getItemByID(String.valueOf(event.getEventID())) == null) {
                    eventRepository.save(event);
                }
            }
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
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Saving data...");
                    List<User> usersToSave = repository.getAll();
                    ioHandler.writeUsers(usersToSave);
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