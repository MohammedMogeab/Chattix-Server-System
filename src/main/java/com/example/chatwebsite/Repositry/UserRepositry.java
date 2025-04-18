package com.example.chatwebsite.Repositry;

import com.example.chatwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositry extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);


    Optional<User> findByEmail(String email);


    @Query("SELECT u.userId FROM User u WHERE u.username = :username")
    Long findUserIdByUsername(@Param("username") String username);




    @Query("SELECT u FROM User u WHERE u.isOnline = true")
    List<User> findOnlineUsers();

    @Query("SELECT DISTINCT u FROM User u WHERE u.userId != :userId AND u.userId IN (" +
            "SELECT m.sender.userId FROM Message m WHERE m.recipient.userId = :userId " +
            "UNION " +
            "SELECT m.recipient.userId FROM Message m WHERE m.sender.userId = :userId)")
    List<User> findChatUsers(@Param("userId") Long userId);


    @Query("SELECT DISTINCT u FROM User u WHERE u.username != :username AND u.userId IN (" +
            "SELECT m.sender.userId FROM Message m WHERE m.recipient.username = :username " +
            "UNION " +
            "SELECT m.recipient.userId FROM Message m WHERE m.sender.username = :username)")
    List<User> findChatUsersByUsername(@Param("username") String username);


}
