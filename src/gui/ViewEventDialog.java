package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;
import user.User;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Font;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JList;

public class ViewEventDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Event testEvent = new CreatedEvent("Event Name", 30,"Mendel",null);
			EventManager eventManager = new EventManager();
			testEvent.setOrganizer("nisa");
			LocalDateTime eventTimeTest = LocalDateTime.of(2026,04,20,16,00);
			testEvent.setEventTime(eventTimeTest);
			ViewEventDialog dialog = new ViewEventDialog(testEvent);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ViewEventDialog(Event event) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblEventName = new JLabel(event.getEventName());
			lblEventName.setFont(new Font("Tahoma", Font.PLAIN, 18));
			GridBagConstraints gbc_lblEventName = new GridBagConstraints();
			gbc_lblEventName.anchor = GridBagConstraints.SOUTHWEST;
			gbc_lblEventName.insets = new Insets(0, 0, 5, 0);
			gbc_lblEventName.gridx = 0;
			gbc_lblEventName.gridy = 0;
			contentPanel.add(lblEventName, gbc_lblEventName);
		}
		{
			JLabel lblDateTime = new JLabel(event.getTimeString());
			lblDateTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
			GridBagConstraints gbc_lblDateTime = new GridBagConstraints();
			gbc_lblDateTime.insets = new Insets(0, 0, 5, 0);
			gbc_lblDateTime.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblDateTime.gridx = 0;
			gbc_lblDateTime.gridy = 1;
			contentPanel.add(lblDateTime, gbc_lblDateTime);
		}
		{
			JLabel lblDescription = new JLabel("Description");
			lblDescription.setFont(new Font("Tahoma", Font.BOLD, 12));
			GridBagConstraints gbc_lblDescription = new GridBagConstraints();
			gbc_lblDescription.anchor = GridBagConstraints.WEST;
			gbc_lblDescription.insets = new Insets(0, 0, 5, 0);
			gbc_lblDescription.gridx = 0;
			gbc_lblDescription.gridy = 2;
			contentPanel.add(lblDescription, gbc_lblDescription);
		}
		{
			JLabel lblDescription = new JLabel(event.getEventDescription());
			lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
			GridBagConstraints gbc_lblDescription = new GridBagConstraints();
			gbc_lblDescription.anchor = GridBagConstraints.WEST;
			gbc_lblDescription.insets = new Insets(0, 0, 5, 0);
			gbc_lblDescription.gridx = 0;
			gbc_lblDescription.gridy = 3;
			contentPanel.add(lblDescription, gbc_lblDescription);
		}
		{
			JLabel lblParticipants = new JLabel("Participants");
			lblParticipants.setFont(new Font("Tahoma", Font.BOLD, 12));
			GridBagConstraints gbc_lblParticipants = new GridBagConstraints();
			gbc_lblParticipants.insets = new Insets(0, 0, 5, 0);
			gbc_lblParticipants.anchor = GridBagConstraints.WEST;
			gbc_lblParticipants.gridx = 0;
			gbc_lblParticipants.gridy = 4;
			contentPanel.add(lblParticipants, gbc_lblParticipants);
		}
		{
			List<String> participantsList = event.getParticipants();
			String[] participantsArray = participantsList.toArray(new String[0]);
			
			JList<String> listParticipants = new JList<>(participantsArray);
			JScrollPane scrollPane = new JScrollPane(listParticipants);
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.gridheight = 2;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 5;
			gbc_scrollPane.weighty = 1.0;
			contentPanel.add(scrollPane, gbc_scrollPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(e -> dispose());
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
