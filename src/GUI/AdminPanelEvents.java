package GUI;

import com.github.lgooddatepicker.components.CalendarPanel;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.io.File;
import IcsImporter.IcsImporter;
import IcsImporter.ImportStatus;

import EventManager.EventManager;
import Invite.Invite;
import Scheduler.Scheduler;
import User.User;
import event.Event;
import Repository.EventRepository;
import Repository.UserRepository;

public class AdminPanelEvents extends JPanel {

	private static final long serialVersionUID = 1L;

	private final UserRepository repository;
	private final EventRepository eventRepository;
	private final User adminUser;
	private final Scheduler scheduler;
	private final JPanel eventPane;
	private final JPanel invitesPane;
	private final javax.swing.border.Border cardBorder;

	public AdminPanelEvents(UserRepository repository, User adminUser, Scheduler scheduler,EventRepository eventRepository) {
		this.repository = repository;
		this.eventRepository = eventRepository;
		this.adminUser = adminUser;
		this.scheduler = scheduler;

		setBackground(Color.CYAN);
		setLayout(null);

		int W = 1180;
		int H = 760;
		int MARGIN = 10;

		cardBorder = cardBorder();

		JButton backButton = new JButton("Admin Panel");
		backButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(AdminPanelEvents.this);
				topFrame.setContentPane(new AdminPanel(repository, adminUser, scheduler,eventRepository));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		backButton.setBounds(MARGIN, MARGIN, W / 5, H / 30);
		add(backButton);

		JButton logoutButton = new JButton("Logout");
		logoutButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(AdminPanelEvents.this);
				topFrame.setContentPane(new AuthenticationPanel(repository, scheduler,eventRepository));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		logoutButton.setBounds(W - (W / 5) - MARGIN, MARGIN, W / 5, H / 30);
		add(logoutButton);

		JPanel calendarCard = new JPanel();
		calendarCard.setLayout(null);
		calendarCard.setBackground(Color.YELLOW);
		calendarCard.setBorder(cardBorder);
		calendarCard.setBounds(W / 2 + MARGIN / 2, MARGIN, W / 2 - MARGIN * 6 / 2, 340);
		add(calendarCard);

		JLabel calendarTitle = new JLabel("Calendar (Welcome " + adminUser.getUsername() + ")");
		calendarTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		calendarTitle.setBounds(MARGIN, MARGIN, 300, 20);
		calendarCard.add(calendarTitle);

		CalendarPanel calendarPanel = new CalendarPanel();
		calendarPanel.setBounds(MARGIN, 2 * MARGIN + 20, 376, 290);
		calendarCard.add(calendarPanel);

		eventPane = new JPanel();
		eventPane.setLayout(null);
		eventPane.setBackground(Color.WHITE);
		eventPane.setBorder(cardBorder);
		eventPane.setBounds(MARGIN, 70, W / 2 - 3 * MARGIN / 2, 300);
		add(eventPane);

		invitesPane = new JPanel();
		invitesPane.setLayout(null);
		invitesPane.setBackground(Color.WHITE);
		invitesPane.setBorder(cardBorder);
		invitesPane.setBounds(MARGIN, 380, W / 2 - 3 * MARGIN / 2, 300);
		add(invitesPane);

		setupEvents();
		setupInvites();
		
	    JButton btnImportCalendar = new JButton("Import Calendar");
	    btnImportCalendar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileFilter(new FileNameExtensionFilter("ICS Files", "ics"));

	            int result = fileChooser.showOpenDialog(AdminPanelEvents.this);

	            if (result == JFileChooser.APPROVE_OPTION) {
	                File selectedFile = fileChooser.getSelectedFile();
	                String filePath = selectedFile.getAbsolutePath();

	                IcsImporter importer = new IcsImporter();
	                ImportStatus status = importer.importCalendar(adminUser, filePath);

	                List<Event> existingEvents = new ArrayList<>(eventRepository.getAll());

	                for (Event event : existingEvents) {
	                    if (event.isImported() && adminUser.getUsername().equals(event.getOrganizer())) {
	                        eventRepository.deleteEvent(event.getEventID());
	                    }
	                }
	                if (status == ImportStatus.Succes) {
	                    for (Event event : adminUser.getCalendar().getEvents()) {
	                        if (eventRepository.findByEventID(event.getEventID()) == null) {
	                            eventRepository.save(event);
	                        }
	                    }

	                    JOptionPane.showMessageDialog(AdminPanelEvents.this, "Calendar imported successfully!");
	                    refreshEvents();
	                } else {
	                    JOptionPane.showMessageDialog(AdminPanelEvents.this, "Import failed: " + status);
	                }
	            }
	        }
	    });
	    btnImportCalendar.setBounds(831, 444, 146, 29);
	    add(btnImportCalendar);
	}

	private javax.swing.border.Border cardBorder() {
		return new CompoundBorder(
				new LineBorder(Color.GRAY, 1, true),
				new EmptyBorder(8, 8, 8, 8)
		);
	}

	private List<Event> collectUniqueEvents() {
		return new ArrayList<>(eventRepository.getAll());
	}

	private List<Invite> collectUniqueInvites(List<Event> events) {
		Set<String> seen = new LinkedHashSet<>();
		List<Invite> invites = new ArrayList<>();
		for (Event event : events) {
			if (event.getInvites() == null) {
				continue;
			}
			for (Invite invite : event.getInvites()) {
				String key = invite.getRecipient() + "|" + invite.getEventID();
				if (seen.add(key)) {
					invites.add(invite);
				}
			}
		}
		return invites;
	}

	private void setupEvents() {
		JLabel eventPaneTitle = new JLabel("Events");
		eventPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
		eventPaneTitle.setBounds(10, 10, 160, 24);
		eventPane.add(eventPaneTitle);

		JButton createEventButton = new JButton("+ Create Event");
		createEventButton.setBounds(170, 10, 120, 24);
		createEventButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(eventPane);
				topFrame.setContentPane(new EventManagePanel(repository, eventRepository, adminUser, true, null, scheduler, () -> {
					topFrame.setContentPane(new AdminPanelEvents(repository, adminUser, scheduler, eventRepository));
					topFrame.revalidate();
					topFrame.repaint();
				}));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		eventPane.add(createEventButton);

		JPanel eventsCardsPanel = new JPanel();
		eventsCardsPanel.setLayout(new javax.swing.BoxLayout(eventsCardsPanel, javax.swing.BoxLayout.Y_AXIS));
		eventsCardsPanel.setBackground(Color.WHITE);
		eventsCardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		List<Event> events = collectUniqueEvents();
		eventsCardsPanel.setPreferredSize(new Dimension(500, Math.max(1, events.size()) * 118));

		for (Event event : events) {
			JPanel card = createEventCard(event, eventsCardsPanel);
			card.setAlignmentX(Component.LEFT_ALIGNMENT);
			eventsCardsPanel.add(card);
			eventsCardsPanel.add(Box.createVerticalStrut(10));
		}

		JScrollPane eventsScrollPane = new JScrollPane(
				eventsCardsPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		eventsScrollPane.setBounds(10, 45, 565, 235);
		eventsScrollPane.getVerticalScrollBar().setUnitIncrement(12);
		eventsScrollPane.setBorder(null);
		eventPane.add(eventsScrollPane);
	}
	

	private JPanel createEventCard(Event event, JPanel eventsCardsPanel) {
		JPanel card = new JPanel();
		card.setLayout(null);
		card.setBorder(cardBorder);
		card.setBackground(Color.WHITE);
		card.setPreferredSize(new Dimension(500, 108));
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
		card.setMinimumSize(new Dimension(200, 108));

		int MARGIN = 10;

		JButton nameButton = new JButton(event.getEventName());
		nameButton.setFont(new Font("Arial", Font.BOLD, 13));
		nameButton.setBounds(MARGIN, MARGIN, 400, 20);
		nameButton.setHorizontalAlignment(SwingConstants.LEFT);
		
		nameButton.setMargin(new Insets(0,0,0,0));
		nameButton.setBorderPainted(false);
		nameButton.setContentAreaFilled(false);
		nameButton.setFocusPainted(false);
		nameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		nameButton.addActionListener(e -> {
			ViewEventDialog dialog = new ViewEventDialog(event);
			dialog.setModal(true);
			dialog.setLocationRelativeTo(card);
			dialog.setVisible(true);
		});
		
		card.add(nameButton);


		JLabel metaLabel = new JLabel("Time: " + event.getTimeString() + " |  Organizer: " + event.getOrganizer());
		metaLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		metaLabel.setForeground(Color.BLACK);
		metaLabel.setBounds(MARGIN, 36, 440, 16);
		card.add(metaLabel);

		JLabel descLabel = new JLabel(event.getEventDescription());
		descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
		descLabel.setForeground(Color.BLACK);
		descLabel.setBounds(MARGIN, 55, 440, 16);
		card.add(descLabel);

		JButton deleteButton = new JButton("Delete Event");
		deleteButton.setBounds(MARGIN + 130, 72, 120, 22);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventManager eventManager = new EventManager(repository, eventRepository);
				eventManager.deleteEvent(event);
				refreshEvents();
			}
		});

		JButton updateButton = new JButton("Update Event");
		updateButton.setBounds(MARGIN, 72, 120, 22);
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(card);
					topFrame.setContentPane(new EventManagePanel(repository, eventRepository, adminUser, false, event, scheduler, () -> {
						topFrame.setContentPane(new AdminPanelEvents(repository, adminUser, scheduler, eventRepository));
						topFrame.revalidate();
						topFrame.repaint();
					}));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});

		card.add(deleteButton);
		card.add(updateButton);
		return card;
	}

	private void setupInvites() {
		JLabel invitesPaneTitle = new JLabel("Invites");
		invitesPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
		invitesPaneTitle.setBounds(10, 10, 200, 24);
		invitesPane.add(invitesPaneTitle);

		JPanel invitesCardsPanel = new JPanel();
		invitesCardsPanel.setLayout(null);
		invitesCardsPanel.setBackground(Color.WHITE);

		List<Event> events = collectUniqueEvents();
		List<Invite> invites = collectUniqueInvites(events);
		int inviteCardHeight = 50;
		int inviteCardWidth = 560;
		int inviteCardSpacing = 58;
		invitesCardsPanel.setPreferredSize(new Dimension(290, Math.max(1, invites.size()) * inviteCardSpacing));

		for (int i = 0; i < invites.size(); i++) {
			Invite invite = invites.get(i);
			Event event = findEventById(events, invite.getEventID());
			if (event == null) {
				continue;
			}

			JPanel inviteCard = new JPanel();
			inviteCard.setLayout(null);
			inviteCard.setBackground(new Color(250, 250, 250));
			inviteCard.setBorder(cardBorder);
			inviteCard.setBounds(0, i * inviteCardSpacing, inviteCardWidth, inviteCardHeight);

			JLabel nameLabel = new JLabel(event.getEventName());
			nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
			nameLabel.setBounds(10, 8, inviteCardWidth, 16);
			inviteCard.add(nameLabel);

			JLabel detailLabel = new JLabel("From: " + event.getOrganizer() + " | To: " + invite.getRecipient() + " | Status: " + invite.getStatus());
			detailLabel.setFont(new Font("Arial", Font.PLAIN, 10));
			detailLabel.setForeground(Color.DARK_GRAY);
			detailLabel.setBounds(10, 23, inviteCardWidth, 14);
			inviteCard.add(detailLabel);

			JButton acceptButton = new JButton("Accept");
			acceptButton.setFont(new Font("Arial", Font.PLAIN, 10));
			acceptButton.setBackground(Color.GREEN);
			acceptButton.setForeground(Color.WHITE);
			acceptButton.setBounds(10, 34, 70, 16);
			acceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					invite.accept();
					refreshEvents();
				}
			});
			inviteCard.add(acceptButton);

			JButton declineButton = new JButton("Decline");
			declineButton.setFont(new Font("Arial", Font.PLAIN, 10));
			declineButton.setBackground(Color.RED);
			declineButton.setForeground(Color.WHITE);
			declineButton.setBounds(90, 34, 70, 16);
			declineButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EventManager eventManager = new EventManager(repository, eventRepository);
					eventManager.rejectInvite(invite,event);
					refreshEvents();
				}
			});
			inviteCard.add(declineButton);

			invitesCardsPanel.add(inviteCard);
		}

		JScrollPane invitesScrollPane = new JScrollPane(invitesCardsPanel);
		invitesScrollPane.setBounds(10, 45, 565, 235);
		invitesPane.add(invitesScrollPane);
	}


	private Event findEventById(List<Event> events, String eventId) {
		for (Event event : events) {
			if (event.getEventID().equals(eventId)) {
				return event;
			}
		}
		return null;
	}

	private void refreshEvents() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		topFrame.setContentPane(new AdminPanelEvents(repository, adminUser, scheduler, eventRepository));
		topFrame.revalidate();
		topFrame.repaint();
	}
	
}
