package com.example.chatwebsite.model;

import com.example.chatwebsite.Config.UserDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@ToString
@Entity
@Table(name = "messages",indexes = @Index(name = "idx_sentAt",columnList = "sentAt"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonDeserialize(using = UserDeserializer.class)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @JsonDeserialize(using = UserDeserializer.class)
    private User recipient; // Nullable for group messages

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupChat group; // Nullable for private messages

    @Column(nullable = false)
    private String content;

    private String Type;

    private LocalDateTime sentAt = LocalDateTime.now();
}
