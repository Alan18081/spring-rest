package com.alex.springrest.models.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SetNewPasswordRequestModel {

    @NotNull
    private String token;

    @NotNull
    @Size(min = 6)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
