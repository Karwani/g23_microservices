package clientside;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.client.WebTarget;

public class AccountSteps {
    WebTarget baseURL;

    public AccountSteps(){
        
    }

    @Given("a new customer with id ”{string}”")
    public void aNewCustomerWithId(String userId) {

        throw new io.cucumber.java.PendingException();
    }

    @Given("name {string1} {string2}")
    public void name() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("user initiates registration")
    public void userInitiatesRegistration() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the type is {string}")
    public void theTypeIs() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("a new user is created")
    public void aNewUserIsCreated() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
