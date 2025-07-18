package com.example.chatwebsite.Config;

import com.example.chatwebsite.Repositry.MessageRepository;
import com.example.chatwebsite.Repositry.UserRepositry;
import com.example.chatwebsite.model.FriendList;
import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.MessageFriendDTO;
import com.example.chatwebsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private UserRepositry userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<FriendList> getChatList(String  userUsername) {


        Long userId = userRepository.findUserIdByUsername(userUsername);

        List<User> chatUsers = userRepository.findChatUsers(userId);
        List<FriendList> chatList = new ArrayList<>();

        for (User chatUser : chatUsers) {
            Message lastMessage = messageRepository.findLastMessage(userId,(long) chatUser.getUserId());

            // Handle case where there is no last message (shouldn't happen, but just in case)
            if (lastMessage == null) {
                lastMessage = new Message();
                lastMessage.setContent("No messages yet.");
                lastMessage.setSentAt(null);
            }

            chatList.add(new FriendList(chatUser, lastMessage));
        }

        return chatList;
    }




    public List<MessageFriendDTO> getChatMessages(Long userUsername, Long chatUserId) {

        List<Message> messages = messageRepository.findMessagesBetweenUsers(userUsername, chatUserId);


        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convert Message to MessageFriendDTO
    private MessageFriendDTO convertToDTO(Message message) {
        MessageFriendDTO dto = new MessageFriendDTO();
        dto.setMessageId(String.valueOf(message.getMessageId()));
        dto.setSenderId(String.valueOf(message.getSender().getUserId()));
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setRecipientId(String.valueOf(message.getRecipient().getUserId()));
        dto.setRecipientUsername(message.getRecipient().getUsername());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        dto.setType(message.getType());
        return dto;
    }

}
