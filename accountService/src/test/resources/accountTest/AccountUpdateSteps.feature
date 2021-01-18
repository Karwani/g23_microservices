Feature: Account
  Scenario: Update customer successful
    Given that customer with user id "1"
    When the user changes name "sth" "ole"
    Then we successful update the customer