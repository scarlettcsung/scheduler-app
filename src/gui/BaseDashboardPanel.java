package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import repository.EventRepository;
import repository.UserRepository;
import scheduler.Scheduler;
import user.User;

public abstract class BaseDashboardPanel extends JPanel {
	protected UserRepository repository;
	protected EventRepository eventRepository;
	protected Scheduler scheduler;
	protected User activeUser;
   
    public BaseDashboardPanel(UserRepository repository, User user, Scheduler scheduler, EventRepository eventRepository) {
        this.repository = repository;
        this.activeUser = user;
        this.scheduler = scheduler;
        this.eventRepository = eventRepository;
    }

    protected void refreshEvents() {
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		topFrame.setContentPane(new UserPanel(repository, activeUser, scheduler,eventRepository));
		topFrame.revalidate();
		topFrame.repaint(); 
		}
  
    protected abstract BaseDashboardPanel createRefreshPanel();

}
