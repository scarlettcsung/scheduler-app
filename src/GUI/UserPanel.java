package GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import User.User;
import UserRepository.UserRepository;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserPanel extends JPanel {

	private static final long serialVersionUID = 1L;


	public UserPanel(UserRepository repository, User currentUser) {
		setBackground(Color.GREEN);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{1, 114, 215, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{1, 29, 29, 29, 29, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
				
		JButton btnDeleteAccount = new JButton("Delete Account");
		btnDeleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SN GI: 6/4/2026 
				int confirm = JOptionPane.showConfirmDialog(UserPanel.this, "Are you sure you want to delete your account?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					repository.deleteUserData(currentUser.getUsername(), currentUser);
					JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(UserPanel.this);
					topFrame.setContentPane(new AuthenticationPanel(repository));
					topFrame.revalidate();
					topFrame.repaint();
				}
			}
		});
		GridBagConstraints gbc_btnDeleteAccount = new GridBagConstraints();
		gbc_btnDeleteAccount.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnDeleteAccount.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteAccount.gridx = 0;
		gbc_btnDeleteAccount.gridy = 0;
		add(btnDeleteAccount, gbc_btnDeleteAccount);

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SN GI: 6/4/2026 
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(UserPanel.this);
				topFrame.setContentPane(new AuthenticationPanel(repository));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		GridBagConstraints gbc_btnLogout = new GridBagConstraints();
		gbc_btnLogout.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnLogout.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogout.gridx = 8;
		gbc_btnLogout.gridy = 12;
		add(btnLogout, gbc_btnLogout);

		JButton btnAvailableEvents = new JButton("Available Events");
		btnAvailableEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		GridBagConstraints gbc_btnAvailableEvents = new GridBagConstraints();
		gbc_btnAvailableEvents.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAvailableEvents.insets = new Insets(0, 0, 5, 5);
		gbc_btnAvailableEvents.gridx = 2;
		gbc_btnAvailableEvents.gridy = 1;
		add(btnAvailableEvents, gbc_btnAvailableEvents);

		JButton btnViewInvites = new JButton("View Invites");
		GridBagConstraints gbc_btnViewInvites = new GridBagConstraints();
		gbc_btnViewInvites.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnViewInvites.insets = new Insets(0, 0, 5, 5);
		gbc_btnViewInvites.gridx = 2;
		gbc_btnViewInvites.gridy = 2;
		add(btnViewInvites, gbc_btnViewInvites);
		
		JButton btnCreateEvent = new JButton("Create Event");
		GridBagConstraints gbc_btnCreateEvent = new GridBagConstraints();
		gbc_btnCreateEvent.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCreateEvent.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreateEvent.gridx = 2;
		gbc_btnCreateEvent.gridy = 3;
		add(btnCreateEvent, gbc_btnCreateEvent);

		JButton btnManageEvent = new JButton("Manage Event");
		btnManageEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// We will have a update event and delete event button.
			}
		});
		GridBagConstraints gbc_btnManageEvent = new GridBagConstraints();
		gbc_btnManageEvent.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnManageEvent.insets = new Insets(0, 0, 5, 5);
		gbc_btnManageEvent.gridx = 2;
		gbc_btnManageEvent.gridy = 4;
		add(btnManageEvent, gbc_btnManageEvent);
				
	}

}
