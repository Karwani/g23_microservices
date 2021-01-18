package com.gr_23.accountTest;

import com.gr_23.Customer;
import com.gr_23.User;
import com.gr_23.UserInfo;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class AccountValidationSteps {

    boolean successful;
    Customer customer;
    dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    AccountService accountService = new AccountService();

    @Given("that customer with user id {string} has and account in DUTpay")
    public void thatCustomerWithUserIdHasAndAccountInDUTpay(String userId) throws Exception {
        customer = new Customer("Ole", "hansen", "333323-7777", userId, false);
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setCprNumber(customer.getCprNumber());
        System.out.println("user cpr " + user.getCprNumber());
        System.out.println("customer cpr"+ customer.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(1000));
            accountIds.add(accountId);
//            accountService.register(customer, "customer");
//            successful = accountService.validateDTUPayAccount(customer.getCprNumber());
//            registeredUsers.add(customer);
            System.out.println("created bank account for customer " + user.getCprNumber());
        } catch (Exception e) {
            retireAccounts();
            successful = false;
            e.printStackTrace();
            throw new Exception();
        }
    }


    @When("we lookup the user with CPR")
    public void weLookupTheUserWithCPR() throws Exception {
        try {
            successful = accountService.validateDTUPayAccount(customer.getUserId());
            registeredUsers.add(customer);
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
            throw new Exception();
        }

    }

    @Then("we have validated that the user exist")
    public void weHaveValidatedThatTheUserExist() {
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
