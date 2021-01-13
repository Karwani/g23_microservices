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
	PayService dtuPay = new PayService();
	User customer;
	User merchant;
	boolean successful;
	List<String> accountIds = new ArrayList<>();
	List<User> registeredUsers = new ArrayList<>();
	String error = "";


	@Given("the customer {string} {string} with CPR {string} has a bank account with balance {int}")
	public void theCustomerWithCPRHasABankAccountWithBalance(String firstName, String lastName, String cpr, int balance) throws Exception {
		customer = new Customer(firstName,lastName, cpr,"1",false);
		dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setCprNumber(cpr);
		try {
			String accountId = bank.createAccountWithBalance( user ,new BigDecimal(balance));
			accountIds.add(accountId);
			System.out.println("created bank account for customer " + customer.getCprNumber());
		} catch (BankServiceException_Exception e) {
			retireAccounts();
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Given("the customer is registered with DTUPay")
	public void theCustomerIsRegisteredWithDTUPay() {
		dtuPay.register(customer,"customer");
		registeredUsers.add(customer);
	}

	@Given("the merchant {string} {string} with CPR number {string} has a bank account with balance {int}")
	public void theMerchantWithCPRNumberHasABankAccountWithBalance(String firstName, String lastName, String cpr, int balance) throws Exception {
		merchant = new Merchant(firstName,lastName, cpr,"2",false,"some-CVR");
		dtu.ws.fastmoney.User user = new dtu.ws.fastmoney.User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setCprNumber(cpr);
		try {
			String accountId = bank.createAccountWithBalance( user ,new BigDecimal(balance));
			System.out.println("account created in the bank "+ user.getCprNumber());
			accountIds.add(accountId);
		} catch (BankServiceException_Exception e) {
			retireAccounts();
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Given("the merchant is registered with DTUPay")
	public void theMerchantIsRegisteredWithDTUPay() {
		dtuPay.register(merchant, "merchant");
		registeredUsers.add(merchant);
	}

	@Given("the merchant is not registered with DTUPay")
	public void theMerchantIsNotRegisteredWithDTUPay() {
		dtuPay.deregister(merchant);
	}

	@When("the merchant initiates a payment for {int} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
		try {
			successful = dtuPay.pay(merchant.getUserId(),customer.getUserId(),"",amount);
			if (!successful)
				System.out.println(error);
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
