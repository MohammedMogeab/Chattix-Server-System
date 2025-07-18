package com.example.chatwebsite.Controller;

import com.example.chatwebsite.Service.UserService;
import com.example.chatwebsite.model.GroupChat;
import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import com.example.chatwebsite.Service.GroupChatService;
import com.example.chatwebsite.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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


//    @PostMapping("/private")
//        public ResponseEntity<Map<String,Object>>sendPrivateMessage(@RequestParam Long senderId,@RequestParam Long recipientId,@RequestParam String content){
//
//        Optional<User> sender = userService.getUserById(senderId);
//        Optional<User> recipient = userService.getUserById(recipientId);
//        Map<String , Object>response = new HashMap<>();
//        if(sender.isPresent() && recipient.isPresent()){
//            Message message = messageService.sendPrivateMessage(sender.get(), recipient.get(), content);
//            MessageFriendDTO messageDTO= new MessageFriendDTO(message);
//            response.put("message",messageDTO);
//            response.put("status","success");
//            response.put("code",200);
//        }
//        return ResponseEntity.ok(response);
//        }



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

    @GetMapping("/privateMessage/{userId}")
    public ResponseEntity<List<Message>> getPrivateMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getPrivateMessages(userId));
    }



    @GetMapping("/private/{username}")
    public ResponseEntity<Map<String, Object>> getMessages(@PathVariable String username) {
        // Fetch the user by username
        Optional<User> userOpt = userService.getUserByUsername(username);

        // Handle the case when the user is not found
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "User not found",
                            "status", "failure",
                            "code", 404
                    ));
        }

        User user = userOpt.get();  // Get the User object from Optional
        int userId = user.getUserId(); // Extract userId

        // Debug log
        System.out.println("User found in private API: " + username);

        // Fetch private messages for the user
        List<Message> messages = messageService.getPrivateMessages((long) userId);

        // Convert Message entities to DTOs
        List<MessageDTO> messageDTOS = messages.stream()
                .map(MessageDTO::new)
                .toList();

        // Build response
        Map<String, Object> response = Map.of(
                "messages", messageDTOS,
                "status", "success",
                "code", 200,
                "total", messageDTOS.size(),
                "page", 1,
                "per_page", messageDTOS.size()
        );

        // Return the response with HTTP 200 status
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Message>> getGroupMessages(@PathVariable Long groupId) {
        return ResponseEntity.ok(messageService.getGroupMessages(groupId));
    }
}
