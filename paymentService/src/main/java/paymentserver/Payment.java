package paymentserver;

import java.math.BigDecimal;

public class Payment {

    private BigDecimal amount;
    private String customerCpr;
    private String merchantCpr;

    public Payment() {
        this.amount = new BigDecimal(0);
        this.customerCpr = "";
        this.merchantCpr = "";
    }

    public Payment(String customerCpr, String merchantCpr, BigDecimal amount) {
        this.amount = amount;
        this.customerCpr = customerCpr;
        this.merchantCpr = merchantCpr;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerCpr() {
        return customerCpr;
    }

    public void setCustomerCpr(String customerCpr) {
        this.customerCpr = customerCpr;
    }

    public String getMerchantCpr() {
        return merchantCpr;
    }

    public void setMerchantCpr(String merchantCpr) {
        this.merchantCpr = merchantCpr;
    }
}
