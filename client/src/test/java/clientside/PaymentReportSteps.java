package clientside;

import clientside.apis.MerchantAPI;
import clientside.data_access.PayService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentReportSteps {
    TokenInfo tokenInfo = null;
    PayService dtuPay = new PayService();
    List<Object> report;
    public PaymentReportSteps(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    @When("the customer requests a report of his payments")
    public void theCustomerRequestsAReportOfHisPayments() {
        report = dtuPay.getReport(tokenInfo.userId);
    }

    @Then("the server returns a valid list of size {int}")
    public void theServerReturnsAValidListOfSize(Integer size) {
        assertEquals(size,report.size());
        for (Object p:
             report) {
            HashMap<String,String> payment = (HashMap<String,String>) p;
            assertTrue(payment!=null);
            assertTrue(!payment.get("description").isEmpty());
            assertTrue(!payment.get("customerId").isEmpty());

            assertEquals(payment.get("customerId"), tokenInfo.userId);
            assertTrue(!payment.get("merchantId").isEmpty());
        }
    }
    @Then("the server returns null")
    public void theServerReturnsNull() {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(null,report);
    }


    @After
    public void cleanup()
    {
        dtuPay.deletePayments(tokenInfo.userId);
    }
}
