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
    TokenInfo tokenInfo;

    public TokenSteps(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8181/");
    }
    @Given("Customer with userid {string}")
    public void customerWithId(String id) {
        tokenInfo.userId = id;
    }
    @Given("the customer does not have enough active tokens")
    public void theCustomerDoesNotHaveEnoughActiveTokens() {
        Boolean response = baseUrl.path("Token/isEligible/"+tokenInfo.userId).request()
                .get(Boolean.TYPE);
        assertTrue(response);
    }

    @When("customer asks for tokens")
    public void customerAsksForTokens() {
        Response response = baseUrl.path("Token/"+tokenInfo.userId).request()
                .post( null);
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

    @Then("the service has generated tokens for the user \\(return bool \\/ status code)")
    public void theServiceHasGeneratedTokensForTheUserReturnBoolStatusCode() {
        Boolean response = baseUrl.path("Token/isEligible/"+tokenInfo.userId).request()
                .get(Boolean.TYPE);
        assertFalse(response);
    }

    @Then("the user receives an active token")
    public void theUserReceivesAnActiveToken() {
        String token = baseUrl.path("Token/Active/"+tokenInfo.userId).request()
                .get(String.class);
        assertFalse(token.isEmpty());
    }


    @And("the customer has an unused token")
    public void theCustomerHasAnUnusedToken() {
        tokenInfo.tokenId = baseUrl.path("Token/Active/"+tokenInfo.userId).request().get(String.class);
        assertFalse(tokenInfo.tokenId.isEmpty());
    }

    @And("the token is valid")
    public void theTokenIsValid() {
        String response = baseUrl.path("Token/"+tokenInfo.tokenId).request().get(String.class);
        assertFalse(response.isEmpty());
    }

    @And("the token is no longer valid")
    public void theTokenIsNoLongerValid() {
        String response = baseUrl.path("Token/"+tokenInfo.tokenId).request().get(String.class);

        assertTrue(response.isEmpty());
    }

    @Given("the customer has an used token")
    public void theCustomerHasAnUsedToken()
    {
        theCustomerHasAnUnusedToken();
        Response response = baseUrl.path("Token/ConsumedToken/"+tokenInfo.tokenId).request().post(null);
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }
    @After
    public void cleanup()
    {
        Response response = baseUrl.path("Token/"+tokenInfo.userId).request()
                .delete();
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

}
