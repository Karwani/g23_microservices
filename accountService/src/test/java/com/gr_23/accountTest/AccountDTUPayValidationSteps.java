package com.gr_23.accountTest;

import com.gr_23.business_logic.AccountManagement;
import com.gr_23.business_logic.IAccountManagement;
import com.gr_23.data_access.AccountRepository;
import com.gr_23.data_access.IAccountRepository;
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
    IAccountRepository accountRepository = new AccountRepository();
    IAccountManagement accountManagement = new AccountManagement(null, accountRepository);

    @Given("that user with user id {string} has an account in DUTpay")
    public void thatUserWithUserIdHasAndAccountInDUTpay(String userId) throws Exception {
        user = new User("Ole", "hansen", "333323-7777", userId, false);
        DTUuser.setFirstName(user.getFirstName());
        DTUuser.setLastName(user.getLastName());
        DTUuser.setCprNumber(user.getCprNumber());

        try {
            String accountId = bank.createAccountWithBalance(DTUuser ,new BigDecimal(1000));
            accountIds.add(accountId);
            accountManagement.createDTUPayAccount(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }


    @When("we lookup the user with CPR")
    public void weLookupTheUserWithCPR() throws Exception {
        try {
            successful = accountManagement.validateBankAccount(user.getCprNumber());
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
            }
            accountIds.clear();
            for (User user : registeredUsers) {
                accountManagement.deleteUsers(user.getUserId());
            }
            registeredUsers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
