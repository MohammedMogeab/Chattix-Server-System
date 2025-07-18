package com.example.chatwebsite.Controller;

import com.example.chatwebsite.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String content;
    private Long userId; // Consider if you really need this, senderUsername might be enough
    private String senderUsername;
    private String recipientUsername;
    private String type; // // For private messages
    private Long groupId; // For group messages

    public MessageDTO(Message message) {
        this.content = message.getContent();
        this.type=message.getType();
        this.senderUsername=message.getSender().getUsername();
        this.recipientUsername=message.getRecipient().getUsername();

    }
}