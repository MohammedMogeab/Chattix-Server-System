package com.example.chatwebsite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
 // Foreign key to 'users'
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")  // Foreign key to 'roles'
    private Role role;

    // Getters and Setters
}
