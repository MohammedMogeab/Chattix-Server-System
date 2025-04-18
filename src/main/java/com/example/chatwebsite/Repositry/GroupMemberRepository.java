package com.example.chatwebsite.Repositry;

import com.example.chatwebsite.model.GroupChat;
import com.example.chatwebsite.model.GroupMember;
import com.example.chatwebsite.model.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    List<GroupMember> findByGroup_GroupId(Long groupId);
    List<GroupMember> findByUser_UserId(Long userId);

}
