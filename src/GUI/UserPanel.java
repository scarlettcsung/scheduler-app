package GUI;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.ObjectInputFilter.Status;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;

import EventManager.EventManager;
import Invite.Invite;
import Scheduler.Scheduler;
import Repository.EventRepository;
import Repository.UserRepository;
import User.User;
import event.Event;
import event.CreatedEvent;
import Invite.inviteStatus;

public class UserPanel extends JPanel {
    
    private UserRepository repository;
    private EventRepository eventRepository;
    private User currentUser;
    private Event event1;
    private Event event2;
    private Scheduler scheduler;
    private final JPanel invitesPane;
    private final javax.swing.border.Border cardBorder;
    
    private javax.swing.border.Border cardBorder() {
        return new CompoundBorder(
                new LineBorder(Color.gray, 1, true),
                new EmptyBorder(8, 8, 8, 8)
                );
    }
    
    public UserPanel(UserRepository repository, User user, Scheduler scheduler,EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.currentUser = user;
        this.scheduler = scheduler;
        this.invitesPane = new JPanel();
        
        cardBorder = cardBorder();

        event1 = new CreatedEvent(
    	        "Team Meeting", 
    	        60, 
    	        "yappen met de bros", 
    	        currentUser.getUsername(), 
    	        new ArrayList<>()
    	    );
        event2 = new CreatedEvent(
        		"Deadline", 
        		120, 
        		"strijden voor de deadline", 
        		currentUser.getUsername(), 
        		new ArrayList<>()
        );  
        LocalDateTime event1Time = LocalDateTime.of(2026, 4,20,16,20);
        LocalDateTime event2Time = LocalDateTime.of(2026, 5,11,9,30);
        event1.setEventTime(event1Time);
        event2.setEventTime(event2Time);
        
        // UI Setup
        setBackground(Color.CYAN);
        setLayout(null); // Allows manual bounds setting
        
        // Fixed dimensions based on mainframe design
        int W = 1180;
        int H = 760;
        int MARGIN = 10;
        setPreferredSize(new Dimension(W, H));
        setSize(W, H);
        
        javax.swing.border.Border CARD_BORDER = cardBorder();
        
        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Dialog", Font.PLAIN, 11)); // Changed to Dialog for compatibility
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Find the parent JFrame and swap back to Login
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(UserPanel.this);
                topFrame.setContentPane(new AuthenticationPanel(repository,scheduler,eventRepository));
                topFrame.revalidate();
                topFrame.repaint();
            }
        });
        logoutButton.setBounds(MARGIN, MARGIN, W/5, H/30);
        add(logoutButton); // We add directly to 'this' JPanel
        
        //calendar card
        JPanel calendarCard = new JPanel();
        calendarCard.setLayout(null);
        calendarCard.setBackground(Color.yellow);
        calendarCard.setBorder(CARD_BORDER);
        calendarCard.setBounds(W/2+MARGIN/2, MARGIN, W/2-MARGIN*6/2, 340);
        add(calendarCard);
        
        JLabel calendarTitle = new JLabel("Calendar (Welcome " + currentUser.getUsername() + ")");
        calendarTitle.setFont(new Font("Dialog", Font.BOLD, 14));
        calendarTitle.setBounds(MARGIN, MARGIN, 300, 20);
        calendarCard.add(calendarTitle);
        
        CalendarPanel calendarPanel = new CalendarPanel();
        calendarPanel.setBounds(MARGIN, 2*MARGIN+20, 376, 290);
        calendarCard.add(calendarPanel);
        
        // event pane
        JPanel eventPane = new JPanel();
        eventPane.setLayout(null);
        eventPane.setBackground(Color.WHITE);
        eventPane.setBorder(CARD_BORDER);
        eventPane.setBounds(MARGIN, 70, W/2-3/2*MARGIN, 300);
        add(eventPane);

        
        
        // Run setup for Events and Invites
        setupEvents(eventPane, W, H, MARGIN, CARD_BORDER);
        invitesPane.setLayout(null);
        invitesPane.setBackground(Color.WHITE);
        invitesPane.setBorder(CARD_BORDER);
        invitesPane.setBounds(MARGIN, 380, W/2 - 3*MARGIN/2, 300);
        add(invitesPane);
        
        JButton btnDeleteAccount = new JButton("Delete Account");
        btnDeleteAccount.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		repository.deleteUserData(currentUser.getUsername(), user);
        		eventRepository.deleteEventsByOrganizer(currentUser.getUsername());
        		JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(UserPanel.this);
        		topFrame.setContentPane(new AuthenticationPanel(repository, scheduler,eventRepository));
        		topFrame.revalidate();
        		topFrame.repaint();
        	}
        });
        btnDeleteAccount.setBounds(821, 403, 156, 29);
        add(btnDeleteAccount);

        setupInvites();
    }

    private List<Event> collectAllEventsFromRepository() {
		return new ArrayList<>(eventRepository.getAll());
	}

    private List<Event> collectVisibleEvents() {
		Set<Event> allEvents = new LinkedHashSet<>();
		for (Event event : eventRepository.getAll()) {
			boolean accepted = false;

			if (event.getInvites() != null) {
			    for (Invite invite : event.getInvites()) {
			        if (currentUser.getUsername().equals(invite.getRecipient())
			                && invite.getStatus() == inviteStatus.ACCEPTED) {
			            accepted = true;
			            break;
			        }
			    }
			}

			if (currentUser.getUsername().equals(event.getOrganizer()) || accepted) {
			    allEvents.add(event);
			}
		}
		return new ArrayList<>(allEvents);
	}

    private List<Invite> collectUniqueInvites(List<Event> events) {
    	Set<String> seen = new LinkedHashSet<>();
    	List<Invite> invites = new ArrayList<>();

    	for (Event event : events) {
    		if (event.getInvites() == null) {
    			continue;
    		}

    		for (Invite invite : event.getInvites()) {
    			if (invite.getRecipient() == null) {
    				continue;
    			}

    			if (!invite.getRecipient().equals(currentUser.getUsername())) {
    				continue;
    			}

    			String key = invite.getRecipient() + "|" + invite.getEventID();
    			if (seen.add(key)) {
    				invites.add(invite);
    			}
    		}
    	}

    	return invites;
   	}

    private void setupEvents(JPanel eventPane, int W, int H, int MARGIN, javax.swing.border.Border CARD_BORDER) {
		JLabel eventPaneTitle = new JLabel("Events");
		eventPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
		eventPaneTitle.setBounds(MARGIN, MARGIN, 160, 24);
		eventPane.add(eventPaneTitle);

		JButton createEventButton = new JButton("+ Create Event");
		createEventButton.setBounds(170, MARGIN, 120, 24);


	        createEventButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(UserPanel.this);
					JDialog dialog = new JDialog(topFrame, "Create Event", true);
					dialog.setContentPane(new EventManagePanel(repository, eventRepository, currentUser, true, null, scheduler, () -> {
					    dialog.dispose();
					    refreshEvents();
					}));
					dialog.setSize(500, 550);
					dialog.setLocationRelativeTo(topFrame);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);

			}
		});

        eventPane.add(createEventButton);

		JPanel eventsCardsPanel = new JPanel();
		eventsCardsPanel.setLayout(new javax.swing.BoxLayout(eventsCardsPanel, javax.swing.BoxLayout.Y_AXIS));
		eventsCardsPanel.setBackground(Color.WHITE);
		eventsCardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		List<Event> events = collectVisibleEvents();
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
		
		LocalDateTime eventTime = event.getEventTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
		String stringDateTime = eventTime.format(formatter);
		int duration = event.getEventDuration();
		LocalDateTime endTime = eventTime.plusMinutes(duration);
		String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		String stringDisplay = String.format("%s - %s", stringDateTime, endTimeStr);

		JLabel metaLabel = new JLabel("Time: " + stringDisplay + " |  Organizer: " + event.getOrganizer());
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
				eventRepository.deleteEvent(event.getEventID());
				refreshEvents();
			}
		});

		JButton updateButton = new JButton("Update Event");
		updateButton.setBounds(MARGIN, 72, 120, 22);
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(card);
					topFrame.setContentPane(new EventManagePanel(repository, eventRepository, currentUser, false, event, scheduler, () -> {
						topFrame.setContentPane(new UserPanel(repository, currentUser, scheduler,eventRepository));
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

		List<Event> allEvents = collectAllEventsFromRepository();
		List<Invite> invites = collectUniqueInvites(allEvents);
		int inviteCardHeight = 50;
		int inviteCardWidth = 560;
		int inviteCardSpacing = 58;
		invitesCardsPanel.setPreferredSize(new Dimension(290, Math.max(1, invites.size()) * inviteCardSpacing));

		for (int i = 0; i < invites.size(); i++) {
			Invite invite = invites.get(i);
			Event event = findEventById(allEvents, invite.getEventID());
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

			JLabel detailLabel = new JLabel("From: " + event.getOrganizer() + " | Status: " + invite.getStatus());
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
					eventManager.rejectInvite(invite, event);
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
		topFrame.setContentPane(new UserPanel(repository, currentUser, scheduler,eventRepository));
		topFrame.revalidate();
		topFrame.repaint();
	}
}
