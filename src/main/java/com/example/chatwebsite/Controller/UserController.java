package com.example.chatwebsite.Controller;

import com.example.chatwebsite.Service.MessageService;
import com.example.chatwebsite.Service.UserService;
import com.example.chatwebsite.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    private final MessageService  messageService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//    @GetMapping("/{username}/chats")
//    public ResponseEntity<List<User>> getChatUsers(@PathVariable String username) {
//        List<User> chatUsers = messageService.getUsersChattedWith(username);
//        return ResponseEntity.ok(chatUsers);
//    }
    @GetMapping("/online")
    public ResponseEntity<List<User>> getOnlineUsers() {
        List<User> users = userService.getOnlineUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/setOnline/{userId}")
    public ResponseEntity<Void> setUserOnline(@PathVariable Long userId, @RequestParam boolean isOnline) {
        userService.setUserOnline(userId, isOnline);
        return ResponseEntity.ok().build();
    }
}
