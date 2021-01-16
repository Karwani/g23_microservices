package paymentserver.models;

import java.util.GregorianCalendar;

public class Transaction {


    private String description;
    private GregorianCalendar time;
    private int amount;
    private String creditor;
    private String debtor;
    private String CVR;

    public Transaction(String description, GregorianCalendar time, int amount, String creditor, String debtor, String cvr) {
        this.description = description;
        this.time = time;
        this.amount = amount;
        this.creditor = creditor;
        this.debtor = debtor;
        CVR = cvr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getTime() {
        return time;
    }

    public void setTime(GregorianCalendar time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public String getCVR() {
        return CVR;
    }

    public void setCVR(String CVR) {
        this.CVR = CVR;
    }
}
