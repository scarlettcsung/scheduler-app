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
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JFormattedTextField;

// Back-end related imports
import UserRepository.UserRepository;
import User.User;
import Event.Event;
import Scheduler.Scheduler;
import UserService.UserService;


public class EventPanel extends JPanel {

	// UI items
	private static final long serialVersionUID = 1L;
	private JTextField txtEventName;
	private JTextField txtEventDurationminutes;
	private JButton btnInvite; // Invite
	private JButton btnSave; // Save
	private JComboBox comboBoxEarliestTime; // Earliest time
	private JComboBox comboBoxLatestTime; // Latest time
	private JTextField txtParticipantUsername;
	private JTextArea textAreaShowParticipants; // Participants
	private JLabel lblParticipants; 
	private JLabel lblEventName;
	private JLabel lblEventDuration;
	private JLabel lblEarliestDate;
	private JLabel lblLatestDate;
	private JFormattedTextField frmtdtxtfldEarliestDate;
	private JFormattedTextField frmtdtxtfldLatestDate;
	
	// Back-end items
	private UserRepository repository;
	private UserService userService;
	private User currentUser;
	private boolean isNewEvent;
	private Event event;
	private Scheduler scheduler;
	private List<User> tempInvites = new ArrayList<>();


	/**
	 * Create the panel.
	 */
	public EventPanel(UserRepository repository, User currentUser, 
			boolean isNewEvent, Event event, Scheduler scheduler) {
		
		// Initialization
		this.repository = repository;
		userService = new UserService(repository);
		this.currentUser = currentUser;
		this.isNewEvent = isNewEvent;
		this.event = event;
		this.scheduler = scheduler;

		
		// Layout
		setBackground(Color.CYAN);
	    setOpaque(true);
	    setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
	    
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0,0,0,0,0};
		gridBagLayout.rowHeights = new int[]{50, 0,0, 0,0,0, 0, 0, 0, 30,0};
		gridBagLayout.columnWeights = new double[]{0, 1.0, 0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_lblEventName.anchor = GridBagConstraints.WEST;
		gbc_lblEventName.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventName.gridx = 1;
		gbc_lblEventName.gridy = 1;
		add(lblEventName, gbc_lblEventName);
		
		lblEventDuration = new JLabel("Event duration (minutes)");
		GridBagConstraints gbc_lblEventDuration = new GridBagConstraints();
		gbc_lblEventDuration.anchor = GridBagConstraints.WEST;
		gbc_lblEventDuration.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventDuration.gridx = 3;
		gbc_lblEventDuration.gridy = 1;
		add(lblEventDuration, gbc_lblEventDuration);
		
		txtEventName = new JTextField();
		txtEventName.setText("Name");
		GridBagConstraints gbc_txtEventName = new GridBagConstraints();
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
		gbc_txtEventDurationminutes.insets = new Insets(0, 0, 5, 5);
		gbc_txtEventDurationminutes.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEventDurationminutes.gridx = 3;
		gbc_txtEventDurationminutes.gridy = 2;
		add(txtEventDurationminutes, gbc_txtEventDurationminutes);
		
		lblEarliestDate = new JLabel("Earliest date");
		GridBagConstraints gbc_lblEarliestDate = new GridBagConstraints();
		gbc_lblEarliestDate.anchor = GridBagConstraints.WEST;
		gbc_lblEarliestDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEarliestDate.gridx = 1;
		gbc_lblEarliestDate.gridy = 3;
		add(lblEarliestDate, gbc_lblEarliestDate);
		
		lblLatestDate = new JLabel("Latest date ");
		GridBagConstraints gbc_lblLatestDate = new GridBagConstraints();
		gbc_lblLatestDate.anchor = GridBagConstraints.WEST;
		gbc_lblLatestDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblLatestDate.gridx = 3;
		gbc_lblLatestDate.gridy = 3;
		add(lblLatestDate, gbc_lblLatestDate);
		
		frmtdtxtfldEarliestDate = new JFormattedTextField();
		frmtdtxtfldEarliestDate.setText("2000-01-01");
		GridBagConstraints gbc_frmtdtxtfldEarliestDate = new GridBagConstraints();
		gbc_frmtdtxtfldEarliestDate.insets = new Insets(0, 0, 5, 5);
		gbc_frmtdtxtfldEarliestDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_frmtdtxtfldEarliestDate.gridx = 1;
		gbc_frmtdtxtfldEarliestDate.gridy = 4;
		add(frmtdtxtfldEarliestDate, gbc_frmtdtxtfldEarliestDate);
		
		frmtdtxtfldLatestDate = new JFormattedTextField();
		frmtdtxtfldLatestDate.setText("2000-01-01");
		GridBagConstraints gbc_frmtdtxtfldLatestDate = new GridBagConstraints();
		gbc_frmtdtxtfldLatestDate.insets = new Insets(0, 0, 5, 5);
		gbc_frmtdtxtfldLatestDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_frmtdtxtfldLatestDate.gridx = 3;
		gbc_frmtdtxtfldLatestDate.gridy = 4;
		add(frmtdtxtfldLatestDate, gbc_frmtdtxtfldLatestDate);
		
		comboBoxEarliestTime = new JComboBox();
		comboBoxEarliestTime.setModel(new DefaultComboBoxModel(new String[] {"Select earliest time", "0:00", "0:15", "0:30", "0:45", "1:00", "1:15", "1:30", "1:45", "2:00", "2:15", "2:30", "2:45", "3:00", "3:15", "3:30", "3:45", "4:00", "4:15", "4:30", "4:45", "5:00", "5:15", "5:30", "5:45", "6:00", "6:15", "6:30", "6:45", "7:00", "7:15", "7:30", "7:45", "8:00", "8:15", "8:30", "8:45", "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"}));
		GridBagConstraints gbc_comboBoxEarliestTime = new GridBagConstraints();
		gbc_comboBoxEarliestTime.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxEarliestTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxEarliestTime.gridx = 1;
		gbc_comboBoxEarliestTime.gridy = 5;
		add(comboBoxEarliestTime, gbc_comboBoxEarliestTime);
		
		comboBoxLatestTime = new JComboBox();
		comboBoxLatestTime.setModel(new DefaultComboBoxModel(new String[] {"Select latest time", "0:00", "0:15", "0:30", "0:45", "1:00", "1:15", "1:30", "1:45", "2:00", "2:15", "2:30", "2:45", "3:00", "3:15", "3:30", "3:45", "4:00", "4:15", "4:30", "4:45", "5:00", "5:15", "5:30", "5:45", "6:00", "6:15", "6:30", "6:45", "7:00", "7:15", "7:30", "7:45", "8:00", "8:15", "8:30", "8:45", "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"}));
		GridBagConstraints gbc_comboBoxLatestTime = new GridBagConstraints();
		gbc_comboBoxLatestTime.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxLatestTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxLatestTime.gridx = 3;
		gbc_comboBoxLatestTime.gridy = 5;
		add(comboBoxLatestTime, gbc_comboBoxLatestTime);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		lblParticipants = new JLabel("Participants");
		lblParticipants.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblParticipants = new GridBagConstraints();
		gbc_lblParticipants.gridwidth = 3;
		gbc_lblParticipants.insets = new Insets(0, 0, 5, 5);
		gbc_lblParticipants.gridx = 1;
		gbc_lblParticipants.gridy = 6;
		add(lblParticipants, gbc_lblParticipants);
		
		txtParticipantUsername = new JTextField();
		txtParticipantUsername.setText("Participant username");
		GridBagConstraints gbc_txtParticipantUsername = new GridBagConstraints();
		gbc_txtParticipantUsername.insets = new Insets(0, 0, 5, 5);
		gbc_txtParticipantUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtParticipantUsername.gridx = 1;
		gbc_txtParticipantUsername.gridy = 7;
		add(txtParticipantUsername, gbc_txtParticipantUsername);
		txtParticipantUsername.setColumns(10);
		
		textAreaShowParticipants = new JTextArea();
		textAreaShowParticipants.setBackground(Color.WHITE);
		GridBagConstraints gbc_textAreaShowParticipants = new GridBagConstraints();
		gbc_textAreaShowParticipants.gridheight = 2;
		gbc_textAreaShowParticipants.insets = new Insets(0, 0, 5, 5);
		gbc_textAreaShowParticipants.fill = GridBagConstraints.BOTH;
		gbc_textAreaShowParticipants.gridx = 3;
		gbc_textAreaShowParticipants.gridy = 7;
		add(textAreaShowParticipants, gbc_textAreaShowParticipants);
		
		btnInvite = new JButton("Invite");
		GridBagConstraints gbc_btnInvite = new GridBagConstraints();
		gbc_btnInvite.anchor = GridBagConstraints.EAST;
		gbc_btnInvite.insets = new Insets(0, 0, 5, 5);
		gbc_btnInvite.gridx = 1;
		gbc_btnInvite.gridy = 8;
		add(btnInvite, gbc_btnInvite);
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
			updateParticipantList();
		}
		
		// Duration must be integer
		try {
		    int duration = Integer.parseInt(txtEventDurationminutes.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		// Dates must be in YYYY-MM-DD
		try {
		    javax.swing.text.MaskFormatter dateMask = new javax.swing.text.MaskFormatter("####-##-##");
		    dateMask.setPlaceholderCharacter('_');
		    dateMask.setValidCharacters("0123456789-");
		    
		    dateMask.install(frmtdtxtfldEarliestDate);
		    dateMask.install(frmtdtxtfldLatestDate);
		    
		} catch (java.text.ParseException e) {
		    e.printStackTrace();
		}
		
	}
	
	public void updateParticipantList() {
		textAreaShowParticipants.setText("");
		if (event != null && event.getInvites() != null) {
			for (Invite.Invite invite : event.getInvites()) {
				String username = invite.getRecipient().getUsername();
				textAreaShowParticipants.append(username + "\n");
			}
		}
		
		if (tempInvites != null) {
			for (User user : tempInvites) {
				textAreaShowParticipants.append(user.getUsername() + "(New)\n");
			}
		}
	}
}
