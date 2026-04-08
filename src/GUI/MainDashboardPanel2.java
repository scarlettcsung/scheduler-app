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

import Event.Event;
import Invite.Invite;
import UserRepository.UserRepository;
import User.User;

public class MainDashboardPanel2 extends JPanel {
    
    public static final long serialVersionUID = 1L;
    private UserRepository repository;
    private User currentUser;
    
    private Event event1 = new Event(
	        "Team Meeting", 
	        60, 
	        "yappen met de bros", 
	        currentUser, 
	        false, 
	        new ArrayList<>()
	    );
    private Event event2 = new Event(
    		"Deadline", 
    		120, 
    		"strijden voor de deadline", 
    		currentUser, 
    		false, 
    		new ArrayList<>()
    );  
    
    private javax.swing.border.Border cardBorder() {
        return new CompoundBorder(
                new LineBorder(Color.gray, 1, true),
                new EmptyBorder(8, 8, 8, 8)
                );
    }
    
    public MainDashboardPanel2(UserRepository repository, User user) {
        this.repository = repository;
        this.currentUser = user;
        
        // UI Setup
        setBackground(Color.CYAN);
        setLayout(null); // Allows manual bounds setting
        
        // Fixed dimensions based on mainframe design
        int W = 1180;
        int H = 1150;
        int MARGIN = 10;
        
        javax.swing.border.Border CARD_BORDER = cardBorder();
        
        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Dialog", Font.PLAIN, 11)); // Changed to Dialog for compatibility
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Find the parent JFrame and swap back to Login
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainDashboardPanel2.this);
                topFrame.setContentPane(new AuthenticationPanel(repository));
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
        setupInvites(W, H, MARGIN, CARD_BORDER);
    }

    private void setupEvents(JPanel eventPane, int W, int H, int MARGIN, javax.swing.border.Border CARD_BORDER) {
        JLabel eventPaneTitle = new JLabel("Events");
        eventPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
        eventPaneTitle.setBounds(MARGIN, MARGIN, 160, 24);
        eventPane.add(eventPaneTitle);

        JButton createEventButton = new JButton("+ Create Event");
        createEventButton.setBounds(170, MARGIN, 120, 24);
        eventPane.add(createEventButton);

        // scrollable event area
        JPanel eventsCardsPanel = new JPanel();
        eventsCardsPanel.setLayout(null);
        eventsCardsPanel.setBackground(Color.WHITE);
           
        
        
        /* Sample Events
        
        */
        Event[] events = new Event[]{
        	    event1, event2};

        int cardWidth = W/2-10/2*MARGIN;
        int cardHeight = 140;
        int cardSpacing = 155;
        eventsCardsPanel.setPreferredSize(new Dimension(cardWidth, events.length * cardSpacing));

        for (int i = 0; i < events.length; i++) {
        	Event event = events[i];
        	
            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBorder(CARD_BORDER);
            card.setBounds(0, i * cardSpacing, cardWidth, cardHeight);
            
            JLabel nameLabel = new JLabel(event.getEventName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
            nameLabel.setBounds(MARGIN, MARGIN, cardWidth - 20, 20);
            card.add(nameLabel);

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
        eventsScrollPane.setBounds(MARGIN, 45, W/2-7/2*MARGIN, H/2-10*MARGIN);
        eventPane.add(eventsScrollPane);
    }

    private void setupInvites(int W, int H, int MARGIN, javax.swing.border.Border CARD_BORDER) {
        JPanel invitesPane = new JPanel();
        invitesPane.setLayout(null);
        invitesPane.setBackground(Color.WHITE);
        invitesPane.setBorder(CARD_BORDER);
        // Positioned at the bottom left
        invitesPane.setBounds(MARGIN, H/2+2*MARGIN, W/2-3/2*MARGIN, H/2-7*MARGIN);
        this.add(invitesPane);

        JLabel invitesPaneTitle = new JLabel("Invites");
        invitesPaneTitle.setFont(new Font("Arial", Font.BOLD, 16));
        invitesPaneTitle.setBounds(MARGIN, MARGIN, 200, 24);
        invitesPane.add(invitesPaneTitle);

        // Invite card logic
        JPanel invitesCardsPanel = new JPanel();
        invitesCardsPanel.setLayout(null);
        invitesCardsPanel.setBackground(Color.WHITE);
        
        Invite invite1 = new Invite(
    	    	currentUser, 
    	    	event1
    	    	
    	    );
        Invite invite2 = new Invite(
        	    currentUser, 
        	    event2
    	    );        
        
        Invite[] invites = new Invite[]{
        	    invite1,invite2};
        
        int inviteCardHeight = 60;
        int inviteCardWidth = W/2-10/2*MARGIN;
        int inviteCardSpacing = 68;
        invitesCardsPanel.setPreferredSize(new java.awt.Dimension(290, invites.length * inviteCardSpacing));

        
        for (int i = 0; i < invites.length; i++) {
        	Invite invite = invites[i];
        	Event event = invite.getEvent();
        	
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
                	invite.reject();
                }
            });
            inviteCard.add(declineButton);

            invitesCardsPanel.add(inviteCard);
        }
        
        JScrollPane invitesScrollPane = new JScrollPane(invitesCardsPanel);
        invitesScrollPane.setBounds(MARGIN, 45, W/2-7/2*MARGIN, H/2-13*MARGIN);
        invitesPane.add(invitesScrollPane);
    }
}