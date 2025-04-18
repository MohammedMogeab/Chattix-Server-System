package com.example.chatwebsite.Controller;


import com.example.chatwebsite.Service.GroupChatService;
import com.example.chatwebsite.Service.MessageService;
import com.example.chatwebsite.Service.UserService;
import com.example.chatwebsite.model.GroupChat;
import com.example.chatwebsite.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GroupChatService groupChatService;
    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroup(@RequestParam String groupName, @RequestParam Long userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(groupChatService.createGroupChat(groupName, user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<GroupChat>> getAllGroups() {
        return ResponseEntity.ok(groupChatService.getAllGroups());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupChat> getGroupById(@PathVariable Long groupId) {
        return groupChatService.getGroupById(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/groups")
    public ResponseEntity<List<GroupChat>> getUserGroups(@PathVariable String userId) {
        Long userIdfromusername =  userService.getuseridbyusername(userId);
        List<GroupChat> groups = groupChatService.getUserGroups(userIdfromusername);
        return ResponseEntity.ok(groups);
    }




}




