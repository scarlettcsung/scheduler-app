package GUI;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;

import Event.Event;
import EventManager.EventManager;
import Invite.Invite;
import Scheduler.Scheduler;
import Repository.UserRepository;
import User.User;

public class MainDashboardPanel2 extends JPanel {
    
    public static final long serialVersionUID = 1L;
    private UserRepository repository;
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
    
    public MainDashboardPanel2(UserRepository repository, User user, Scheduler scheduler) {
        this.repository = repository;
        this.currentUser = user;
        this.scheduler = scheduler;
        this.invitesPane = new JPanel();
        
        cardBorder = cardBorder();

        event1 = new Event(
    	        "Team Meeting", 
    	        60, 
    	        "yappen met de bros", 
    	        currentUser.getUsername(), 
    	        false, 
    	        new ArrayList<>()
    	    );
        event2 = new Event(
        		"Deadline", 
        		120, 
        		"strijden voor de deadline", 
        		currentUser.getUsername(), 
        		false, 
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
        int H = 1150;
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
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainDashboardPanel2.this);
                topFrame.setContentPane(new AuthenticationPanel(repository,scheduler));
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
        calendarCard.setBounds(W/2+MARGIN/2, MARGIN, W/2-MARGIN*6/2, H/2-MARGIN*6/2);
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
        eventPane.setBounds(MARGIN, 2*MARGIN+40, W/2-3/2*MARGIN, H/2-3*MARGIN);
        add(eventPane);

        
        
        // Run setup for Events and Invites
        setupEvents(eventPane, W, H, MARGIN, CARD_BORDER);
        invitesPane.setLayout(null);
        invitesPane.setBackground(Color.WHITE);
        invitesPane.setBorder(CARD_BORDER);
        invitesPane.setBounds(MARGIN, H/2 + 2*MARGIN, W/2 - 3*MARGIN/2, H/2 - 7*MARGIN);
        add(invitesPane);

        setupInvites();
    }
    private List<Event> collectUniqueEvents() {
		Set<Event> allEvents = new LinkedHashSet<>();
		for (User user : repository.getAll()) {
			if (user.getCalendar() == null || user.getCalendar().getEvents() == null) {
				continue;
			}
			allEvents.addAll(user.getCalendar().getEvents());
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
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainDashboardPanel2.this);
				JDialog dialog = new JDialog(topFrame, "Create Event", true);
				dialog.setContentPane(new EventPanel(repository, currentUser, true, null, scheduler, () -> {
				    dialog.dispose();
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

		List<Event> events = collectUniqueEvents();
		eventsCardsPanel.setPreferredSize(new Dimension(500, Math.max(1, events.size()) * 155));

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
		eventsScrollPane.setBounds(10, 45, 565, 530);
		eventsScrollPane.getVerticalScrollBar().setUnitIncrement(12);
		eventsScrollPane.setBorder(null);
		eventPane.add(eventsScrollPane);
	}

    private JPanel createEventCard(Event event, JPanel eventsCardsPanel) {
		JPanel card = new JPanel();
		card.setLayout(null);
		card.setBorder(cardBorder);
		card.setBackground(Color.WHITE);
		card.setPreferredSize(new Dimension(500, 140));
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
		card.setMinimumSize(new Dimension(200, 140));

		int MARGIN = 10;

		JLabel nameLabel = new JLabel(event.getEventName());
		nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
		nameLabel.setBounds(MARGIN, MARGIN, 400, 20);
		card.add(nameLabel);

		JLabel durationLabel = new JLabel("Duration: " + event.getEventDuration() + " min");
		durationLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		durationLabel.setForeground(Color.BLACK);
		durationLabel.setBounds(MARGIN, 53, 400, 16);
		card.add(durationLabel);

		JLabel descLabel = new JLabel(event.getEventDescription());
		descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
		descLabel.setForeground(Color.BLACK);
		descLabel.setBounds(MARGIN, 72, 400, 16);
		card.add(descLabel);

		JButton deleteButton = new JButton("Delete Event");
		deleteButton.setBounds(MARGIN + 130, 95, 120, 24);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventManager eventManager = new EventManager(repository);
				eventManager.deleteEvent(event);
				refreshEvents();
			}
		});

		JButton updateButton = new JButton("Update Event");
		updateButton.setBounds(MARGIN, 95, 120, 24);
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(card);
				topFrame.setContentPane(new EventPanel(repository, currentUser, false, event, scheduler, () -> {
					topFrame.setContentPane(new MainDashboardPanel2(repository, currentUser, scheduler));
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
		int inviteCardHeight = 60;
		int inviteCardWidth = 560;
		int inviteCardSpacing = 68;
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
			nameLabel.setBounds(10, 10, inviteCardWidth, 16);
			inviteCard.add(nameLabel);

			JButton acceptButton = new JButton("Accept");
			acceptButton.setFont(new Font("Arial", Font.PLAIN, 10));
			acceptButton.setBackground(Color.GREEN);
			acceptButton.setForeground(Color.WHITE);
			acceptButton.setBounds(10, 30, 70, 20);
			acceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					invite.accept();
				}
			});
			inviteCard.add(acceptButton);

			JButton declineButton = new JButton("Decline");
			declineButton.setFont(new Font("Arial", Font.PLAIN, 10));
			declineButton.setBackground(Color.RED);
			declineButton.setForeground(Color.WHITE);
			declineButton.setBounds(90, 30, 70, 20);
			declineButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EventManager eventManager = new EventManager(repository);
					eventManager.rejectInvite(invite, event, repository);
					refreshEvents();
				}
			});
			inviteCard.add(declineButton);

			invitesCardsPanel.add(inviteCard);
        }

		JScrollPane invitesScrollPane = new JScrollPane(invitesCardsPanel);
		invitesScrollPane.setBounds(10, 45, 565, 530);
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
		topFrame.setContentPane(new MainDashboardPanel2(repository, currentUser, scheduler));
		topFrame.revalidate();
		topFrame.repaint();
	}
}
