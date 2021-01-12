package paymentserver;

import java.util.GregorianCalendar;

public class Receipt {

    private String tokenId;
    private int amount;
    private String requestId;
    private String customerId;
    private String merchantId;
    private GregorianCalendar time;


    public Receipt(String tokenId, int amount, String customerId, String merchantId, GregorianCalendar time) {
        this.tokenId = tokenId;
        this.amount = amount;
        generateId();
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.time = time;
    }

    private void generateId(){
    // TODO
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public GregorianCalendar getTime() {
        return time;
    }

    public void setTime(GregorianCalendar time) {
        this.time = time;
    }
}
