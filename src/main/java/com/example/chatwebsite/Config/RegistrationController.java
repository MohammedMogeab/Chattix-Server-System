package com.example.chatwebsite.Config;

import com.example.chatwebsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("user", new User());
//        return "register";
//    }

//    @GetMapping("/register")
//    public String showRegistrationForm() {
//        return "register";  // Return the registration HTML page
//    }


    //    @PostMapping("/register")
//    public String register(@ModelAttribute("user") User user) {
//        userService.registerUser(user);
//        return "redirect:/login?registered=true";
//    }
//@PostMapping("/register")
//public String registerUser(@RequestParam String username,
//                           @RequestParam String password,
//                           @RequestParam String email,
//                           Model model) {
//    Boolean result = registrationService.registerUser(username, password,email);
//
//    if (result) {
//
//        System.out.println("redirect to /login in registercontroller.java 🙊🚫🚫📵");
//        // Registration successful, redirect to login
//        return "redirect:/login";
//    }
//
//    // Add error message to the model if registration fails
//    model.addAttribute("error", "error in registeration ");
//    return "register";  // Show error on registration page
//}
}

