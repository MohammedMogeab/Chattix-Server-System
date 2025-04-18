package com.example.chatwebsite.Config;

import com.example.chatwebsite.model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String username = p.getText(); // Extract username from the JSON (assuming you're passing a username)
        User user = new User();
        user.setUsername(username); // Set it on the User object
        return user;
    }
}
