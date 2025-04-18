package com.example.chatwebsite.Controller;

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
    private String recipientUsername; // For private messages
    private Long groupId; // For group messages
}