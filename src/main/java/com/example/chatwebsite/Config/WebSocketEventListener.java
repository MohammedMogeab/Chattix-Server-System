package com.example.chatwebsite.Config;

import com.example.chatwebsite.Service.UserService;
//import com.example.chatwebsite.Security.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    
    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // Extract user ID and mark as online
        Long userId = getUserIdFromEvent(event);
        userService.setUserOnline(userId, true);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Extract user ID and mark as offline
        Long userId = getUserIdFromEvent(event);
        userService.setUserOnline(userId, false);
    }

    private Long getUserIdFromEvent(ApplicationEvent event) {
        // Extract user ID from event (implement session tracking)
        return 1L; // Placeholder (replace with real user session handling)
    }
}
