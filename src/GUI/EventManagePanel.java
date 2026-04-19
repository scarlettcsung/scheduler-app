package GUI;

// Util imports
import java.util.List;
import java.util.ArrayList;

// GUI related imports
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JFormattedTextField;
import java.text.ParseException;
import java.time.LocalDateTime;
import com.github.lgooddatepicker.components.DatePicker;

// Back-end related imports
import Repository.UserRepository;
import User.User;
import Scheduler.Scheduler;
import UserService.UserService;	
import event.Event;
import event.CreatedEvent;
import Invite.Invite;
import EventManager.EventManager;
import javax.swing.JSplitPane;
import Repository.EventRepository;	


public class EventManagePanel extends JPanel {

	// UI items
	private static final long serialVersionUID = 1L;
	private JTextField txtEventName;
	private JTextField txtEventDurationminutes;
	private JButton btnInvite; // Invite
	private JButton btnSave; // Save
	private JComboBox comboBoxEarliestTime; // Earliest time
	private JComboBox comboBoxLatestTime; // Latest time
	private JTextField txtInviteeUsername;
	private JTextArea textAreaShowParticipants; // Participants
	private JLabel lblEventName;
	private JLabel lblEventDuration;
	private JLabel lblEventDescription;
	private JLabel lblLatestDate;
	private DatePicker datePickerLatest;

	// Back-end items
	private UserRepository repository;
	private EventRepository eventRepository;
	private UserService userService;
	private User currentUser;
	private boolean isNewEvent;
	private Event event;
	private Scheduler scheduler;
	private EventManager eventManager;
	private Runnable onSaveSuccess;
	private List<String> tempInvites = new ArrayList<>();
	private JLabel lblInvite;
	private JLabel lblParticipants;
	private JTextField txtEventDescription;
	private JSplitPane splitPane;
	private JButton btnUninvite;
	private JButton btnBack;


	/**
	 * Create the panel.
	 */
	public EventManagePanel(UserRepository repository, EventRepository eventRepository, User currentUser,
					  boolean isNewEvent, Event event, Scheduler scheduler) {
		this(repository, eventRepository, currentUser, isNewEvent, event, scheduler, null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public EventManagePanel(UserRepository repository, EventRepository eventRepository, User currentUser,
					  boolean isNewEvent, Event event, Scheduler scheduler, Runnable onSaveSuccess) {

		// Initialization
		this.repository = repository;
		this.eventRepository = eventRepository;
		this.userService = new UserService(repository);
		this.currentUser = currentUser;
		this.isNewEvent = isNewEvent;
		this.event = event;
		this.scheduler = scheduler;
		this.onSaveSuccess = onSaveSuccess;
		this.eventManager = new EventManager(repository, eventRepository);
		

		// Layout
		setBackground(Color.CYAN);
		setOpaque(true);
		setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

		// Labels
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0,0,0,0,0};
		gridBagLayout.rowHeights = new int[]{50, 0,0, 0,0,0, 0, 0, 0, 30,0};
		gridBagLayout.columnWeights = new double[]{0, 1.0, 0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JLabel lblEvent = new JLabel("Event");
		lblEvent.setAlignmentY(Component.TOP_ALIGNMENT);
		lblEvent.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblEvent = new GridBagConstraints();
		gbc_lblEvent.anchor = GridBagConstraints.NORTH;
		gbc_lblEvent.insets = new Insets(0, 0, 5, 0);
		gbc_lblEvent.gridwidth = 5;
		gbc_lblEvent.gridx = 0;
		gbc_lblEvent.gridy = 0;
		add(lblEvent, gbc_lblEvent);

		lblEventName = new JLabel("Event name");
		GridBagConstraints gbc_lblEventName = new GridBagConstraints();
		gbc_lblEventName.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblEventName.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventName.gridx = 1;
		gbc_lblEventName.gridy = 1;
		add(lblEventName, gbc_lblEventName);

		lblEventDuration = new JLabel("Event duration (minutes)");
		GridBagConstraints gbc_lblEventDuration = new GridBagConstraints();
		gbc_lblEventDuration.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblEventDuration.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventDuration.gridx = 3;
		gbc_lblEventDuration.gridy = 1;
		add(lblEventDuration, gbc_lblEventDuration);
		
		// Inputs
		txtEventName = new JTextField();
		txtEventName.setText("Name");
		GridBagConstraints gbc_txtEventName = new GridBagConstraints();
		gbc_txtEventName.anchor = GridBagConstraints.NORTH;
		gbc_txtEventName.insets = new Insets(0, 0, 5, 5);
		gbc_txtEventName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEventName.gridx = 1;
		gbc_txtEventName.gridy = 2;
		add(txtEventName, gbc_txtEventName);
		txtEventName.setColumns(10);

		txtEventDurationminutes = new JTextField();
		txtEventDurationminutes.setText("30");
		txtEventDurationminutes.setColumns(10);
		GridBagConstraints gbc_txtEventDurationminutes = new GridBagConstraints();
		gbc_txtEventDurationminutes.anchor = GridBagConstraints.NORTH;
		gbc_txtEventDurationminutes.insets = new Insets(0, 0, 5, 5);
		gbc_txtEventDurationminutes.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEventDurationminutes.gridx = 3;
		gbc_txtEventDurationminutes.gridy = 2;
		add(txtEventDurationminutes, gbc_txtEventDurationminutes);

		lblEventDescription = new JLabel("Event Description");
		GridBagConstraints gbc_lblEventDescription = new GridBagConstraints();
		gbc_lblEventDescription.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblEventDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventDescription.gridx = 1;
		gbc_lblEventDescription.gridy = 3;
		add(lblEventDescription, gbc_lblEventDescription);

		lblLatestDate = new JLabel("Latest date (DD Month YYYY)");
		GridBagConstraints gbc_lblLatestDate = new GridBagConstraints();
		gbc_lblLatestDate.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblLatestDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblLatestDate.gridx = 3;
		gbc_lblLatestDate.gridy = 3;
		add(lblLatestDate, gbc_lblLatestDate);

		txtEventDescription = new JTextField();
		GridBagConstraints gbc_txtEventDescription = new GridBagConstraints();
		gbc_txtEventDescription.anchor = GridBagConstraints.NORTH;
		gbc_txtEventDescription.insets = new Insets(0, 0, 5, 5);
		gbc_txtEventDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEventDescription.gridx = 1;
		gbc_txtEventDescription.gridy = 4;
		add(txtEventDescription, gbc_txtEventDescription);
		txtEventDescription.setColumns(10);

		datePickerLatest = new DatePicker();
		GridBagConstraints gbc_datePickerLatest = new GridBagConstraints();
		gbc_datePickerLatest.anchor = GridBagConstraints.NORTH;
		gbc_datePickerLatest.insets = new Insets(0, 0, 5, 5);
		gbc_datePickerLatest.fill = GridBagConstraints.HORIZONTAL;
		gbc_datePickerLatest.gridx = 3;
		gbc_datePickerLatest.gridy = 4;
		add(datePickerLatest, gbc_datePickerLatest);

		comboBoxEarliestTime = new JComboBox();
		comboBoxEarliestTime.setModel(new DefaultComboBoxModel(new String[] {"Select earliest time", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		GridBagConstraints gbc_comboBoxEarliestTime = new GridBagConstraints();
		gbc_comboBoxEarliestTime.anchor = GridBagConstraints.NORTH;
		gbc_comboBoxEarliestTime.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxEarliestTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxEarliestTime.gridx = 1;
		gbc_comboBoxEarliestTime.gridy = 5;
		add(comboBoxEarliestTime, gbc_comboBoxEarliestTime);

		comboBoxLatestTime = new JComboBox();
		comboBoxLatestTime.setModel(new DefaultComboBoxModel(new String[] {"Select latest time", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		GridBagConstraints gbc_comboBoxLatestTime = new GridBagConstraints();
		gbc_comboBoxLatestTime.anchor = GridBagConstraints.NORTH;
		gbc_comboBoxLatestTime.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxLatestTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxLatestTime.gridx = 3;
		gbc_comboBoxLatestTime.gridy = 5;
		add(comboBoxLatestTime, gbc_comboBoxLatestTime);

		btnSave = new JButton("Save");
		lblInvite = new JLabel("Invite");
		GridBagConstraints gbc_lblInvite = new GridBagConstraints();
		gbc_lblInvite.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblInvite.insets = new Insets(0, 0, 5, 5);
		gbc_lblInvite.gridx = 1;
		gbc_lblInvite.gridy = 6;
		add(lblInvite, gbc_lblInvite);

		lblParticipants = new JLabel("Participants");
		GridBagConstraints gbc_lblParticipants = new GridBagConstraints();
		gbc_lblParticipants.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblParticipants.insets = new Insets(0, 0, 5, 5);
		gbc_lblParticipants.gridx = 3;
		gbc_lblParticipants.gridy = 6;
		add(lblParticipants, gbc_lblParticipants);

		txtInviteeUsername = new JTextField();
		txtInviteeUsername.setText("Invitee username");
		GridBagConstraints gbc_txtInviteeUsername = new GridBagConstraints();
		gbc_txtInviteeUsername.anchor = GridBagConstraints.SOUTH;
		gbc_txtInviteeUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtInviteeUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtInviteeUsername.gridx = 1;
		gbc_txtInviteeUsername.gridy = 7;
		add(txtInviteeUsername, gbc_txtInviteeUsername);
		txtInviteeUsername.setColumns(10);

		textAreaShowParticipants = new JTextArea();
		textAreaShowParticipants.setEditable(false);
		textAreaShowParticipants.setBackground(Color.WHITE);
		GridBagConstraints gbc_textAreaShowParticipants = new GridBagConstraints();
		gbc_textAreaShowParticipants.gridheight = 2;
		gbc_textAreaShowParticipants.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaShowParticipants.fill = GridBagConstraints.BOTH;
		gbc_textAreaShowParticipants.gridx = 3;
		gbc_textAreaShowParticipants.gridy = 7;
		add(textAreaShowParticipants, gbc_textAreaShowParticipants);
				
				splitPane = new JSplitPane();
				splitPane.setResizeWeight(0.5);
				GridBagConstraints gbc_splitPane = new GridBagConstraints();
				gbc_splitPane.anchor = GridBagConstraints.NORTH;
				gbc_splitPane.insets = new Insets(0, 0, 5, 5);
				gbc_splitPane.fill = GridBagConstraints.HORIZONTAL;
				gbc_splitPane.gridx = 1;
				gbc_splitPane.gridy = 8;
				add(splitPane, gbc_splitPane);
		
				btnInvite = new JButton("Invite");
				splitPane.setLeftComponent(btnInvite);
				btnUninvite = new JButton("Uninvite");
				
				splitPane.setRightComponent(btnUninvite);
		
		btnBack = new JButton("Back");
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnBack.insets = new Insets(0, 0, 0, 5);
		gbc_btnBack.gridx = 1;
		gbc_btnBack.gridy = 9;
		add(btnBack, gbc_btnBack);
				
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.gridwidth = 2;
		gbc_btnSave.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnSave.gridx = 3;
		gbc_btnSave.gridy = 9;
		add(btnSave, gbc_btnSave);

		// Load existing event
		if (!isNewEvent && event != null) {
			txtEventName.setText(event.getEventName());
			txtEventDurationminutes.setText(String.valueOf(event.getEventDuration()));
			txtEventDescription.setText(event.getEventDescription());
			updateParticipantList();
		}

		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inviteeUsername = txtInviteeUsername.getText().trim();

				// Error message for empty invitee input
				if (inviteeUsername.isEmpty() || inviteeUsername.equalsIgnoreCase("Invitee username")) {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
							"Please enter a username.");
					return;
				}
				
				if (inviteeUsername.equals(currentUser.getUsername())) {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
							"Event organizer already invited to this event.");
					return;
				}

				// Checks if invitee exists in repository and is not already in event
				User invitee = repository.findUsername(inviteeUsername);
				if (invitee != null) {
					// if statement adds the participant to the list
					if (!tempInvites.contains(inviteeUsername) || !event.getParticipants().contains(inviteeUsername)) {
						tempInvites.add(inviteeUsername);
						updateParticipantList();
						txtInviteeUsername.setText("");
						// Clear input so user can input new username
					} else {
						javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
								"User is already invited to this event!");
					}
				} else {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
							"User not found.", "Error" , JOptionPane.ERROR_MESSAGE);
				}}
		});
		
		btnUninvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inviteeUsername = txtInviteeUsername.getText().trim();

				// Error message for empty invitee input
				if (inviteeUsername.isEmpty() || inviteeUsername.equalsIgnoreCase("Invitee username")) {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
							"Please enter a username.");
					return;
				}
				if (inviteeUsername.equals(currentUser.getUsername())) {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
							"You cannot uninvite the organizer.");
					return;
				}
				

				User invitee = repository.findUsername(inviteeUsername);
				if (invitee == null) {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this, 
							"User not found.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (tempInvites.contains(inviteeUsername)) {
					tempInvites.remove(inviteeUsername);
				} else if (!isNewEvent && event != null) {
					eventManager.removeInvite(event,invitee);			
				} else {
					javax.swing.JOptionPane.showMessageDialog(EventManagePanel.this,
							"User is not in this event!");
					return;
				}
				
				updateParticipantList();
				txtInviteeUsername.setText("");
			}
		});
		
		btnBack.addActionListener(e -> {
			if (onSaveSuccess != null) {
				onSaveSuccess.run();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String eventName = txtEventName.getText();
				if (eventName.isEmpty() || eventName.equalsIgnoreCase("Name")) {
					JOptionPane.showMessageDialog(EventManagePanel.this,
							"Please input an Event Name.");

					return;
				}

				String eventDescription = txtEventDescription.getText();
				if (eventDescription == null || eventDescription.trim().isEmpty()) {
					eventDescription = " ";
				}

				// Duration must be integer
				int duration = 30;
				try {
					duration = Integer.parseInt(txtEventDurationminutes.getText());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(EventManagePanel.this, "Please input integer number for Event Duration in minutes.");
					return;
				}

				// Date inputs should be in YYYY-MM-DD
				java.time.LocalDate ldLatest = datePickerLatest.getDate();
				
				if (ldLatest == null) {
					JOptionPane.showMessageDialog(EventManagePanel.this, "Please select the latest date.");
					return;
				}
				
				if (!ldLatest.isAfter(java.time.LocalDate.now())) {
				    JOptionPane.showMessageDialog(EventManagePanel.this, "The latest date must be after today.");
				    return;
				}

				java.time.LocalDate today = java.time.LocalDate.now();
				long daysAhead = java.time.temporal.ChronoUnit.DAYS.between(today,ldLatest);
				int maxDaysAhead = (int) daysAhead;

				Object selectedEarliestHour = comboBoxEarliestTime.getSelectedItem();
				Object selectedLatestHour = comboBoxLatestTime.getSelectedItem();
				if (selectedEarliestHour == null || selectedEarliestHour.equals("Select earliest time")) {
					JOptionPane.showMessageDialog(EventManagePanel.this, "Please select the earliest time boundary.");
					return;
				}
				if (selectedLatestHour == null || selectedLatestHour.equals("Select latest time")) {
					JOptionPane.showMessageDialog(EventManagePanel.this, "Please select the latest time boundary.");
					return;
				}
				if (Integer.parseInt(selectedEarliestHour.toString()) >= Integer.parseInt(selectedLatestHour.toString())) {
					JOptionPane.showMessageDialog(EventManagePanel.this, "Latest time must be later than earliest time.");
					return;
				}

				String earliestTime = selectedEarliestHour.toString();
				String latestTime = selectedLatestHour.toString();
				int earliestHour = Integer.parseInt(earliestTime);
				int latestHour = Integer.parseInt(latestTime);

				if (isNewEvent) {
					EventManagePanel.this.event = new CreatedEvent(eventName,duration,eventDescription,currentUser.getUsername(), new ArrayList<>());
					EventManagePanel.this.isNewEvent = false;

				} else {

					EventManagePanel.this.event.setEventName(eventName);
					EventManagePanel.this.event.setEventDuration(duration);
					EventManagePanel.this.event.setEventDescription(eventDescription);
				}

				for (String username: tempInvites) {
					User invitee = repository.findUsername(username);
					if (invitee != null) {
						EventManagePanel.this.eventManager.addInvite(EventManagePanel.this.event, invitee);
					}
				}
				tempInvites.clear();


				Scheduler scheduler = new Scheduler(earliestHour, latestHour, maxDaysAhead, repository,eventRepository);
				scheduler.scheduleEvent(EventManagePanel.this.event);

				JOptionPane.showMessageDialog(EventManagePanel.this, "Event Saved.");
				updateParticipantList();
				if (onSaveSuccess != null) {
					onSaveSuccess.run();
				}

			}
		});
	}

	public void updateParticipantList() {
	    textAreaShowParticipants.setText("");
	    appendExistingInvites();
	    appendTemporaryInvites();
	}
	private void appendExistingInvites() {
	    if (event == null || event.getInvites() == null) {
	        return;
	    }

	    for (Invite invite : event.getInvites()) {
	        textAreaShowParticipants.append(invite.getRecipient() + "\n");
	    }
	}

	private void appendTemporaryInvites() {
	    if (tempInvites == null) {
	        return;
	    }

	    for (String username : tempInvites) {
	        textAreaShowParticipants.append(username + " (New)\n");
	    }
	}
}
