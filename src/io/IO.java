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

/**
 * Handles JSON-based persistence for users and their calendars.
 *
 * @author AA NJ
 * @version 2
 */
public class IO {

    /**
     * Reads a list of users and calendar data from a JSON file.
     *
     * @param filePath path to the JSON file
     * @return users loaded from the file
     * @throws IOException when the file cannot be read
     */
    public List<User> readUsers(String filePath) {
        JsonDeserializer<Event> eventDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean isImportedField = (jsonObject.has("isImportedField") && jsonObject.get("isImportedField").getAsBoolean())
                    || (jsonObject.has("isImported") && jsonObject.get("isImported").getAsBoolean()); // old

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

        try (FileReader reader = new FileReader(filePath)) {
            User[] users = gson.fromJson(reader, User[].class);
            return (users == null) ? Collections.emptyList() : Arrays.asList(users);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to read users from: " + filePath);
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    /**
     * Reads a list of events from a JSON file.
     */
    public List<Event> readEvents(String filePath) {
        JsonDeserializer<Event> eventDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean isImported = (jsonObject.has("isImportedField") && jsonObject.get("isImportedField").getAsBoolean())
                    || (jsonObject.has("isImported") && jsonObject.get("isImported").getAsBoolean());

            if (isImported) {
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

        try (FileReader reader = new FileReader(filePath)) {
            Event[] events = gson.fromJson(reader, Event[].class);
            return Arrays.asList(events);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Writes user data to a JSON file.
     *
     * @param userList users to persist
     * @param filePath destination file path
     * @throws IOException when the file cannot be written
     */
    public void writeUsers(List<User> userList, String filePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (src, type, context) -> new JsonPrimitive(src.toString()))
                .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(userList, writer);
        } catch (IOException e) {
            System.err.println("Failed to write users to: " + filePath);
            e.printStackTrace();
        }
    }
    
    /**
     * Writes a list of events to a JSON file.
     *
     * @param eventList events to persist
     * @param filePath destination file path
     */
    public void writeEvents(List<Event> eventList, String filePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (src, type, context) -> new JsonPrimitive(src.toString()))
                .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(eventList, writer);
        } catch (IOException e) {
            System.err.println("Failed to write events to: " + filePath);
            e.printStackTrace();
        }
    }
}
