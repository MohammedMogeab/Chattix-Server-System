package com.example.chatwebsite.Service;

import com.example.chatwebsite.Repositry.GroupMemberRepository;
import com.example.chatwebsite.model.GroupChat;
import com.example.chatwebsite.model.GroupMember;
import com.example.chatwebsite.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public void addUserToGroup(User user, GroupChat group) {
        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setGroup(group);
        groupMemberRepository.save(groupMember);
    }

    public List<GroupMember> getMembersByGroup(Long groupId) {
        return groupMemberRepository.findByGroup_GroupId(groupId);
    }
}
