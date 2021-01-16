Feature: Account

  @AccountTest
  Scenario: Register Customer successful
    Given a new customer with name "New" "TestUser" and CPR "333323-7777"
    Given the customer has a bank account
#    And is type "customer" who wants to be registered in DTUPay
    When the user initiates registration as a customer "customer"
    Then registration of customer is successful