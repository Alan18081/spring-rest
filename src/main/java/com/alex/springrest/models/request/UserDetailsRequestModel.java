package com.alex.springrest.models.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UserDetailsRequestModel {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Min(value = 6)
    private String password;

    private List<AddressRequestModel> addresses = new ArrayList<>();

    public List<AddressRequestModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRequestModel> addresses) {
        this.addresses = addresses;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
