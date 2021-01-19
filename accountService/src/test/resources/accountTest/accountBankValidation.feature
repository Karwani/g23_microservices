Feature: Account
  Scenario: Validate user has a bank account
    Given that user with CPR "333323-7777" has and account in the bank
    Then we can validate the bank account