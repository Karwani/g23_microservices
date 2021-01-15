package com.gr_23;

import dtu.ws.fastmoney.User;

public class UserInfo {
    private String cprNumber;
    private String firstName;
    private String lastName;
    private String userType;

    public UserInfo() {}

    public UserInfo(String cprNumber, String firstName, String lastName) {
        this.cprNumber = cprNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
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

    public dtu.ws.fastmoney.User asUser() {
        dtu.ws.fastmoney.User user = new User();
        user.setCprNumber(cprNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}