package com.example.chatwebsite.Service;


import com.example.chatwebsite.Repositry.MessageRepository;
import com.example.chatwebsite.model.GroupChat;

import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message sendPrivateMessage(User sender, User recipient, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public Message sendGroupMessage(User sender, GroupChat group, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setGroup(group);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getPrivateMessages(Long userId) {
        return messageRepository.findByRecipient_UserId(userId);
    }

    public List<Message> getGroupMessages(Long groupId) {
        return messageRepository.findByGroup_GroupId(groupId);
    }
    public Message saveMessage(Message message) {
        message.setSentAt(LocalDateTime.now());  // Set timestamp before saving
        return messageRepository.save(message);
    }
}
