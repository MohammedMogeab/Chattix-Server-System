package com.example.chatwebsite.Config;

import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.Security.Service.AuthResponse;
import com.example.chatwebsite.Security.Service.JWTService;
import com.example.chatwebsite.Security.Service.UserService;
import com.example.chatwebsite.model.User;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private JWTService jwtService;

//    @GetMapping("/login")
//    public String login(Principal principal, Model model, @RequestParam(value = "error", required = false) String error,
//                        @RequestParam(value = "logout", required = false) String logout) {
//
//        if (error != null) {
//            model.addAttribute("error", "Invalid username or password!");
//        }
//        if (principal != null) {
//            return "redirect:/home";
//        }
//
//        if (logout != null) {
//            model.addAttribute("message", "You have been logged out successfully.");
//        }
//
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String loginUser(@RequestParam String username, HttpSession session) {
////        System.out.println("Username stored in session: " + username); // This should print
////        session.setAttribute("username", username);
////        return "redirect:/chat";
//        return "login page here ";
//    }
//
//    @GetMapping("/chat")
//    public String welcome(HttpSession session, Principal principal, Model model) {
////        // Store username in session if not already
////        if (session.getAttribute("username") == null && principal != null) {
////            session.setAttribute("username", principal.getName());
////
////        }
////
////
////        return "chat.html"; // welcome.html
//
//        return  " chat page here";
//    }
//
//
//    @GetMapping("/logout")
//    public String logout(RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("message", "You have successfully logged out.");
//        return "redirect:/login?logout";
//    }

    @PostMapping("/login")
    public  AuthResponse login(@RequestBody User users){
        System.out.println(users);
        return userService.verify(users);
    }
    @PostMapping("/register")
    public User register(@RequestBody User users){
        System.out.println(users.getUsername());
        System.out.println(users.getEmail());
        return userService.register(users);
    }

    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadImage(@RequestBody byte[] fileBytes,
                                              @RequestHeader("Authorization") String token) throws IOException, java.io.IOException {
        // Validate token manually if needed
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));
        String filename = UUID.randomUUID() + ".jpg";
        Path path = Paths.get("uploads/" + filename);
        System.out.println("Saving file to: " + path.toAbsolutePath());

        Files.write(path, fileBytes);
        String imageUrl = "http://localhost:8087/uploads/" + filename;
        System.out.println("image url: " + imageUrl );
        return ResponseEntity.ok(imageUrl);
    }


}
