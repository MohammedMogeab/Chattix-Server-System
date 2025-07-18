package com.example.chatwebsite.Security;
import com.example.chatwebsite.Security.Service.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  private  UserDetailsService userDetailsService;

  @Autowired
  private JwtFilter jwtFilter;
  @Autowired
  private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
      return http.
              csrf(AbstractHttpConfigurer::disable)
//              .csrf(csrf -> csrf
//                      .ignoringRequestMatchers("/ws/**") // Disable CSRF for WebSocket handshake
//              )
              .authorizeHttpRequests(request ->

                      request.requestMatchers("/login", "/register",
                                      "/oauth2/**", "/favicon.ico","/auth/refresh-token","/api/upload").permitAll()
                              .requestMatchers("/oauth2/success").permitAll()
                              .requestMatchers("/ws").permitAll()
                              .requestMatchers("/ws-native").permitAll()
                              .requestMatchers("/ws/**").permitAll()
                              .requestMatchers(
                                      "/v3/api-docs/**",
                                      "/swagger-ui/**",
                                      "/swagger-ui.html",
                                      "/swagger-resources/**",
                                      "/webjars/**",
                                      "/favicon.ico",
                                      "/error"
                              ).permitAll()                              .anyRequest().authenticated()

              )
              .oauth2Login(oauth2 -> oauth2
                      .successHandler(oAuth2LoginSuccessHandler) // <--- use it here!

              )
              .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

//        .formLogin(
//                Customizer.withDefaults()
//        )
//        .httpBasic(Customizer.withDefaults())
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .exceptionHandling(customizer -> customizer
                      .authenticationEntryPoint((request, response, authException) -> {
                          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                          response.setContentType("application/json");
                          response.getWriter().write("{\"error\": \"You must log in first\"}");
                      })
                      .accessDeniedHandler((request, response, accessDeniedException) -> {
                          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                          response.setContentType("application/json");
                          response.getWriter().write("{\"error\": \"Access Denied\"}");
                      })
              )
              .build();

    }


    @Bean
   public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(5));
        provider.setUserDetailsService(userDetailsService);
        return  provider;
    }



   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();


   }
    @Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Redirect manually after authentication, while still inside the SecurityContext
            response.sendRedirect("http://localhost:8080/oauth2/success");
        };
    }


}
