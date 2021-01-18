Feature: Account
  Scenario: Validate customer has a DTUpay account
    Given that customer with user id "1" has and account in DUTpay
    When we lookup the user with CPR
    Then we have validated that the user exist
