package IO;
//readUserList(filePath): userList, writeUserList(userList,filePath): void, 
// readCalender(userList): void, writeCalender(userList):void.
import java.util.*;
import java.io.*;
import User.User;
import UserCalendar.UserCalendar;
import Event.Event;
import Repository.UserRepository;

//Additional Packages
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/**
 * Handles JSON-based persistence for users and their calendars.
 *
 * @author AA NJ
 * @version 2
 */
public class IO {

    // Reads calendar data for all users from files (example: each user has a calendar file)
    /**
     * Reads a list of users and calendar data from a JSON file.
     *
     * @param filePath path to the JSON file
     * @return users loaded from the file
     * @throws IOException when the file cannot be read
     */
    public List<User> readUsers(String filePath) throws IOException{
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
    /**
     * Writes user and calendar data to a JSON file.
     *
     * @param userList users to persist
     * @param filePath destination file path
     * @throws IOException when the file cannot be written
     */
    public void writeUsers(List<User> userList, String filePath) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,(JsonSerializer<LocalDateTime>)
        		(src,type,context)->new JsonPrimitive(src.toString())).create();
        FileWriter writer = new FileWriter(filePath);
        gson.toJson(userList, writer);
        writer.close();
    }
    }
