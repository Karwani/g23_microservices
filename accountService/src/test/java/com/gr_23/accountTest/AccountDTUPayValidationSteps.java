package com.gr_23.accountTest;

import com.gr_23.models.User;
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

public class AccountDTUPayValidationSteps {

    boolean successful;
    User user;
    dtu.ws.fastmoney.User DTUuser = new dtu.ws.fastmoney.User();
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    AccountService accountService = new AccountService();

    @Given("that user with user id {string} has an account in DUTpay")
    public void thatUserWithUserIdHasAndAccountInDUTpay(String userId) throws Exception {
        user = new User("Ole", "hansen", "333323-7777", userId, false);
        DTUuser.setFirstName(user.getFirstName());
        DTUuser.setLastName(user.getLastName());
        DTUuser.setCprNumber(user.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(DTUuser ,new BigDecimal(1000));
            accountIds.add(accountId);
            accountService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }


    @When("we lookup the user with CPR")
    public void weLookupTheUserWithCPR() throws Exception {
        try {
            successful = accountService.validateDTUPayAccount(user.getUserId());
            registeredUsers.add(user);
        } catch (Exception e) {
            retireAccounts();
            successful = false;
            e.printStackTrace();
            throw new Exception();
        }

    }

    @Then("we can validate the DTU pay account")
    public void weCanValidateTheDTUpayaccount() {
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
            for (User user : registeredUsers) {
                accountService.deleteuser(user);
            }
            registeredUsers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
