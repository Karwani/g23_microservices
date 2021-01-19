Feature: Account
  Scenario: Validate user has a DTUpay account
    Given that user with user id "1" has an account in DUTpay
    When we lookup the user with CPR
    Then we can validate the DTU pay account
