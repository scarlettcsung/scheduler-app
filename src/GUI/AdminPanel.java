package GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import User.User;
import net.miginfocom.swing.MigLayout;
import UserRepository.UserRepository;

public class AdminPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private UserRepository repository;

	// existing repository instance here EO GI: 5/4/2026
	// EO G: 5/4/2026 23.37 adminUser as a parameter
	public AdminPanel(UserRepository repository, User adminUser) {
		
		this.repository = repository;
		setBackground(Color.MAGENTA);
		setLayout(new MigLayout("", "[75px][75px][75px][][][75px][75px][75px]", "[50px][50px][50px][50px][50px][50px]"));
		
		JLabel label = new JLabel("");
		add(label, "cell 0 0,grow");
		
		JLabel label_1 = new JLabel("");
		add(label_1, "cell 1 0,grow");
		
		JLabel label_2 = new JLabel("");
		add(label_2, "cell 2 0,grow");
		
		JLabel label_3 = new JLabel("");
		add(label_3, "cell 5 0,grow");
		
		JLabel label_4 = new JLabel("");
		add(label_4, "cell 6 0,grow");
		
		JLabel label_5 = new JLabel("");
		add(label_5, "cell 7 0,grow");
		
		JLabel label_6 = new JLabel("");
		add(label_6, "cell 0 1,grow");
		
		JLabel label_7 = new JLabel("");
		add(label_7, "cell 1 1,grow");
		
		JLabel label_8 = new JLabel("");
		add(label_8, "cell 2 1,grow");
		
		JButton btnCreateEvent = new JButton("Create Event");
		btnCreateEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AdminPanel.this);
				// chanelled right repository EO GI: 7/4/2026
				topFrame.setContentPane(new AdminPanelCreateEvent(repository, adminUser));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		add(btnCreateEvent, "cell 3 1");
		
		JLabel label_9 = new JLabel("");
		add(label_9, "cell 5 1,grow");
		
		JLabel label_10 = new JLabel("");
		add(label_10, "cell 6 1,grow");
		
		JLabel label_11 = new JLabel("");
		add(label_11, "cell 7 1,grow");
		
		JLabel label_12 = new JLabel("");
		add(label_12, "cell 0 2,grow");
		
		JLabel label_13 = new JLabel("");
		add(label_13, "cell 1 2,grow");
		
		JButton btnUpdateEvents = new JButton("Update Events");
		btnUpdateEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		add(btnUpdateEvents, "cell 3 2,grow");
		
		JLabel label_14 = new JLabel("");
		add(label_14, "cell 5 2,grow");
		
		JLabel label_15 = new JLabel("");
		add(label_15, "cell 6 2,grow");
		
		JLabel label_16 = new JLabel("");
		add(label_16, "cell 7 2,grow");
		
		JLabel label_17 = new JLabel("");
		add(label_17, "cell 0 3,grow");
		
		JLabel label_18 = new JLabel("");
		add(label_18, "cell 1 3,grow");
		
		JButton btnDeleteEvents = new JButton("Delete Events");
		btnDeleteEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AdminPanel.this);
				// chanelled right repository EO GI: 7/4/2026
				topFrame.setContentPane(new AdminPanelDeleteEvent(repository, adminUser));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		
		add(btnDeleteEvents, "cell 3 3,grow");
		
		JLabel label_19 = new JLabel("");
		add(label_19, "cell 5 3,grow");
		
		JLabel label_20 = new JLabel("");
		add(label_20, "cell 6 3,grow");
		
		JLabel label_21 = new JLabel("");
		add(label_21, "cell 7 3,grow");
		
		JLabel label_22 = new JLabel("");
		add(label_22, "cell 0 4,grow");
		
		JLabel label_23 = new JLabel("");
		add(label_23, "cell 1 4,grow");
		
		JButton btnDeleteUser = new JButton("Delete User");
		// connected to delete user panel EO GI: 5/4/2026
		btnDeleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AdminPanel.this);
				// chanelled right repository EO GI: 5/4/2026
				topFrame.setContentPane(new AdminPanelDeleteUser(repository, adminUser));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});	
		add(btnDeleteUser, "cell 3 4,grow");
		
		JLabel label_24 = new JLabel("");
		add(label_24, "cell 5 4,grow");
		
		JLabel label_25 = new JLabel("");
		add(label_25, "cell 6 4,grow");
		
		JLabel label_26 = new JLabel("");
		add(label_26, "cell 7 4,grow");
		
		JLabel label_27 = new JLabel("");
		add(label_27, "cell 0 5,grow");
		
		JLabel label_28 = new JLabel("");
		add(label_28, "cell 1 5,grow");
		
		JLabel label_29 = new JLabel("");
		add(label_29, "cell 2 5,grow");
		
		JLabel label_30 = new JLabel("");
		add(label_30, "cell 5 5,grow");
		
		JLabel label_31 = new JLabel("");
		add(label_31, "cell 6 5,grow");
		
		// EO GI: 3/4/2026 - Logout button added to AdminPanel
		JButton btnLogout = new JButton("Log Out");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// EO GI: Transition back to the authentication panel
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AdminPanel.this);
				topFrame.setContentPane(new AuthenticationPanel(repository));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		add(btnLogout, "flowy,cell 7 5");
		JLabel label_32 = new JLabel("");
		add(label_32, "cell 7 5,grow");	
	}
}
