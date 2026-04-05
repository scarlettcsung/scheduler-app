package GUI;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import UserRepository.UserRepository;

public class AdminPanelDeleteUser extends JPanel {

	private static final long serialVersionUID = 1L;
	private UserRepository repository;

	/**
	 * Create the panel.
	 */
	public AdminPanelDeleteUser(UserRepository repository) {
		
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
		
		
		for(int i = 0; i < repository.getListUsers().size(); i++)
		{
			listModel.addElement(repository.getListUsers().get(i).getUsername());
		}
		JList<String> userList = new JList<>(listModel);
		
		
		
		JScrollPane scrollPane = new JScrollPane(userList);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 5;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
		
		

		
		

	}

}
