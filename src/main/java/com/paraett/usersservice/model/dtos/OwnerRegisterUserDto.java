package com.paraett.usersservice.model.dtos;

public class OwnerRegisterUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Integer norm;
    private Integer freeDaysTotal;

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

    public OwnerRegisterUserDto() {
    }

}
