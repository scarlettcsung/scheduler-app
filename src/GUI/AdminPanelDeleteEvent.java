package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Event.Event;
import User.User;
import UserRepository.UserRepository;

public class AdminPanelDeleteEvent extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int PANEL_WIDTH = 1000;
	private static final int PANEL_HEIGHT = 700;
	private static final int MARGIN = 10;

	private final UserRepository repository;
	private final User adminUser;
	private final JPanel eventsCardsPanel;

	public AdminPanelDeleteEvent(UserRepository repository, User adminUser) {
		this.repository = repository;
		this.adminUser = adminUser;

		setLayout(null);
		setBackground(Color.GRAY);
		setBorder(cardBorder());

		JButton btnBackToAdminPanel = new JButton("Admin Panel");
		btnBackToAdminPanel.setFont(new Font("Arial", Font.PLAIN, 11));
		btnBackToAdminPanel.setBackground(Color.WHITE);
		btnBackToAdminPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(AdminPanelDeleteEvent.this);
				topFrame.setContentPane(new AdminPanel(repository, adminUser,scheduler);
				topFrame.revalidate();
				topFrame.repaint();
			}
		});
		btnBackToAdminPanel.setBounds(MARGIN, MARGIN, 140, 28);
		add(btnBackToAdminPanel);

		JPanel eventPane = new JPanel();
		eventPane.setLayout(null);
		eventPane.setBackground(Color.WHITE);
		eventPane.setBorder(cardBorder());
		eventPane.setBounds(MARGIN, 2 * MARGIN + 20, PANEL_WIDTH - 4 * MARGIN, PANEL_HEIGHT - 5 * MARGIN);
		add(eventPane);

		JLabel eventPaneTitle = new JLabel("Delete Events");
		eventPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
		eventPaneTitle.setBounds(MARGIN, MARGIN, 200, 24);
		eventPane.add(eventPaneTitle);

		JPanel eventDivider = new JPanel();
		eventDivider.setBackground(new Color(200, 200, 200));
		eventDivider.setBounds(MARGIN, 38, PANEL_WIDTH - 8 * MARGIN, 1);
		eventPane.add(eventDivider);

		eventsCardsPanel = new JPanel();
		eventsCardsPanel.setLayout(null);
		eventsCardsPanel.setBackground(Color.WHITE);

		JScrollPane eventsScrollPane = new JScrollPane(eventsCardsPanel);
		eventsScrollPane.setBounds(MARGIN, 45, PANEL_WIDTH - 8 * MARGIN, PANEL_HEIGHT - 10 * MARGIN);
		eventsScrollPane.setBorder(null);
		eventsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		eventPane.add(eventsScrollPane);

		rebuildEventCards();
	}

	private javax.swing.border.Border cardBorder() {
		return new CompoundBorder(
			new LineBorder(Color.GRAY, 1, true),
			new EmptyBorder(8, 8, 8, 8)
		);
	}

	private List<Event> collectUniqueEvents() {
		List<Event> allEvents = new ArrayList<>();

		for (User user : repository.getListUsers()) {
			if (user.getCalendar() == null || user.getCalendar().getEvents() == null) {
				continue;
			}

			for (Event event : user.getCalendar().getEvents()) {
				if (event != null && !allEvents.contains(event)) {
					allEvents.add(event);
				}
			}
		}

		return allEvents;
	}

	private void rebuildEventCards() {
		eventsCardsPanel.removeAll();

		List<Event> events = collectUniqueEvents();
		int cardWidth = PANEL_WIDTH - 10 * MARGIN;
		int cardHeight = 150;
		int cardSpacing = 165;

		eventsCardsPanel.setPreferredSize(new java.awt.Dimension(cardWidth + 10, Math.max(cardSpacing, events.size() * cardSpacing)));

		if (events.isEmpty()) {
			JLabel emptyLabel = new JLabel("No events available.");
			emptyLabel.setFont(new Font("Arial", Font.PLAIN, 13));
			emptyLabel.setBounds(MARGIN, MARGIN, 200, 24);
			eventsCardsPanel.add(emptyLabel);
		}

		for (int i = 0; i < events.size(); i++) {
			Event event = events.get(i);

			JPanel card = new JPanel();
			card.setLayout(null);
			card.setBackground(new Color(250, 250, 250));
			card.setBorder(cardBorder());
			card.setBounds(0, i * cardSpacing, cardWidth, cardHeight);

			JLabel nameLabel = new JLabel(event.getEventName());
			nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
			nameLabel.setBounds(MARGIN, MARGIN, cardWidth - 20, 20);
			card.add(nameLabel);

			JLabel timeLabel = new JLabel("Time: " + formatTime(event.getEventTime()));
			timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
			timeLabel.setForeground(Color.BLACK);
			timeLabel.setBounds(MARGIN, 34, cardWidth - 20, 16);
			card.add(timeLabel);

			JLabel durationLabel = new JLabel("Duration: " + event.getEventDuration() + " min");
			durationLabel.setFont(new Font("Arial", Font.PLAIN, 11));
			durationLabel.setForeground(Color.BLACK);
			durationLabel.setBounds(MARGIN, 53, cardWidth - 20, 16);
			card.add(durationLabel);

			String organizerName = event.getOrganizer() != null ? event.getOrganizer() : "Unknown";
			JLabel organizerLabel = new JLabel("Organizer: " + organizerName);
			organizerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
			organizerLabel.setForeground(Color.BLACK);
			organizerLabel.setBounds(MARGIN, 72, cardWidth - 20, 16);
			card.add(organizerLabel);

			JLabel descLabel = new JLabel(event.getEventDescription());
			descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
			descLabel.setForeground(Color.BLACK);
			descLabel.setBounds(MARGIN, 91, cardWidth - 20, 16);
			card.add(descLabel);

			JButton deleteButton = new JButton("Delete Event");
			deleteButton.setBounds(MARGIN, 114, 120, 26);
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteEventEverywhere(event);
					JOptionPane.showMessageDialog(null, "Event deleted successfully!");
					rebuildEventCards();
				}
			});
			card.add(deleteButton);

			eventsCardsPanel.add(card);
		}

		eventsCardsPanel.revalidate();
		eventsCardsPanel.repaint();
	}

	private String formatTime(LocalDateTime eventTime) {
		if (eventTime == null) {
			return "Not scheduled";
		}
		return eventTime.toString().replace("T", " ");
	}

	private void deleteEventEverywhere(Event targetEvent) {
		for (User user : repository.getListUsers()) {
			if (user.getCalendar() != null && user.getCalendar().getEvents() != null) {
				user.getCalendar().removeEvent(targetEvent);
			}
		}
	}
}
