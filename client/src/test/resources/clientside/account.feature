Feature: Account

  Scenario: Register Customer
    Given a new customer with id ”1”
    And name “New” “User” and "CPR"
    When user initiates registration
    And the type is “Customer”
    Then registration of customer is successful

