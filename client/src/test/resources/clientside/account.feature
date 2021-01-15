Feature: Account

  Scenario: Register Customer successful
    Given a new customer with name "GUS" "Homer" and CPR "654321-8172"
    And is type "Customer" who wants to be registered in DTUPay
    When the customer initiates registration
    Then registration of customer is successful