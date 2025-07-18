package com.example.chatwebsite.Security.Service;

import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.model.User;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final UserRepositry userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws java.io.IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();




        Optional<User> userOptional = userRepository.findByEmail(email);
        int userId =userOptional.get().getUserId();
        System.out.println(email);


        if (userOptional.isEmpty()) {
            // Redirect to registration page on your frontend
            response.sendRedirect("http://localhost:3000/register");
            return;
        }

        String token = jwtService.generateTokern(email,userId);
        String refreshToken = jwtService.generateRefreshToken(email,userId);


        // Send as HttpOnly Cookies
        Cookie accessCookie = new Cookie("accessToken", token);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(900); // 15 mins

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        // 🔁 Redirect to frontend (without token in query string)
        response.sendRedirect("http://localhost:3000");
        // Option 1: Redirect to frontend with tokens in query (NOT secure in production!)

    }
}
