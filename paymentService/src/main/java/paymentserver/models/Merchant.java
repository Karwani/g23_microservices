package paymentserver.models;

import paymentserver.models.User;

public class Merchant extends User {

    private String CVR;

    public Merchant() {}

    public Merchant(String firstName, String lastName, String cprNumber, String userId, boolean admin, String cvr) {
        super(firstName, lastName, cprNumber, userId, admin);
        CVR = cvr;
    }

    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }
}
