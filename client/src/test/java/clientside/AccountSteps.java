package clientside;

import clientside.apis.CustomerAPI;
import clientside.data_access.AccountService;
import clientside.data_access.PayService;
import clientside.models.Customer;
import clientside.models.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class AccountSteps {

    User customer;
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    CustomerAPI customerAPI;
    AccountService accountService = new AccountService();
    dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
    boolean successful;


    public AccountSteps(){
        customerAPI = new CustomerAPI(accountService);
    }

    @Given("a new customer with name {string} {string} and CPR {string}")
    public void aNewCustomerWithNameAndCPR(String firstName, String lastName, String cpr) {
        customer = new Customer(firstName, lastName, cpr, "1",false);
    }

    @Given("the customer has a bank account")
    public void theCustomerHasABankAccount() throws Exception {
       // UUID uuid = UUID.randomUUID();
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setCprNumber(customer.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(1000));
            accountIds.add(accountId);
        } catch (BankServiceException_Exception e) {
            retireAccounts();
            e.printStackTrace();
            throw new Exception();
        }
    }


    @When("the user initiates registration as a customer {string}")
    public void theUserInitiatesRegistrationAsACustomer(String userType) {
        System.out.println(userType);
        try {
            successful = customerAPI.createAccount(customer);
            registeredUsers.add(customer);
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
        }
    }

    @Then("registration of customer is successful")
    public void registrationOfCustomerIsSuccessful() {
        assertTrue(successful);
    }

    @After
    public void retireAccounts()  {
        try{
            for (String id : accountIds){
                bank.retireAccount(id);
            }
            accountIds.clear();
            System.out.println(registeredUsers.size());
            for (User user : registeredUsers) {
                accountService.deregister(user);
            }
            registeredUsers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
