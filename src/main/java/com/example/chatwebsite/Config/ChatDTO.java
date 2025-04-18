package com.example.chatwebsite.Config;

import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides null values in JSON
public class ChatDTO {
    private User chatUser;
    private Message lastMessage;

    public ChatDTO(User chatUser, Message lastMessage) {
        this.chatUser = chatUser;
        this.lastMessage = lastMessage.getMessageId() != null ? lastMessage : null;
    }

    // Getters and setters
}

