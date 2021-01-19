package com.gr_23.accountTest;
import com.gr_23.models.Customer;
import com.gr_23.models.User;
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
    User user;
    dtu.ws.fastmoney.User DTUuser = new dtu.ws.fastmoney.User();
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    AccountService accountService = new AccountService();

    @Given("a new user with name {string} {string} and CPR {string} has a bank account")
    public void aNewUserWithNameAndCPRHasABankAccount(String firstName, String lastName, String CPR) throws Exception {
        user = new User(firstName, lastName, CPR, "1", false);
        DTUuser.setFirstName(user.getFirstName());
        DTUuser.setLastName(user.getLastName());
        DTUuser.setCprNumber(user.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(DTUuser ,new BigDecimal(1000));
            accountIds.add(accountId);
        } catch (BankServiceException_Exception e) {
            retireAccounts();
            e.printStackTrace();
            throw new Exception();
        }
    }

    @When("the user initiates registration as a user")
    public void theUserInitiatesRegistrationAsAUser() {
        try {
            successful = accountService.register(user);
            registeredUsers.add(user);
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
        }
    }

    @Then("registration of user is successful")
    public void registrationOfUserIsSuccessful() {
        assertTrue(successful);
    }


    @Given("that user with CPR {string} has and account in the bank")
    public void thatUserWithCPRHasAndAccountInTheBank(String cpr) throws Exception {
        user = new User("Ole", "hansen", cpr, "1", false);
        DTUuser.setFirstName(user.getFirstName());
        DTUuser.setLastName(user.getLastName());
        DTUuser.setCprNumber(user.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(DTUuser, new BigDecimal(1000));
            accountIds.add(accountId);
            successful = accountService.validateAccount(user.getCprNumber());
            registeredUsers.add(user);
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

