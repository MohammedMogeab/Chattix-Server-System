package com.example.chatwebsite.Config;

import com.example.chatwebsite.Repositry.MessageRepository;
import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChatService {
    @Autowired
    private UserRepositry userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<ChatDTO> getChatList(String  userUsername) {


        Long userId = userRepository.findUserIdByUsername(userUsername);

        List<User> chatUsers = userRepository.findChatUsers(userId);
        List<ChatDTO> chatList = new ArrayList<>();

        for (User chatUser : chatUsers) {
            Message lastMessage = messageRepository.findLastMessage(userId,(long) chatUser.getUserId());

            // Handle case where there is no last message (shouldn't happen, but just in case)
            if (lastMessage == null) {
                lastMessage = new Message();
                lastMessage.setContent("No messages yet.");
                lastMessage.setSentAt(null);
            }

            chatList.add(new ChatDTO(chatUser, lastMessage));
        }

        return chatList;
    }




    public List<Message> getChatMessages(String userUsername, Long chatUserId) {
        Long userId = userRepository.findUserIdByUsername(userUsername);
        System.out.println(userId);
        return messageRepository.findMessagesBetweenUsers(userId, chatUserId);
    }


}
