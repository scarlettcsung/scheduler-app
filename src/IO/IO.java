package IO;
//readUserList(filePath): userList, writeUserList(userList,filePath): void, 
// readCalender(userList): void, writeCalender(userList):void.
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.lang.reflect.Type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import User.User;
import UserCalendar.UserCalendar;
import Event.Event;
import UserRepository.UserRepository;

public class IO {
	List<User> userList = new ArrayList<>();
/*	
    // Reads a list of users from a file and returns it
	public List<User> readUserList(String filePath) throws FileNotFoundException {
        
        File file = new File(filePath);
        Scanner reader = new Scanner(file);
        //read every line in txt file
        while (reader.hasNextLine()){
        	//every user is grouped in 3 with first line being user name than the password and than the admin line
        	//this sets the user name
        	String username = reader.nextLine();
        	
            //if username string is empty skipp it
            if (username.trim().isEmpty()) {
                continue;
            }
            
            //read the the password line
            if (!reader.hasNextLine()) break;
            String password = reader.nextLine();
            
            //read the admin line
            if (!reader.hasNextLine()) break;
            String adminLine = reader.nextLine();
            //set admin to boolean
            boolean isAdmin = Boolean.parseBoolean(adminLine);
            //set the calender (Null for now)
            UserCalendar myCalendar = null; //change to what it is if calender is present
            
            //create the user
            User user = new User(username, password, myCalendar, isAdmin);
            //add to user list
            userList.add(user);
        }
        return userList;
    }
	

    // Writes the list of users to a file
    public void writeUserList(List<User> userList, String filePath) {
    	FileWriter writer = new FileWriter(filePath, false);
    	this.userList = userList;
    	for (User u : userList) {
            writer.write(u.getUsername() + "\n");
            writer.write(u.getPassword() + "\n");
            writer.write(u.isAdmin() + "\n");
            writer.write("\n");
        }
    	writer.close();
    } 
*/
    // Reads calendar data for all users from files (example: each user has a calendar file)
    public List<User> readCalendar(String filePath) throws IOException{
    	Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) 
                (json, type, context) -> LocalDateTime.parse(json.getAsString()))
            .create();

        FileReader reader = new FileReader(filePath);
        User[] users = gson.fromJson(reader, User[].class);
        reader.close();
       

        return Arrays.asList(users);
    }

    // Writes calendar data for all users to files
    public void writeCalendar(List<User> userList, String filePath) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,(JsonSerializer<LocalDateTime>)
        		(src,type,context)->new JsonPrimitive(src.toString())).create();
        FileWriter writer = new FileWriter(filePath);
        gson.toJson(userList, writer);
        writer.close();
    }
    }