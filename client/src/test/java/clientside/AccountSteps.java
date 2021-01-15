package clientside;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class AccountSteps {

    WebTarget baseUrl;
    User customer;
    User merchant;
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    PayService dtuPay = new PayService();
    dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
    boolean successful;
    UserInfo userInfo = new UserInfo();


    public AccountSteps(){
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8383/");
    }

    @Given("a new customer with name {string} {string} and CPR {string}")
    public void aNewCustomerWithNameAndCPR(String firstName, String lastName, String cpr) throws Exception {
        System.out.println(cpr);
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setCprNumber(cpr);
    }

    @Given("the customer has a bank account")
    public void theCustomerHasABankAccount() throws Exception {
        UUID uuid = UUID.randomUUID();
        //String firstName = userInfo

        customer = new Customer(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getCprNumber(), uuid.toString(), false);
        System.out.println(customer.getCprNumber());
        dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setCprNumber(userInfo.getCprNumber());
        System.out.println(customer.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(1000));
            accountIds.add(accountId);
            System.out.println("created bank account for customer " + customer.getCprNumber());
        } catch (BankServiceException_Exception e) {
            retireAccounts();
            e.printStackTrace();
            throw new Exception();
        }
    }

    @And("is type {string} who wants to be registered in DTUPay")
    public void isType(String userType) {
//        UserInfo.userType = userType;
        dtuPay.register(customer,"customer");
        registeredUsers.add(customer);
        System.out.println("Usertype must be" + userType);
        MatcherAssert.assertThat(userType, containsString("Customer"));
    }

    @When("the customer initiates registration")
    public void theCustomerInitiatesRegistration() throws Exception {
        Response response = baseUrl.path("Account/User").request()
                .put(Entity.entity(userInfo, MediaType.APPLICATION_JSON));
        System.out.println(userInfo.getCprNumber());
        System.out.println(response.getStatus() +" " +  Response.Status.OK.getStatusCode());
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

    @Then("registration of customer is successful")
    public void registrationOfCustomerIsSuccessful() {
        Boolean response = baseUrl.path("Account/User").request()
                .get(Boolean.TYPE);
        assertTrue(response);
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
