package com.example.chatwebsite.Config;

import com.example.chatwebsite.Controller.GroupMessageDTO;
import com.example.chatwebsite.Controller.MessageDTO;
import com.example.chatwebsite.Service.GroupChatService;
import com.example.chatwebsite.Service.UserService;
import com.example.chatwebsite.model.*;
import com.example.chatwebsite.Service.MessageService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
//    private final MessageService messageService;
    private static Set<String> onlineUsers = new HashSet<>();
    private final UserService userService;
    private final MessageService messageService;
    private final GroupChatService groupChatService;
    @Autowired
    private ChatService chatService;





    @MessageMapping("/sendPrivateMessage")
    public void sendPrivateMessage(@Payload MessageDTO messageDTO) {
        System.out.println("🔹 Received message: " + messageDTO);
        String destination = "/user/" + messageDTO.getRecipientUsername() + "/queue/messages";
        User sender = userService.getUserByUsername(messageDTO.getSenderUsername())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userService.getUserByUsername(messageDTO.getRecipientUsername())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setSentAt(LocalDateTime.now());
        message.setType("text");
        messageService.saveMessage(message);

//        messageService.saveMessage(message);
        System.out.println("Sending message to: " + messageDTO.getRecipientUsername());
        messagingTemplate.convertAndSendToUser(messageDTO.getRecipientUsername(),"/queue/messages",messageDTO);
    }




    @PreAuthorize("#username==authentication.name")
    @GetMapping("/list/{username}")
    public ResponseEntity<?> getChatList(@PathVariable String username) {
//        String authenticatedUsername = getAuthenticatedUsername();
//
//        if (!Objects.equals(authenticatedUsername, username)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Map.of("status", 403, "message", "Access denied"));
//        }

        List<FriendList> chatList = chatService.getChatList(username);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Chat list retrieved successfully",
                "data", chatList
        ));
    }

    public String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

     @PreAuthorize("#userUsername == principal.userId")
    @GetMapping("/messages/{userUsername}/{userFriend}")
    public List<MessageFriendDTO> getChatMessages(@PathVariable Long  userUsername, @PathVariable Long  userFriend) {
    return chatService.getChatMessages(userUsername,userFriend);
    }

    @GetMapping("/group-messages/{groupId}")
    public List<GroupMessageDTO> getGroupMessages(@PathVariable Long groupId) {
        return groupChatService.getGroupMessages(groupId);
    }
    @MessageMapping("/sendGroupMessage")
    public void sendGroupMessage(@Payload MessageDTO messageDTO) {
        System.out.println("📢 Received group message for topic: /topic/group-" + messageDTO.getGroupId());
        User sender = userService.getUserByUsername(messageDTO.getSenderUsername())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        GroupChat group = groupChatService.getGroupById(messageDTO.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSender(sender);
        message.setGroup(group);
        message.setSentAt(LocalDateTime.now());
        messageService.saveMessage(message);
        System.out.println("Sending message to: " + group.getGroupId());

        messagingTemplate.convertAndSend("/topic/group-" + group.getGroupId(), messageDTO);
    }

    @MessageMapping("/sendPrivateImage")
    public void sendPrivateImage(@Payload MessageDTO messageDTO) {
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        System.out.println("📷 Received image message: " + messageDTO);
        String destination = "/user/" + messageDTO.getRecipientUsername() + "/queue/messages";

        User sender = userService.getUserByUsername(messageDTO.getSenderUsername())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userService.getUserByUsername(messageDTO.getRecipientUsername())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Message message = new Message();
        message.setContent(messageDTO.getContent()); // this is the image URL
        // add a type field to Message entity
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setSentAt(LocalDateTime.now());
        message.setType("image");
        messageService.saveMessage(message);

        messagingTemplate.convertAndSendToUser(
                messageDTO.getRecipientUsername(),
                "/queue/messages",
                messageDTO
        );

    }






//    @GetMapping("/user/{username}/groups")
//    public ResponseEntity<List<GroupChat>> getUserGroups(@PathVariable String username) {
//        User user = userService.getUserByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        List<GroupChat> groups = groupChatService.getGroupsByMember(user);
//        return ResponseEntity.ok(groups);
//    }
//
//    @GetMapping("/api/chat/group/{groupId}/messages")
//    public ResponseEntity<List<Message>> getGroupMessages(@PathVariable Long groupId) {
//        List<Message> messages = groupChatService.getGroupMessages(groupId);
//        return ResponseEntity.ok(messages);
//    }






//    @MessageMapping("/private")
//    public void sendPrivateMessage(Message message) {
//        if (message.getRecipient() != null) {
//            System.out.println("Recipient: " + message.getRecipient().getUsername());
//            messageService.saveMessage(message);
//            messagingTemplate.convertAndSendToUser(
//                    String.valueOf(message.getRecipient().getUserId()), "/queue/messages", message);
//        } else {
//            System.out.println("Recipient is null. Cannot send the message.");
//        }
//    }
//
//    @MessageMapping("/chat.adduser")
//    @SendTo("/topic/message")
//    public Message adduser(@Payload Message chatmessage, SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("username",chatmessage.getSender());
//        return  chatmessage;
//    }
//
//    @MessageMapping("/register")
//    public void registerUser(@Payload String username) {
//        onlineUsers.add(username);
//        messagingTemplate.convertAndSend("/topic/online-users", onlineUsers);
//    }
//
//    // 📌 Send messages in a group
//    @MessageMapping("/group-chat/{groupId}")
//    public void sendGroupMessage(@DestinationVariable Long groupId, @Payload Message message) {
//        messageService.saveMessage(message);
//        messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
//    }
//
//    @MessageMapping("/group")
//    @SendTo("/topic/group")
//    public Message sendGroupMessage(Message message) {
//        return messageService.saveMessage(message);
//    }
//    @MessageMapping("/private-chat/{recipient}") // Listen for private messages
//    public void sendPrivateMessages(Message message) {
//        System.out.println(message);
//        if (message.getRecipient() == null || message.getRecipient().getUsername() == null) {
//            System.out.println("Error: Recipient username is missing!");
//            return; // Exit to avoid errors
//        }
//
//        // Use Optional to avoid NullPointerException
//        Optional<User> recipientUserOpt = userService.getUserByUsername(message.getRecipient().getUsername());
//
//        if (recipientUserOpt.isPresent()) {
//            User recipientUser = recipientUserOpt.get(); // Extract the User object safely
//            message.setRecipient(recipientUser); // Set the recipient if found
//            messagingTemplate.convertAndSendToUser(
//                    String.valueOf(recipientUser.getUserId()), "/queue/messages", message);
//        } else {
//            System.out.println("Error: Recipient not found!");
//        }
//    }










}
