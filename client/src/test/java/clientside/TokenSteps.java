package clientside;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public class TokenSteps {
    WebTarget baseUrl;
     // String customerId;

    public TokenSteps() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8181/");
    }
    @Given("Customer with userid {string}")
    public void customerWithId(String id) {
        TokenInfo.userId = id;
        System.out.println("TokenSteps tokeninfo: " +TokenInfo.userId);
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }
    @Given("the customer does not have enough active tokens")
    public void theCustomerDoesNotHaveEnoughActiveTokens() {
        Boolean response = baseUrl.path("Token/isEligible/"+TokenInfo.userId).request()
                .get(Boolean.TYPE);
        assertTrue(response);
    }

    @When("customer asks for tokens")
    public void customerAsksForTokens() {
        Response response = baseUrl.path("Token/"+TokenInfo.userId).request()
                .post( null);
        System.out.println(response.getStatus());
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

    @Then("the service has generated tokens for the user \\(return bool \\/ status code)")
    public void theServiceHasGeneratedTokensForTheUserReturnBoolStatusCode() {
        Boolean response = baseUrl.path("Token/isEligible/"+TokenInfo.userId).request()
                .get(Boolean.TYPE);
        assertFalse(response);
    }

    @Then("the user receives an active token")
    public void theUserReceivesAnActiveToken() {
        String token = baseUrl.path("Token/Active/"+TokenInfo.userId).request()
                .get(String.class);
        System.out.println(token);
        assertFalse(token.isEmpty());
    }


    @And("the customer has an unused token")
    public void theCustomerHasAnUnusedToken() {
        TokenInfo.tokenId = baseUrl.path("Token/Active/"+TokenInfo.userId).request().get(String.class);
        assertFalse(TokenInfo.tokenId.isEmpty());
    }

    @And("the token is valid")
    public void theTokenIsValid() {
        String response = baseUrl.path("Token/"+TokenInfo.tokenId).request().get(String.class);
        assertFalse(response.isEmpty());
    }

    @And("the token is no longer valid")
    public void theTokenIsNoLongerValid() {
        String response = baseUrl.path("Token/"+TokenInfo.tokenId).request().get(String.class);

        System.out.println("No longer valid" + response);
        assertTrue(response.isEmpty());
    }

    @After
    public void cleanup()
    {
        Boolean response = baseUrl.path("Token/"+TokenInfo.userId).request()
                .delete(Boolean.TYPE);
        assertTrue(response);
    }
}
