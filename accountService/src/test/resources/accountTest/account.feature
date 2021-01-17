Feature: Account

  @AccountTest
  Scenario: Validate customer has a bank account
    Given that customer with CPR "333323-7777" has and account in the bank
    Then we can validate the bank account