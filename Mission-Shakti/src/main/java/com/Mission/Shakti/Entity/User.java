package com.Mission.Shakti.Entity;

import com.Mission.Shakti.Enum.ApprovalStatus;
import com.Mission.Shakti.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private LocalDateTime createdAt;




    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}

