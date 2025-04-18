package com.example.chatwebsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GroupMemberId.class)
public class GroupMember {
    @Id
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupChat group;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime joinedAt = LocalDateTime.now();
}
