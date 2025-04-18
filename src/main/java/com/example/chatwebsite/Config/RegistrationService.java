package com.example.chatwebsite.Config;

import com.example.chatwebsite.Repositry.RoleRepository;
import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.model.Role;
import com.example.chatwebsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public Boolean registerUser(String username, String password,String email) {
        if (userRepositry.findByUsername(username).isPresent()) {
            return false;
        }
        System.out.println("register user 🙊⛔⛔⛔🔕");

        // Create new user entity
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);  // Encoding the password
        user.setEmail(email);

        // Assign role to the user (assign "USER" role by default)
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("USER");
        roles.add(role);
        user.setRoles(roles);

        // Save the user to the database
        userRepositry.save(user);

        return true;
    }
}
