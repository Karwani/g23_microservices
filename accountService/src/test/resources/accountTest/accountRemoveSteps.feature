Feature: Account
  Scenario: Remove user successful
    Given a user with user id "1" has a account in the bank
    When we search for the user
    Then we successful delete that user