package UserLoader;

import User.User;
import UserCalendar.UserCalendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class UserLoader {
	
    public static Optional<List<User>> loadUsers(String filePath) {
        List<User> users = new ArrayList<>();
        File file = new File(filePath);
        
        try (Scanner reader = new Scanner(file)) {
        	
            while (reader.hasNextLine()) {
            	
                String username = reader.nextLine();
                
                if (username.trim().isEmpty()) {
                    continue;
                }
                
                if (!reader.hasNextLine()) break;
                String password = reader.nextLine();
                
                if (!reader.hasNextLine()) break;
                String adminLine = reader.nextLine();
                
                boolean isAdmin = Boolean.parseBoolean(adminLine);

                UserCalendar myCalendar = null;
                //format user: (String username, String password, UserCalendar myCalendar, boolean isAdmin)
                
                User user = new User(username, password, myCalendar);
                users.add(user);
            }
            
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
        
        return Optional.of(users);
    }
    
    public static void loadUsers() {
    	
        String filePath = "src/UserLoader/UserList.txt";
        
        Optional<List<User>> result = loadUsers(filePath);
        
        if (result.isEmpty()) {
            return;
        }
    }
}