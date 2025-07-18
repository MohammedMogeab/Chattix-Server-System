package com.example.chatwebsite.Security.Service;


import com.example.chatwebsite.Repositry.RoleRepository;
import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.model.Role;
import com.example.chatwebsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("securityUserService")
public class UserService {
    @Autowired
    private UserRepositry userRepo;
    @Autowired
    private AuthenticationManager Autho;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JWTService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder(5);
    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsOnline(false);

        // If no roles provided, assign default "USER" role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByRoleName("USER");
            System.out.println(defaultRole.getRoleName());
            user.setRoles(Set.of(defaultRole));
        } else {
            // Replace transient roles with managed ones from DB
            Set<Role> managedRoles = new HashSet<>();
            for (Role r : user.getRoles()) {
                Role roleFromDb = roleRepository.findByRoleName(r.getRoleName());
                managedRoles.add(roleFromDb);
            }
            user.setRoles(managedRoles);
        }

        return userRepo.save(user);
    }


    public AuthResponse verify(User users) {
        Authentication authentication = Autho.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword())
        );

        if (authentication.isAuthenticated()) {
            Optional<User> realUser = userRepo.findByUsername(users.getUsername());

            System.out.println("verified: " +    realUser.get().getUserId()); // This will be correct now

            String accessToken = jwtService.generateTokern(users.getUsername(),realUser.get().getUserId());
            String refreshToken = jwtService.generateRefreshToken(users.getUsername(), realUser.get().getUserId());
            return new AuthResponse(accessToken, refreshToken);
        }

        throw new RuntimeException("Invalid credentials");
    }






}




