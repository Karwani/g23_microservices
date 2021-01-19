Feature: Account
  Scenario: Update user successful
    Given that user with user id "1"
    When the user changes user "sth" "ole"
    Then we successful update the user