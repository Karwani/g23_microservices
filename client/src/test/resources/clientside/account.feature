Feature: Account

  Scenario: Register Customer
    Given a new customer with id ”1”
    And name “New” “User”
    When user initiates registration
    And the type is “Customer”
    Then a new user is created

