package com.gr_23.accountTest;
import com.gr_23.models.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountBankValidationSteps {
    boolean successful;
    User user;
    dtu.ws.fastmoney.User DTUuser = new dtu.ws.fastmoney.User();
    BankService bank = new BankServiceService().getBankServicePort();
    List<String> accountIds = new ArrayList<>();
    List<User> registeredUsers = new ArrayList<>();
    AccountService accountService = new AccountService();

    @Given("that user with CPR {string} has and account in the bank")
    public void thatUserWithCPRHasAndAccountInTheBank(String cpr) throws Exception {
        user = new User("Ole", "hansen", cpr, "1", false);
        DTUuser.setFirstName(user.getFirstName());
        DTUuser.setLastName(user.getLastName());
        DTUuser.setCprNumber(user.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(DTUuser, new BigDecimal(1000));
            accountIds.add(accountId);
            accountService.register(user);
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
            for (User user : registeredUsers) {
                accountService.deleteuser(user);
            }
            registeredUsers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
