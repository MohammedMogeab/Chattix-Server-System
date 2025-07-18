package com.example.chatwebsite.Config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/ws/test")
    public String test() {
        return "WebSocket works";
    }
}
