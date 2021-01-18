Feature: Account

  @AccountTest
  Scenario: Register customer successful
    Given a new customer with name "New" "TestUser" and CPR "333323-7777" has a bank account
    When the user initiates registration as a customer "customer"
    Then registration of customer is successful


  Scenario: Validate customer has a bank account
    Given that customer with CPR "333323-7777" has and account in the bank
    Then we can validate the bank account