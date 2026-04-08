package GUI;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import Authentication.Authentication;
import UserService.UserService;
import javax.swing.JOptionPane;


import javax.swing.JLabel;
import java.awt.Color;

import UserRepository.UserRepository;
import User.User;

public class AuthenticationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textInUsername;
	private JPasswordField textInPassword;
	private UserRepository repository;
	private UserService userService;
	private Authentication auth;
	
	/**
	 * Create the panel.
	 */
	public AuthenticationPanel(UserRepository repository) {
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

		            if(currentUser.canAccessAdminPanel()) {
		            	topFrame.setContentPane(new AdminPanel(repository,currentUser));
		            } else {
		            	topFrame.setContentPane(new UserPanel(repository,currentUser));
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
		
		JButton btnEasterEgg = new JButton("Make Your Day:))");
		GridBagConstraints gbc_btnEasterEgg = new GridBagConstraints();
		gbc_btnEasterEgg.insets = new Insets(0, 0, 5, 0);
		gbc_btnEasterEgg.gridx = 2;
		gbc_btnEasterEgg.gridy = 9;
		add(btnEasterEgg, gbc_btnEasterEgg);
		
		btnEasterEgg.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AuthenticationPanel.this);
			topFrame.setContentPane(new EasterEgg());
			topFrame.revalidate();
			topFrame.repaint();
		}
		});

	}



public static void main(String[] args) {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    UserRepository repository = new UserRepository();
    frame.setContentPane(new AuthenticationPanel(repository));
    frame.setVisible(true);
}
}
