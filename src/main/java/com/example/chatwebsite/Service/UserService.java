package com.example.chatwebsite.Service;

import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepositry userRepository;
    public User createUser(User user) {
        return userRepository.save(user);
    }
    public void registerUser(User user) {
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Long getuseridbyusername(String username){
        return userRepository.findUserIdByUsername(username);

    }

    public List<User> getOnlineUsers() {
        return userRepository.findOnlineUsers();
    }

    public void setUserOnline(Long userId, boolean isOnline) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsOnline(isOnline);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
