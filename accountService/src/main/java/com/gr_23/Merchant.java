package com.gr_23;

public class Merchant extends User{
    private String CVR;

    public Merchant(){}

    public Merchant(String firstName, String lastName, String cprNumber, String userId, boolean admin) {
        super(firstName, lastName, cprNumber, userId, admin);
    }
    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }
}
