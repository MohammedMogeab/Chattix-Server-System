package com.example.chatwebsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
 @Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(nullable = false , unique = true,length = 50,name = "username")
    private  String username;
//    @JsonIgnore // Hides password in API response
    @Column(nullable = false,unique = true,length = 100,name = "email")
    private String email;
//    @JsonIgnore
    @Column(nullable = false,name = "password")
    private String password;


    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false,name = "isOnline")
    private Boolean isOnline=false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


}
