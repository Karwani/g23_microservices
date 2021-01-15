Feature: Account

  Scenario: Register Customer successful
    Given a new customer with name "New" "TestUser" and CPR "233223-7777"
    Given the customer has a bank account
    And is type "Customer" who wants to be registered in DTUPay
    When the customer initiates registration
    Then registration of customer is successful