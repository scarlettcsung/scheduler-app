package GUI;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Event.Event;

public class GUIMainframe extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    private static javax.swing.border.Border cardBorder() {
        return new CompoundBorder(
            new LineBorder(Color.GRAY, 1, true),
            new EmptyBorder(8, 8, 8, 8)
        );
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUIMainframe frame = new GUIMainframe();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GUIMainframe() {
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 1200);
        
        // Grab actual size after setBounds
        int W 				= getWidth();
        int H				= getHeight();
        int MARGIN          = 10;

        // Reusable border
        javax.swing.border.Border CARD_BORDER = new CompoundBorder(
            new LineBorder(Color.GRAY, 1, true),
            new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)
        );
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.GRAY);
        contentPane.setBorder(CARD_BORDER);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ── Logout button ────────────────────────────────────────────
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 11));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setBackground(Color.WHITE);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
        logoutButton.setBounds(MARGIN, MARGIN, W/5, H/30);
        
        contentPane.add(logoutButton);

        // ── Calendar card ────────────────────────────────────────────
        JPanel calendarCard = new JPanel();
        calendarCard.setLayout(null);
        calendarCard.setBackground(Color.WHITE);
        calendarCard.setBorder(CARD_BORDER);
        calendarCard.setBounds(W/2+MARGIN/2, MARGIN, W/2-MARGIN*6/2, H/2-MARGIN*6/2);
        contentPane.add(calendarCard);

        JLabel calendarTitle = new JLabel("Calendar");
        calendarTitle.setFont(new Font("Arial", Font.BOLD, 14));
        calendarTitle.setBounds(MARGIN, MARGIN, 200, 20);
        calendarCard.add(calendarTitle);

        CalendarPanel calendarPanel = new CalendarPanel();
        calendarPanel.setBounds(MARGIN, 2*MARGIN+20, 376, 290);
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.addCalendarListener(new CalendarListener() {
            @Override
            public void selectedDateChanged(CalendarSelectionEvent event) {
                LocalDate selectedDate = event.getNewDate();
                System.out.println("Selected date: " + selectedDate);
            }
            @Override
            public void yearMonthChanged(YearMonthChangeEvent event) {}
        });
        calendarCard.add(calendarPanel);

        // ── Event pane ───────────────────────────────────────────────
        JPanel eventPane = new JPanel();
        eventPane.setLayout(null);
        eventPane.setBackground(Color.WHITE);
        eventPane.setBorder(CARD_BORDER);
        eventPane.setBounds(MARGIN, 2*MARGIN+20, W/2-3/2*MARGIN, H/2-3*MARGIN);
        contentPane.add(eventPane);

        JLabel eventPaneTitle = new JLabel("Events");
        eventPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
        eventPaneTitle.setBounds(MARGIN, MARGIN, 160, 24);
        eventPane.add(eventPaneTitle);

        // Create event button next to title
        JButton createEventButton = new JButton("+ Create Event");
        createEventButton.setFont(new Font("Arial", Font.PLAIN, 11));
        createEventButton.setBackground(new Color(0, 120, 215));
        createEventButton.setForeground(Color.WHITE);
        createEventButton.setBounds(170, MARGIN, 120, 24);
        createEventButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create Event clicked");
            }
        });
        eventPane.add(createEventButton);

        JPanel eventDivider = new JPanel();
        eventDivider.setBackground(new Color(200, 200, 200));
        eventDivider.setBounds(MARGIN, 38, W/2-7/2*MARGIN, 1);
        eventPane.add(eventDivider);

        // Sample events
        Event[] events = new Event[]{
        	    new Event(
        	        "Team Meeting", 
        	        60, 
        	        "yappen met de bros", 
        	        null, 
        	        false, 
        	        new ArrayList<>()
        	    ),
        	    new Event(
        	        "Deadline", 
        	        120, 
        	        "strijden voor de deadline", 
        	        null, 
        	        false, 
        	        new ArrayList<>()
        	    )
        	};

        JPanel eventsCardsPanel = new JPanel();
        eventsCardsPanel.setLayout(null);
        eventsCardsPanel.setBackground(Color.WHITE);
        int cardWidth = W/2-10/2*MARGIN;
        int cardHeight = 140;
        int cardSpacing = 155;
        eventsCardsPanel.setPreferredSize(new java.awt.Dimension(cardWidth + 10, events.length * cardSpacing));

        for (int i = 0; i < events.length; i++) {
            Event event = events[i];

            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBackground(new Color(250, 250, 250));
            card.setBorder(CARD_BORDER);
            card.setBounds(0, i * cardSpacing, cardWidth, cardHeight);

            JLabel nameLabel = new JLabel(event.getEventName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
            nameLabel.setBounds(MARGIN, MARGIN, cardWidth - 20, 20);
            card.add(nameLabel);

            JLabel timeLabel = new JLabel("Time: " + event.getEventTime().toString().replace("T", " "));
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            timeLabel.setForeground(Color.BLACK);
            timeLabel.setBounds(MARGIN, 34, cardWidth - 20, 16);
            card.add(timeLabel);

            JLabel durationLabel = new JLabel("Duration: " + event.getEventDuration() + " min");
            durationLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            durationLabel.setForeground(Color.BLACK);
            durationLabel.setBounds(MARGIN, 53, cardWidth - 20, 16);
            card.add(durationLabel);

            JLabel descLabel = new JLabel(event.getEventDescription());
            descLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            descLabel.setForeground(Color.BLACK);
            descLabel.setBounds(MARGIN, 72, cardWidth - 20, 16);
            card.add(descLabel);

            JButton viewButton = new JButton("View Event");
            viewButton.setBounds(MARGIN, 100, 100, 26);
            viewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Clicked event: " + event.getEventName());
                }
            });
            card.add(viewButton);

            eventsCardsPanel.add(card);
        }

        JScrollPane eventsScrollPane = new JScrollPane(eventsCardsPanel);
        eventsScrollPane.setBounds(MARGIN, 45, W/2-7/2*MARGIN, H/2-5*MARGIN);
        eventsScrollPane.setBorder(null);
        eventsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        eventPane.add(eventsScrollPane);

        // ── Invites pane ─────────────────────────────────────────────
        JPanel invitesPane = new JPanel();
        invitesPane.setLayout(null);
        invitesPane.setBackground(Color.WHITE);
        invitesPane.setBorder(CARD_BORDER);
        invitesPane.setBounds(MARGIN, H/2+2*MARGIN, W/2-3/2*MARGIN, H/2-7*MARGIN);
        contentPane.add(invitesPane);

        JLabel invitesPaneTitle = new JLabel("Invites");
        invitesPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
        invitesPaneTitle.setBounds(MARGIN, MARGIN, 200, 24);
        invitesPane.add(invitesPaneTitle);

        JPanel invitesDivider = new JPanel();
        invitesDivider.setBackground(new Color(200, 200, 200));
        invitesDivider.setBounds(MARGIN, 36, W/2-7/2*MARGIN, 1);
        invitesPane.add(invitesDivider);

        // Sample invite cards panel
        JPanel invitesCardsPanel = new JPanel();
        invitesCardsPanel.setLayout(null);
        invitesCardsPanel.setBackground(Color.WHITE);

        // Placeholder invite cards
        String[] invites = {"Invite from Alice — Team Lunch", "Invite from Bob — Sprint Review"};
        int inviteCardHeight = 60;
        int inviteCardWidth = W/2-10/2*MARGIN;
        int inviteCardSpacing = 68;
        invitesCardsPanel.setPreferredSize(new java.awt.Dimension(290, invites.length * inviteCardSpacing));

        for (int i = 0; i < invites.length; i++) {
            JPanel inviteCard = new JPanel();
            inviteCard.setLayout(null);
            inviteCard.setBackground(new Color(250, 250, 250));
            inviteCard.setBorder(CARD_BORDER);
            inviteCard.setBounds(0, i * inviteCardSpacing, inviteCardWidth, inviteCardHeight);

            JLabel inviteLabel = new JLabel(invites[i]);
            inviteLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            inviteLabel.setForeground(new Color(60, 60, 60));
            inviteLabel.setBounds(MARGIN, MARGIN, inviteCardWidth, 16);
            inviteCard.add(inviteLabel);

            JButton acceptButton = new JButton("Accept");
            acceptButton.setFont(new Font("Arial", Font.PLAIN, 10));
            acceptButton.setBackground(Color.GREEN);
            acceptButton.setForeground(Color.WHITE);
            acceptButton.setBounds(MARGIN, 30, 70, 20);
            acceptButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Accepted: " + inviteLabel.getText());
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
                    System.out.println("Declined: " + inviteLabel.getText());
                }
            });
            inviteCard.add(declineButton);

            invitesCardsPanel.add(inviteCard);
        }

        JScrollPane invitesScrollPane = new JScrollPane(invitesCardsPanel);
        invitesScrollPane.setBounds(MARGIN, 45, W/2-7/2*MARGIN,  H/2-13*MARGIN);
        invitesScrollPane.setBorder(null);
        invitesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        invitesPane.add(invitesScrollPane);
    }
}