package clientside;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentReportSteps {
    PayService dtuPay = new PayService();
    List<Object> report;
    String userId;
    @When("the customer requests a report of his payments")
    public void theCustomerRequestsAReportOfHisPayments() {
        userId = "1";
        report = dtuPay.getReport(userId);
    }

    @Then("the server returns a valid list of size {int}")
    public void theServerReturnsAValidListOfSize(Integer size) {
        assertEquals(size,report.size());
        for (Object p:
             report) {
            HashMap<String,String> payment = (HashMap<String,String>) p;
            assertTrue(!payment.get("description").isEmpty());
            assertTrue(!payment.get("customerId").isEmpty());
            assertTrue(!payment.get("merchantId").isEmpty());
            assertTrue(payment!=null);
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
        dtuPay.deletePayments(userId);
    }
}
