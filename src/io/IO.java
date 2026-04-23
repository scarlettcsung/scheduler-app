package io;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import event.CreatedEvent;
import event.Event;
import event.ImportedEvent;
import user.User;

public class IO {
    public final String FILE_PATH = "src/filestorage/userStorage.json";

    public List<User> readUsers() {
        JsonDeserializer<Event> eventDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean isImportedField = jsonObject.has("isImported") && jsonObject.get("isImported").getAsBoolean();
            if (isImportedField) {
                return context.deserialize(json, ImportedEvent.class);
            } else {
                return context.deserialize(json, CreatedEvent.class);
            }
        };
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                        (json, type, context) -> LocalDateTime.parse(json.getAsString()))
                .registerTypeAdapter(Event.class, eventDeserializer)
                .create();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            User[] users = gson.fromJson(reader, User[].class);
            return Arrays.asList(users);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_PATH);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to read users from: " + FILE_PATH);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void writeUsers(List<User> userList) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (src, type, context) -> new JsonPrimitive(src.toString()))
                .create();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(userList, writer);
        } catch (IOException e) {
            System.err.println("Failed to write users to: " + FILE_PATH);
            e.printStackTrace();
        }
    }
}