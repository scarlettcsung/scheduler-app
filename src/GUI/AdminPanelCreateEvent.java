package GUI;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Repository.UserRepository;
import User.User;
import net.miginfocom.swing.MigLayout;

public class AdminPanelCreateEvent extends JPanel {

	private static final long serialVersionUID = 1L;

	
	public AdminPanelCreateEvent(UserRepository repository, User adminUser) {
		
		setBackground(Color.ORANGE);
		setLayout(new MigLayout("", "[75px][75px][75px][][][75px][75px][75px]", "[50px][50px][50px][50px][50px][50px]"));
		
		JLabel label = new JLabel("");
		add(label, "cell 0 0,grow");
		
		JLabel label_1 = new JLabel("");
		add(label_1, "cell 1 0,grow");
		
		JLabel label_2 = new JLabel("");
		add(label_2, "cell 2 0,grow");
		
		JLabel label_3 = new JLabel("");
		add(label_3, "cell 5 0,grow");
		
		JLabel label_4 = new JLabel("");
		add(label_4, "cell 6 0,grow");
		
		JLabel label_5 = new JLabel("");
		add(label_5, "cell 7 0,grow");
		
		JLabel label_6 = new JLabel("");
		add(label_6, "cell 0 1,grow");
		
		JLabel label_7 = new JLabel("");
		add(label_7, "cell 1 1,grow");
		
		JLabel label_8 = new JLabel("");
		add(label_8, "cell 2 1,grow");
		
		

	}

}
