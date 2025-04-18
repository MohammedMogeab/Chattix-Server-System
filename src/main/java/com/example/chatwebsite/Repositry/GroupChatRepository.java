package com.example.chatwebsite.Repositry;

import com.example.chatwebsite.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

    @Query("SELECT g FROM GroupChat g JOIN GroupMember gm ON g.groupId = gm.group.groupId WHERE gm.user.userId = :userId")
    List<GroupChat> findGroupsByUserId(@Param("userId") Long userId);

}
