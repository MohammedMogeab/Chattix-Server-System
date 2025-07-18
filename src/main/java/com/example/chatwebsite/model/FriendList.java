package com.example.chatwebsite.model;

import com.example.chatwebsite.model.Message;
import com.example.chatwebsite.model.User;

public class FriendList {
    private Long friendId;
    private String friendUsername;
    private String lastMessage;
    private String lastMessageTime;

    public FriendList(User friend, Message lastMessage) {
        this.friendId = (long) friend.getUserId();
        this.friendUsername = friend.getUsername();
        this.lastMessage = lastMessage.getContent();
        this.lastMessageTime = lastMessage.getSentAt() != null ? lastMessage.getSentAt().toString() : null;
    }

    // Getters and Setters
    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
