package io;

import java.util.*;
import java.io.*;

import event.*;
import repository.UserRepository;
import user.User;
import user.calendar.UserCalendar;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class IO {
    private Gson createReaderGson() {

        JsonDeserializer<Event> eventDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();

            boolean isImportedField = jsonObject.has("isImported") 
                    && jsonObject.get("isImported").getAsBoolean();

            if (isImportedField) {
                return context.deserialize(json, ImportedEvent.class);
            } else {
                return context.deserialize(json, CreatedEvent.class);
            }
        };

        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>)
                                (json, type, context) -> LocalDateTime.parse(json.getAsString()))
                .registerTypeAdapter(Event.class, eventDeserializer)
                .create();
    }
    private Gson createWriterGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>)
                                (src, type, context) -> new JsonPrimitive(src.toString()))
                .create();
    }

    public List<User> readUsers(String filePath) throws IOException {

        // CHANGED: removed GsonBuilder setup → replaced with method call
        Gson gson = createReaderGson();

        FileReader reader = new FileReader(filePath);
        User[] users = gson.fromJson(reader, User[].class);
        reader.close();

        return Arrays.asList(users);
    }

    public void writeUsers(List<User> userList, String filePath) throws IOException {

        // CHANGED: removed GsonBuilder setup → replaced with method call
        Gson gson = createWriterGson();

        FileWriter writer = new FileWriter(filePath);
        gson.toJson(userList, writer);
        writer.close();
    }
}