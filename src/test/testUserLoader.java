package test;

import java.util.List;
import java.util.Optional;

import junit.framework.TestCase;

public class testUserLoader extends TestCase {
public static void loadUsers() {
    	
        String filePath = "src/Test/test.recources/UserListTest.txt";
        
        Optional<List<User>> result = loadUsers(filePath);
        
        if (result.isEmpty()) {
            System.out.println("no users in list");
            return
        }
        
        List<User> users = result.get();
        
        for (User user : users) {
            System.out.println("--- User ---");
            System.out.println("Username  : " + user.getUsername());
            System.out.println("Password  : " + user.getPassword());
            System.out.println("Calendar  : " + user.getMyCalendar());
            System.out.println("Is Admin  : " + user.isAdmin());
            System.out.println();
        }
}
