package com.alex.springrest.entities;

import javax.persistence.*;

@Entity(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public PasswordResetTokenEntity() {}

    public PasswordResetTokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
