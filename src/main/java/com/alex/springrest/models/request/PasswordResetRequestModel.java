package com.alex.springrest.models.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class PasswordResetRequestModel {

    @NotNull
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
