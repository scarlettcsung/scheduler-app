package GUI;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import Authentication.Authentication;
import Event.Event;
import UserService.UserService;
import javax.swing.JOptionPane;


import javax.swing.JLabel;
import java.awt.Color;

import User.User;
import UserCalendar.UserCalendar;
import EventManager.EventManager;
import Invite.Invite;

import Repository.UserRepository;

import Scheduler.Scheduler;


public class AuthenticationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textInUsername;
	private JPasswordField textInPassword;
	private UserRepository repository;
	private Scheduler scheduler;
	private UserService userService;
	private Authentication auth;
	
	/**
	 * Create the panel.
	 */
	public AuthenticationPanel(UserRepository repository, Scheduler scheduler) {
		// Fancy Colors:))
		
	    setBackground(Color.decode("#00FFFF"));
	    setOpaque(true);
	    
	    this.repository = repository;
	    userService = new UserService(repository);
	    auth = new Authentication(repository);
	    
	    //Backend stuff meets with frontend stuff
	    
	    // Main Layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{166, 0, 117, 0};
		gridBagLayout.rowHeights = new int[]{29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblWelcome = new JLabel("Welcome to Schedule");
		GridBagConstraints gbc_lblWelcome = new GridBagConstraints();
		gbc_lblWelcome.insets = new Insets(0, 0, 5, 5);
		gbc_lblWelcome.gridx = 1;
		gbc_lblWelcome.gridy = 3;
		add(lblWelcome, gbc_lblWelcome);
		
		JLabel lblUsername = new JLabel("Username");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 5;
		add(lblUsername, gbc_lblUsername);
		
		textInUsername = new JTextField();
		GridBagConstraints gbc_textInUsername = new GridBagConstraints();
		gbc_textInUsername.insets = new Insets(0, 0, 5, 5);
		gbc_textInUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_textInUsername.gridx = 1;
		gbc_textInUsername.gridy = 5;
		add(textInUsername, gbc_textInUsername);
		textInUsername.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textInUsername.getText();
		        String password = new String(textInPassword.getPassword());
		        
		        boolean success = auth.login(username, password);
		        
		        // EO GI: 5/4/2026 23.42 currentUser upgrade
		        User currentUser = auth.getauthenticatedUser();
		        
			        if (success) {
			            JOptionPane.showMessageDialog(null, "Login successful!");
			            // EO GI: Transition to the admin panel or user dashboard here
			            JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AuthenticationPanel.this);
			            if (currentUser.getCalendar() == null) {
			            	currentUser.setCalendar(new UserCalendar(new ArrayList<>()));
			            }

			            if(currentUser.canAccessAdminPanel()) {
			            	topFrame.setContentPane(new AdminPanel(repository,currentUser, scheduler));
			            } else {
				 topFrame.setContentPane(new UserPanel(repository,currentUser,scheduler));
			            }
		            topFrame.revalidate();
		            topFrame.repaint();
		        } else {
		            JOptionPane.showMessageDialog(null, "Invalid username or password!");
		        }
				
			}
		});
		
		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 6;
		add(lblPassword, gbc_lblPassword);
		
		textInPassword = new JPasswordField();
		GridBagConstraints gbc_textInPassword = new GridBagConstraints();
		gbc_textInPassword.insets = new Insets(0, 0, 5, 5);
		gbc_textInPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_textInPassword.gridx = 1;
		gbc_textInPassword.gridy = 6;
		add(textInPassword, gbc_textInPassword);
		textInPassword.setColumns(10);
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogin.anchor = GridBagConstraints.NORTH;
		gbc_btnLogin.gridx = 1;
		gbc_btnLogin.gridy = 8;
		add(btnLogin, gbc_btnLogin);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String username = textInUsername.getText();
		        String password = new String(textInPassword.getPassword());

		        boolean success = userService.registerUser(username, password);

		        if (success) {
		            JOptionPane.showMessageDialog(null, "User registered!");
		        } else {
		            JOptionPane.showMessageDialog(null, "User already exists!");
		        }
		    }
		});
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.anchor = GridBagConstraints.NORTH;
		gbc_btnRegister.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegister.gridx = 1;
		gbc_btnRegister.gridy = 9;
		add(btnRegister, gbc_btnRegister);
		
		

	}

public static void main(String[] args) {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    UserRepository repository = new UserRepository();
    EventManager eventManager = new EventManager(repository);
    Scheduler scheduler = new Scheduler(8,23,7,repository);

    // Yaratıcı bir test.
    
    User testUser1 = new User("nisa", "1234", null);
    User testUser2 = new User("remzi", "1234", null);
    UserCalendar testCalendar1 = new UserCalendar(null);
    UserCalendar testCalendar2 = new UserCalendar(null);
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
    
    repository.saveUser(testUser1);
    repository.saveUser(testUser2);
    
    //testCalendar1.removeEvent(dummyEvent);
    eventManager.deleteEvent(dummyEvent);
    
    System.out.println(testUser1.getCalendar().getEvents());
    System.out.println(testUser2.getCalendar().getEvents());
    
    
    
    frame.setContentPane(new AuthenticationPanel(repository, scheduler));
    frame.setVisible(true);
}
}
