package com.example.chatwebsite.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GroupMessageDTO {
    private String groupName;
    private String senderUsername;
    private String content;
    private LocalDateTime sentAt;
}
