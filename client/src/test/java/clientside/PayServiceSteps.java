package clientside;


import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PayServiceSteps {

	BankService bank = new BankServiceService().getBankServicePort();
	PayService dtuPay = new PayService(bank);
	UserInfo customer;
	UserInfo merchant;
	AccountInfo account;
	boolean successful;
	List<String> accountIds = new ArrayList<>();
	List<UserInfo> registeredUsers = new ArrayList<>();
	String error = "";


	@Given("the customer {string} {string} with CPR {string} has a bank account")
	public void theCustomerWithCPRHasABankAccount(String firstName, String lastName, String cpr) {
		customer = new UserInfo(cpr,firstName,lastName);
		account = new AccountInfo();
		account.setUser(customer.asUser());
	}

	@Given("the balance of that account is {int}")
	public void theBalanceOfThatAccountIs(int balance) throws Exception {
		try {
			String accountId = bank.createAccountWithBalance(account.getUser(),new BigDecimal(balance));
			accountIds.add(accountId);
		} catch (BankServiceException_Exception e) {
			retireAccounts();
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Given("the customer is registered with DTUPay")
	public void theCustomerIsRegisteredWithDTUPay() {
		dtuPay.register(customer);
		registeredUsers.add(customer);
	}

	@Given("the merchant {string} {string} with CPR number {string} has a bank account")
	public void theMerchantWithCPRNumberHasABankAccount(String firstName, String lastName, String cpr) {
		merchant = new UserInfo(cpr,firstName,lastName);
		account = new AccountInfo();
		account.setUser(merchant.asUser());
	}

	@Given("the merchant is registered with DTUPay")
	public void theMerchantIsRegisteredWithDTUPay() {
		dtuPay.register(merchant);
		registeredUsers.add(merchant);
	}

	@Given("the merchant is not registered with DTUPay")
	public void theMerchantIsNotRegisteredWithDTUPay() {
		dtuPay.deregister(merchant);
	}

	@When("the merchant initiates a payment for {int} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
		try {
			Payment payment = new Payment(customer.getCprNumber(), merchant.getCprNumber(), new BigDecimal(amount));
			successful = dtuPay.pay(payment);
		} catch (Exception e) {
			successful = false;
			error = e.getMessage();
		}
	}

	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);
	}

	@Then("the payment is not successful")
	public void thePaymentIsNotSuccessful() {
		assertFalse(successful);
	}

	@Then("the balance of the customer at the bank is {int} kr")
	public void theBalanceOfTheCustomerAtTheBankIsKr(Integer int1) throws Exception {
		try {
			Account customerAccount = bank.getAccountByCprNumber(customer.getCprNumber());
			BigDecimal balance = customerAccount.getBalance();
			assertEquals(new BigDecimal(int1), balance);
		} catch (BankServiceException_Exception e) {
			retireAccounts();
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Then("the balance of the merchant at the bank is {int} kr")
	public void theBalanceOfTheMerchantAtTheBankIsKr(Integer int1) throws Exception {
		try {
			Account customerAccount = bank.getAccountByCprNumber(merchant.getCprNumber());
			BigDecimal balance = customerAccount.getBalance();
			assertEquals(new BigDecimal(int1), balance);
		} catch (BankServiceException_Exception e) {
			retireAccounts();
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Then("an error is given with the message {string}")
	public void anErrorIsGivenWithTheMessage(String string) {
		assertEquals(string, error);
	}

	@After
	public void retireAccounts() throws BankServiceException_Exception {
		for (String id : accountIds){
			bank.retireAccount(id);
		}
		for (UserInfo user : registeredUsers) {
			dtuPay.deregister(user);
		}
	}
}
