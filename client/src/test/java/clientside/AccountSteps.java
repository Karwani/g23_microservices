package clientside;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountSteps {
    WebTarget baseUrl;
    User customer;
    User merchant;
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    PayService dtuPay = new PayService();



    public AccountSteps(){
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8383/");
    }

    @Given("a new customer with id ”{string}”")
    public void aNewCustomerWithId(String userId) {
        TokenInfo.userId = userId;
        System.out.println("accountSteps given: " +TokenInfo.userId);
    }

    @Given("name {string1} {string2} and CPR {string3}")
    public void name(String firstName, String lastName, String CPR) throws Exception {
        customer = new Customer(firstName,lastName, CPR ,TokenInfo.userId,false);
        dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(CPR);
        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(100));
            accountIds.add(accountId);
            System.out.println("created bank account for customer " + customer.getCprNumber());
        } catch (BankServiceException_Exception e) {
            retireAccounts();
            e.printStackTrace();
            throw new Exception();
        }
    }

    @When("user initiates registration")
    public void userInitiatesRegistration() {
        Response response = baseUrl.path("User/" + TokenInfo.userId).request()
                .get(Response.class);
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

    @When("the type is {string}")
    public void theTypeIs() {
        dtuPay.register(customer,"customer");
        registeredUsers.add(customer);
    }

    @Then("registration of customer is successful")
    public void aNewUserIsCreated() {
        dtuPay.register(customer,"customer");
        registeredUsers.add(customer);
    }

    @After
    public void retireAccounts() throws BankServiceException_Exception {
        for (String id : accountIds){
            bank.retireAccount(id);
            System.out.println("retired account "+ id);
        }
        accountIds.clear();
        System.out.println(registeredUsers.size());
        for (User user : registeredUsers) {
            dtuPay.deregister(user);
        }
        registeredUsers.clear();
    }

}
