Feature: Account
  Scenario: Remove customer successful
    Given a customer with user id "1" has a account in the bank
    When we search for the user
    Then we successful delete that customer