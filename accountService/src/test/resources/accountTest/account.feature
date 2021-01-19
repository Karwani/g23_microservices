Feature: Account

  Scenario: Register user successful
    Given a new user with name "New" "TestUser" and CPR "333323-7777" has a bank account
    When the user initiates registration as a user
    Then registration of user is successful


  Scenario: Validate user has a bank account
    Given that user with CPR "333323-7777" has and account in the bank
    Then we can validate the bank account