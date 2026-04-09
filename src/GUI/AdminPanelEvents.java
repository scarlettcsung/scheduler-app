package GUI;

import com.github.lgooddatepicker.components.CalendarPanel;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Event.Event;
import EventManager.EventManager;
import Invite.Invite;
import Scheduler.Scheduler;
import User.User;
import UserRepository.UserRepository;

public class AdminPanelEvents extends JPanel {

	private static final long serialVersionUID = 1L;

	private final UserRepository repository;
	private final User adminUser;
	private final Scheduler scheduler;
	private final Event event1;
	private final Event event2;

	public AdminPanelEvents(UserRepository repository, User adminUser, Scheduler scheduler) {
		this.repository = repository;
		this.adminUser = adminUser;
		this.scheduler = scheduler;
		this.event1 = new Event(
				"Admin Team Meeting",
				60,
				"planning the admin tasks",
				adminUser.getUsername(),
				false,
				new ArrayList<>()
		);
		this.event2 = new Event(
				"Review Deadline",
				120,
				"checking the pending calendar actions",
				adminUser.getUsername(),
				false,
				new ArrayList<>()
		);

		setBackground(Color.CYAN);
		setLayout(null);

		int W = 1180;
		int H = 1150;
		int MARGIN = 10;

		javax.swing.border.Border CARD_BORDER = cardBorder();

		JButton backButton = new JButton("Admin Panel");
		backButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(AdminPanelEvents.this);
				topFrame.setContentPane(new AdminPanel(repository, adminUser, scheduler));
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
				topFrame.setContentPane(new AuthenticationPanel(repository, scheduler));
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		logoutButton.setBounds(W - (W / 5) - MARGIN, MARGIN, W / 5, H / 30);
		add(logoutButton);

		JPanel calendarCard = new JPanel();
		calendarCard.setLayout(null);
		calendarCard.setBackground(Color.YELLOW);
		calendarCard.setBorder(CARD_BORDER);
		calendarCard.setBounds(W / 2 + MARGIN / 2, MARGIN, W / 2 - MARGIN * 6 / 2, H / 2 - MARGIN * 6 / 2);
		add(calendarCard);

		JLabel calendarTitle = new JLabel("Calendar (Admin " + adminUser.getUsername() + ")");
		calendarTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		calendarTitle.setBounds(MARGIN, MARGIN, 300, 20);
		calendarCard.add(calendarTitle);

		CalendarPanel calendarPanel = new CalendarPanel();
		calendarPanel.setBounds(MARGIN, 2 * MARGIN + 20, 376, 290);
		calendarCard.add(calendarPanel);

		JPanel eventPane = new JPanel();
		eventPane.setLayout(null);
		eventPane.setBackground(Color.WHITE);
		eventPane.setBorder(CARD_BORDER);
		eventPane.setBounds(MARGIN, 2 * MARGIN + 40, W / 2 - 3 * MARGIN / 2, H / 2 - 3 * MARGIN);
		add(eventPane);

		setupEvents(eventPane, W, H, MARGIN, CARD_BORDER);
		setupInvites(W, H, MARGIN, CARD_BORDER);
	}

	private javax.swing.border.Border cardBorder() {
		return new CompoundBorder(
				new LineBorder(Color.GRAY, 1, true),
				new EmptyBorder(8, 8, 8, 8)
		);
	}
	
	private JPanel createEventCard(Event event, JPanel eventsCardsPanel, javax.swing.border.Border CARD_BORDER) {
	    JPanel card = new JPanel();
	    card.setLayout(null);
	    card.setBorder(CARD_BORDER);
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
	    deleteButton.setFont(new Font("Arial", Font.PLAIN, 10));
	    deleteButton.setForeground(Color.WHITE);
	    deleteButton.setFocusPainted(false);
	    deleteButton.setBorderPainted(false);
	    deleteButton.setOpaque(true);
	    deleteButton.setBackground(new Color(220, 53, 69)); 
	    deleteButton.setBounds(MARGIN + 130, 95, 120, 24);
	    
	    JButton updateButton = new JButton("Update Event");
	    updateButton.setFont(new Font("Arial", Font.PLAIN, 10));
	    updateButton.setForeground(Color.WHITE);
	    updateButton.setFocusPainted(false);
	    updateButton.setBorderPainted(false);
	    updateButton.setOpaque(true);
	    updateButton.setBackground(new Color(25, 42, 86));
	    updateButton.setBounds(MARGIN, 95, 120, 24);
	    updateButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(card);
	            topFrame.setContentPane(new EventPanel(repository, adminUser, false, event, scheduler));
	            
	            topFrame.revalidate();
	            topFrame.repaint();
	        }
	    });
	    		
	    //card.setLayout(new FlowLayout(FlowLayout.LEFT));

	    card.add(deleteButton);
	    card.add(updateButton);
	    
	    deleteButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            EventManager eventManager = new EventManager(repository);
	            eventManager.deleteEvent(event);

	            int index = -1;
	            Component[] components = eventsCardsPanel.getComponents();
	            for (int i = 0; i < components.length; i++) {
	                if (components[i] == card) {
	                    index = i;
	                    break;
	                }
	            }

	            if (index != -1) {
	                eventsCardsPanel.remove(index);
	                if (index < eventsCardsPanel.getComponentCount()) {
	                    Component next = eventsCardsPanel.getComponent(index);
	                    if (next instanceof Box.Filler) {
	                        eventsCardsPanel.remove(index); 
	                    }
	                }
	            }

	            eventsCardsPanel.revalidate();
	            eventsCardsPanel.repaint();
	        }
	    });
	    card.add(deleteButton);

	    return card;
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
	            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(eventPane);
	            topFrame.setContentPane(new EventPanel(repository, adminUser, true, null, scheduler));
	            topFrame.revalidate();
	            topFrame.repaint();
	        }
	    });
	    eventPane.add(createEventButton);

	    JPanel eventsCardsPanel = new JPanel();
	    eventsCardsPanel.setLayout(new javax.swing.BoxLayout(eventsCardsPanel, javax.swing.BoxLayout.Y_AXIS));
	    eventsCardsPanel.setBackground(Color.WHITE);
	    eventsCardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

	    //Event[] events = new Event[]{event1, event2};
	    Event[] events = new Event[]{event1, event2, event1, event2, event1, event2};

	    for (Event event : events) {
	        JPanel card = createEventCard(event, eventsCardsPanel, CARD_BORDER);
	        card.setAlignmentX(Component.LEFT_ALIGNMENT);
	        eventsCardsPanel.add(card);
	        eventsCardsPanel.add(Box.createVerticalStrut(10));
	    }

	    JScrollPane eventsScrollPane = new JScrollPane(
	            eventsCardsPanel,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
	    );
	    eventsScrollPane.setBounds(MARGIN, 45, W / 2 - 7 * MARGIN / 2, H / 2 - 10 * MARGIN);
	    eventsScrollPane.getVerticalScrollBar().setUnitIncrement(12);
	    eventsScrollPane.setBorder(null);
	    
	    eventPane.add(eventsScrollPane);
	}

	private void setupInvites(int W, int H, int MARGIN, javax.swing.border.Border CARD_BORDER) {
		EventManager eventManager = new EventManager(repository);

		JPanel invitesPane = new JPanel();
		invitesPane.setLayout(null);
		invitesPane.setBackground(Color.WHITE);
		invitesPane.setBorder(CARD_BORDER);
		invitesPane.setBounds(MARGIN, H / 2 + 2 * MARGIN, W / 2 - 3 * MARGIN / 2, H / 2 - 7 * MARGIN);
		add(invitesPane);

		JLabel invitesPaneTitle = new JLabel("Invites");
		invitesPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
		invitesPaneTitle.setBounds(MARGIN, MARGIN, 200, 24);
		invitesPane.add(invitesPaneTitle);

		JPanel invitesCardsPanel = new JPanel();
		invitesCardsPanel.setLayout(null);
		invitesCardsPanel.setBackground(Color.WHITE);

		Invite invite1 = new Invite(adminUser.getUsername(), event1.getEventID());
		Invite invite2 = new Invite(adminUser.getUsername(), event2.getEventID());
		Invite[] invites = new Invite[]{invite1, invite2};
		Event[] inviteEvents = new Event[]{event1, event2};

		int inviteCardHeight = 60;
		int inviteCardWidth = W / 2 - 10 * MARGIN / 2;
		int inviteCardSpacing = 68;
		invitesCardsPanel.setPreferredSize(new Dimension(290, invites.length * inviteCardSpacing));

		for (int i = 0; i < invites.length; i++) {
			Invite invite = invites[i];
			Event event = inviteEvents[i];

			JPanel inviteCard = new JPanel();
			inviteCard.setLayout(null);
			inviteCard.setBackground(new Color(250, 250, 250));
			inviteCard.setBorder(CARD_BORDER);
			inviteCard.setBounds(0, i * inviteCardSpacing, inviteCardWidth, inviteCardHeight);

			JLabel nameLabel = new JLabel(event.getEventName());
			nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
			nameLabel.setBounds(MARGIN, MARGIN, inviteCardWidth, 16);
			inviteCard.add(nameLabel);

			JButton acceptButton = new JButton("Accept");
			acceptButton.setFont(new Font("Arial", Font.PLAIN, 10));
			acceptButton.setBackground(Color.GREEN);
			acceptButton.setForeground(Color.WHITE);
			acceptButton.setBounds(MARGIN, 30, 70, 20);
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
					eventManager.rejectInvite(invite, event);
				}
			});
			inviteCard.add(declineButton);

			invitesCardsPanel.add(inviteCard);
		}

		JScrollPane invitesScrollPane = new JScrollPane(invitesCardsPanel);
		invitesScrollPane.setBounds(MARGIN, 45, W / 2 - 7 * MARGIN / 2, H / 2 - 13 * MARGIN);
		invitesPane.add(invitesScrollPane);
	}
}
