package GUI;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import Repository.EventRepository;
import Repository.UserRepository;
import Scheduler.Scheduler;

import javax.swing.JScrollBar;

import User.User;

public class AdminPanelDeleteUser extends JPanel {

	private static final long serialVersionUID = 1L;
	private UserRepository repository;

	/**
	 * Create the panel.
	 */
	// EO GI: 5/4/2026 23.36 adding currentUser as a parameter to correctly use delete_user_data method from user_repository
	public AdminPanelDeleteUser(UserRepository repository, User adminUser, Scheduler scheduler,EventRepository eventRepository) {
		
		this.repository = repository;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Choose a username to delete");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 5;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		// List of users to delete EO GI: 5/4/2026
		DefaultListModel<String> listModel = new DefaultListModel<>();
		
		
		for(int i = 0; i < repository.getAll().size(); i++)
		{
			listModel.addElement(repository.getAll().get(i).getUsername());
		}
		JList<String> userList = new JList<>(listModel);
		
		JButton btnBackToAdminPanel = new JButton("Admin Panel");
		btnBackToAdminPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AdminPanelDeleteUser.this);
				// chanelled right repository EO GI: 5/4/2026
				topFrame.setContentPane(new AdminPanel(repository, adminUser,scheduler,eventRepository));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		GridBagConstraints gbc_btnBackToAdminPanel = new GridBagConstraints();
		gbc_btnBackToAdminPanel.insets = new Insets(0, 0, 0, 5);
		gbc_btnBackToAdminPanel.gridx = 0;
		gbc_btnBackToAdminPanel.gridy = 4;
		add(btnBackToAdminPanel, gbc_btnBackToAdminPanel);
		
		JButton btnDeleteSelectedUser = new JButton("Delete Selected User");
		GridBagConstraints gbc_btnDeleteSelectedUser = new GridBagConstraints();
		gbc_btnDeleteSelectedUser.insets = new Insets(0, 0, 0, 5);
		gbc_btnDeleteSelectedUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteSelectedUser.gridx = 5;
		gbc_btnDeleteSelectedUser.gridy = 4;
		add(btnDeleteSelectedUser, gbc_btnDeleteSelectedUser);


		btnDeleteSelectedUser.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String selectedUsername = userList.getSelectedValue();

		        if (selectedUsername == null) {
		        	JOptionPane.showMessageDialog(null, "No user selected :(");

		            return;
		        }

		        int result = repository.deleteUserData(selectedUsername, adminUser);

		        if (result == 2) {
		            listModel.removeElement(selectedUsername);
		            JOptionPane.showMessageDialog(null, "User deleted successfully!");

		        } else {
		        	JOptionPane.showMessageDialog(null, "Problem occured :(");

		        }
		    }
		});

		JScrollPane scrollPane = new JScrollPane(userList);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 5;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
	}

}
