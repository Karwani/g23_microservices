package clientside;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public class TokenSteps {
    WebTarget baseUrl;
    String customerId;
    public TokenSteps() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8181/");
    }
    @Given("Customer with userid {string}")
    public void customerWithId(String id) {
        customerId = id;
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }
    @Given("the customer does not have enough active tokens")
    public void theCustomerDoesNotHaveEnoughActiveTokens() {
        Boolean response = baseUrl.path("Token/isEligible/"+customerId).request()
                .get(Boolean.TYPE);
        assertTrue(response);
    }

    @When("customer asks for tokens")
    public void customerAsksForTokens() {
        Response response = baseUrl.path("Token/"+customerId).request()
                .post( null);
        System.out.println(response.getStatus());
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

    @Then("the service has generated tokens for the user \\(return bool \\/ status code)")
    public void theServiceHasGeneratedTokensForTheUserReturnBoolStatusCode() {
        Boolean response = baseUrl.path("Token/isEligible/"+customerId).request()
                .get(Boolean.TYPE);
        assertFalse(response);
    }

    @Then("the user receives an active token")
    public void theUserReceivesAnActiveToken() {
        String token = baseUrl.path("Token/Active/"+customerId).request()
                .get(String.class);
        System.out.println(token);
        assertFalse(token.isEmpty());
    }
    @After
    public void cleanup()
    {
        Boolean response = baseUrl.path("Token/"+customerId).request()
                .delete(Boolean.TYPE);
        assertTrue(response);
    }

}
