//package com.example.chatwebsite.Config;
//
//import com.example.chatwebsite.Security.Service.AuthResponse;
//import com.example.chatwebsite.Security.Service.JWTService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//public class OAuthController {
//
//    @Autowired
//    private JWTService jwtService;
//
//    @GetMapping("/oauth2/redirect")
//    public ResponseEntity<?> handleGoogleLogin(OAuth2AuthenticationToken authToken) {
//        if (authToken == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 Authentication failed or user not authenticated.");
//        }
//        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
//        String email = (String) attributes.get("email");
//
//        // Check if user exists or register them if needed
//
//        String jwt = jwtService.generateTokern(email);
//        String refresh = jwtService.generateRefreshToken(email);
//
//        return ResponseEntity.ok(new AuthResponse(jwt, refresh));
//    }
//}
