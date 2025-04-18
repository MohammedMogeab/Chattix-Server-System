package com.example.chatwebsite.Repositry;


import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
                         List<Message>findByRecipient_UserId(Long userId);
                         List<Message>findByGroup_GroupId(Long groupId);

    List<Message> findByGroup_GroupIdOrderBySentAt(Long groupId);


    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId AND m.recipient.id = :chatUserId) " +
            "OR (m.sender.id = :chatUserId AND m.recipient.id = :userId) ORDER BY m.sentAt ASC")
    List<Message> findChatMessages(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.userId = :userId AND m.recipient.userId = :chatUserId) " +
            "OR (m.sender.userId = :chatUserId AND m.recipient.userId = :userId) " +
            "ORDER BY m.sentAt DESC LIMIT 1")
    Message findLastMessage(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId);


    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.userId = (SELECT u.userId FROM User u WHERE u.username = :username) " +
            "AND m.recipient.userId = :chatUserId) " +
            "OR (m.sender.userId = :chatUserId AND m.recipient.userId = (SELECT u.userId FROM User u WHERE u.username = :username)) " +
            "ORDER BY m.sentAt DESC")
    Message findLastMessageByUsername(@Param("username") String username, @Param("chatUserId") Long chatUserId);


    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.userId = :userId AND m.recipient.userId = :chatUserId) " +
            "OR (m.sender.userId = :chatUserId AND m.recipient.userId = :userId) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findMessagesBetweenUsers(Long userId, Long chatUserId);

}



