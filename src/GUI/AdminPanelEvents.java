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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
	private final JPanel eventPane;
	private final JPanel invitesPane;
	private final javax.swing.border.Border cardBorder;

	public AdminPanelEvents(UserRepository repository, User adminUser, Scheduler scheduler) {
		this.repository = repository;
		this.adminUser = adminUser;
		this.scheduler = scheduler;

		setBackground(Color.CYAN);
		setLayout(null);

		int W = 1180;
		int H = 1150;
		int MARGIN = 10;

		cardBorder = cardBorder();

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
		calendarCard.setBorder(cardBorder);
		calendarCard.setBounds(W / 2 + MARGIN / 2, MARGIN, W / 2 - MARGIN * 6 / 2, H / 2 - MARGIN * 6 / 2);
		add(calendarCard);

		JLabel calendarTitle = new JLabel("Calendar (Admin " + adminUser.getUsername() + ")");
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
		eventPane.setBounds(MARGIN, 2 * MARGIN + 40, W / 2 - 3 * MARGIN / 2, H / 2 - 3 * MARGIN);
		add(eventPane);

		invitesPane = new JPanel();
		invitesPane.setLayout(null);
		invitesPane.setBackground(Color.WHITE);
		invitesPane.setBorder(cardBorder);
		invitesPane.setBounds(MARGIN, H / 2 + 2 * MARGIN, W / 2 - 3 * MARGIN / 2, H / 2 - 7 * MARGIN);
		add(invitesPane);

		setupEvents();
		setupInvites();
	}

	private javax.swing.border.Border cardBorder() {
		return new CompoundBorder(
				new LineBorder(Color.GRAY, 1, true),
				new EmptyBorder(8, 8, 8, 8)
		);
	}

	private List<Event> collectUniqueEvents() {
		Set<Event> allEvents = new LinkedHashSet<>();
		for (User user : repository.getListUsers()) {
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
				topFrame.setContentPane(new EventPanel(repository, adminUser, true, null, scheduler, () -> {
					topFrame.setContentPane(new AdminPanelEvents(repository, adminUser, scheduler));
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
				topFrame.setContentPane(new EventPanel(repository, adminUser, false, event, scheduler, () -> {
					topFrame.setContentPane(new AdminPanelEvents(repository, adminUser, scheduler));
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
					eventManager.rejectInvite(invite, event);
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
		topFrame.setContentPane(new AdminPanelEvents(repository, adminUser, scheduler));
		topFrame.revalidate();
		topFrame.repaint();
	}
}
