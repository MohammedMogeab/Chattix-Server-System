package com.example.chatwebsite.Config;

import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.Security.Service.AuthResponse;
import com.example.chatwebsite.Security.Service.JWTService;
import com.example.chatwebsite.model.RefreshTokenRequest;
import com.example.chatwebsite.model.User;
import com.nimbusds.oauth2.sdk.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

//@RestController
//public class AuthController {
//    @Autowired
//    private UserRepositry userRepositry;
//
//    @Autowired
//    private JWTService jwtService;
////
////    @GetMapping("/oauth2/success")
////    public ResponseEntity<?> getSuccessPage(@AuthenticationPrincipal OidcUser oidcUser) {
////        if (oidcUser == null) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication token.");
////        }
////
////        String email = oidcUser.getEmail();
////        System.out.println("User email: " + email);
////
////        // Check if the user exists in the database
////        Optional<User> optionalUser = userRepositry.findByEmail(email);
////
////        if (optionalUser.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not found");
////        }
////
////        // Generate tokens
////        String token = jwtService.generateTokern(email);
////        String refreshToken = jwtService.generateRefreshToken(email);
////
////        return ResponseEntity.ok(new AuthResponse(token, refreshToken));
////
////    }
//
//
//}

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            String username = jwtService.extractUserName(refreshToken);
            int userId= jwtService.extractUserId(refreshToken);

            // Optionally validate token expiration
            if (jwtService.isTokenExpired(refreshToken)) {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                        .body(Map.of("error", "Refresh token expired"));
            }
            System.out.println("the userId in AuthController is: "+userId);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtService.generateTokern(username,userId);

            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }
    }
}
