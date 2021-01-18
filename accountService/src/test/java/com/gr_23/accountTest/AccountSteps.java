package com.gr_23.accountTest;
import com.gr_23.AccountServer;
import com.gr_23.Customer;
import com.gr_23.User;
import com.gr_23.UserInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountSteps {

    boolean successful;
    UserInfo userInfo;
    Customer customer;
    dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    AccountService accountService = new AccountService();

    @Given("a new user with name {string} {string} and CPR {string} has a bank account")
    public void aNewUserWithNameAndCPRHasABankAccount(String firstName, String lastName, String CPR) throws Exception {
        System.out.println("called first");
        customer = new Customer(firstName, lastName, CPR, "1", false);
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setCprNumber(customer.getCprNumber());
        System.out.println("user cpr " + user.getCprNumber());
        System.out.println("customer cpr"+ customer.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(1000));
            accountIds.add(accountId);
            System.out.println("created bank account for customer " + user.getCprNumber());
        } catch (BankServiceException_Exception e) {
            retireAccounts();
            e.printStackTrace();
            throw new Exception();
        }
    }

    @When("the user initiates registration as a user")
    public void theUserInitiatesRegistrationAsAUser() {
        try {
            successful = accountService.register(customer);
            registeredUsers.add(customer);
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
        }
    }

    @Then("registration of user is successful")
    public void registrationOfUserIsSuccessful() {
        assertTrue(successful);
    }


    @Given("that customer with CPR {string} has and account in the bank")
    public void thatCustomerWithCPRHasAndAccountInTheBank(String cpr) throws Exception {
        customer = new Customer("Ole", "hansen", cpr, "1", false);
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setCprNumber(customer.getCprNumber());
        System.out.println("user cpr " + user.getCprNumber());
        System.out.println("customer cpr"+ customer.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(1000));
            accountIds.add(accountId);
            successful = accountService.validateAccount(customer.getCprNumber());
            registeredUsers.add(customer);
            System.out.println("created bank account for customer " + user.getCprNumber());
        } catch (Exception e) {
            retireAccounts();
            successful = false;
            e.printStackTrace();
            throw new Exception();
        }
    }

    @Then("we can validate the bank account")
    public void weCanValidateTheBankAccount() throws Exception {
        assertTrue(successful);
    }

    @After
    public void retireAccounts()  {
        try{
            for (String id : accountIds){
                bank.retireAccount(id);
                System.out.println("retired account "+ id);
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
