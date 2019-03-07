package com.paraett.usersservice.model.dtos;

public class AccountActivationUserDto {
    String email;
    String activationCode;
    String password;

    public String getEmail() {
        return email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public String getPassword() {
        return password;
    }
}
