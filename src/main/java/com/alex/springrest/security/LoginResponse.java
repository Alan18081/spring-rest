package com.alex.springrest.security;

import com.alex.springrest.models.response.UserRest;

public class LoginResponse {
    private String token;
    private UserRest userInfo;

    public LoginResponse(String token, UserRest userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRest getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserRest userInfo) {
        this.userInfo = userInfo;
    }
}
