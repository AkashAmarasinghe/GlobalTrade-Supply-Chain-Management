package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=50)
    private String username;
    @Column(name="password_hash", nullable=false, length=255)
    private String passwordHash;
    @Column(name="full_name", nullable=false, length=100)
    private String fullName;
    @Column(length=120)
    private String email;
    @Column(nullable=false)
    private boolean active = true;
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="role_id", nullable=false)
    private Role role;
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;
    @PrePersist public void prePersist(){ if(createdAt==null) createdAt=LocalDateTime.now(); }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getUsername(){return username;} public void setUsername(String username){this.username=username;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
    public String getFullName(){return fullName;} public void setFullName(String fullName){this.fullName=fullName;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
    public Role getRole(){return role;} public void setRole(Role role){this.role=role;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
