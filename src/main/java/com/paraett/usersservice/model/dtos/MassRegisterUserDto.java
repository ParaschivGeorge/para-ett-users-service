package com.paraett.usersservice.model.dtos;

import com.paraett.usersservice.model.enums.UserType;

public class MassRegisterUserDto {
    private String email;
    private String firstName;
    private String lastName;
    private UserType type;
    private String managerEmail;
    private Integer norm;
    private Integer freeDaysTotal;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public Integer getNorm() {
        return norm;
    }

    public void setNorm(Integer norm) {
        this.norm = norm;
    }

    public Integer getFreeDaysTotal() {
        return freeDaysTotal;
    }

    public void setFreeDaysTotal(Integer freeDaysTotal) {
        this.freeDaysTotal = freeDaysTotal;
    }
}
