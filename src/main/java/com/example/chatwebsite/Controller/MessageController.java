package com.example.chatwebsite.Controller;

import com.example.chatwebsite.Service.UserService;
import com.example.chatwebsite.model.GroupChat;
import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import com.example.chatwebsite.Service.GroupChatService;
import com.example.chatwebsite.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final GroupChatService groupChatService;

    @PostMapping("/private")
    public ResponseEntity<Message> sendPrivateMessage(@RequestParam Long senderId, @RequestParam Long recipientId, @RequestParam String content) {
        Optional<User> sender = userService.getUserById(senderId);
        Optional<User> recipient = userService.getUserById(recipientId);
        
        if (sender.isPresent() && recipient.isPresent()) {
            Message message = messageService.sendPrivateMessage(sender.get(), recipient.get(), content);
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/group")
    public ResponseEntity<Message> sendGroupMessage(@RequestParam Long senderId, @RequestParam Long groupId, @RequestParam String content) {
        Optional<User> sender = userService.getUserById(senderId);
        Optional<GroupChat> group = groupChatService.getGroupById(groupId);
        
        if (sender.isPresent() && group.isPresent()) {
            Message message = messageService.sendGroupMessage(sender.get(), group.get(), content);
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/private/{userId}")
    public ResponseEntity<List<Message>> getPrivateMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getPrivateMessages(userId));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Message>> getGroupMessages(@PathVariable Long groupId) {
        return ResponseEntity.ok(messageService.getGroupMessages(groupId));
    }
}
