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

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountUpdateSteps {

    boolean successful;
    UserInfo userInfo;
    Customer customer;
    dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    AccountService accountService = new AccountService();

    @Given("that customer with user id {string}")
    public void thatCustomerWithUserId(String userId) throws Exception {
        customer = new Customer("Ole", "hansen", "333323-7777", userId, false);
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setCprNumber(customer.getCprNumber());
        System.out.println("user cpr " + user.getCprNumber());
        System.out.println("customer cpr"+ customer.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(user ,new BigDecimal(1000));
            accountIds.add(accountId);
            accountService.register(customer);
            registeredUsers.add(customer);
            System.out.println("created bank account for customer " + user.getCprNumber());
        } catch (Exception e) {
            retireAccounts();
            successful = false;
            e.printStackTrace();
            throw new Exception();
        }
    }

    @When("the user changes name {string} {string}")
    public void theUserChangesName(String firstname, String lastname) {
        customer = new Customer(firstname, lastname, "333323-7777", "1", false);
        accountService.updateUser(customer);
    }

    @Then("we successful update the customer")
    public void weSuccessfulUpdateNameOnTheCustomer() {
        try{
            accountService.fetchUser(customer.getUserId());
            successful = true;
        } catch (Exception e){
            successful = false;

        }
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
