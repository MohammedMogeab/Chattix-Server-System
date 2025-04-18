package com.example.chatwebsite.Service;


import com.example.chatwebsite.Controller.GroupMessageDTO;
import com.example.chatwebsite.Repositry.GroupChatRepository;
import com.example.chatwebsite.Repositry.MessageRepository;
import com.example.chatwebsite.model.GroupChat;
import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final MessageRepository messageRepository;
    public GroupChat createGroupChat(String groupName, User createdBy) {
        GroupChat group = new GroupChat();
        group.setGroupName(groupName);
        group.setCreatedBy(createdBy);
        return groupChatRepository.save(group);
    }

    public List<GroupChat> getAllGroups() {
        return groupChatRepository.findAll();
    }

    public Optional<GroupChat> getGroupById(Long groupId) {
        return groupChatRepository.findById(groupId);
    }

    public List<GroupChat> getUserGroups(Long userId) {
        return groupChatRepository.findGroupsByUserId(userId);
    }

    public List<GroupMessageDTO> getGroupMessages(Long groupId) {
        List<Message> messages = messageRepository.findByGroup_GroupIdOrderBySentAt(groupId);

        return messages.stream().map(msg -> new GroupMessageDTO(
                msg.getGroup().getGroupName(),
                msg.getSender().getUsername(),
                msg.getContent(),
                msg.getSentAt()
        )).collect(Collectors.toList());
    }
}


