Feature: Payment

  Scenario: Successful Payment
    Given the customer "Gus" "Homer" has a bank account with balance 1000
    And the customer is registered with DTUPay
    And the customer has an unused token
    And the token is valid
    And the merchant "Mac" "Hunt" has a bank account with balance 2000
    And the merchant is registered with DTUPay
    When the merchant initiates a payment for 10 kr using the customer token
    Then the payment is successful
    And the balance of the customer at the bank is 990 kr
    And the balance of the merchant at the bank is 2010 kr
    And the token is no longer valid

  Scenario: Failed payment because the customer does not have a DTUPay
    Given the merchant "Mac" "Hunt" has a bank account with balance 2000
    Given the customer "Gus" "Homer" has a bank account with balance 1000
    And the customer has an unused token
    And the token is valid
    And the customer is not registered with DTUPay
    And the merchant is registered with DTUPay
    When the merchant initiates a payment for 1000 kr using the customer token
    Then the payment is not successful
    And the balance of the customer at the bank is 1000 kr
    And the balance of the merchant at the bank is 2000 kr
    And there is an error saying "Customer is not registered with DTUPay"

  Scenario: Failed payment because the token has already been used
    Given the merchant "Mac" "Hunt" has a bank account with balance 2000
    Given the customer "Gus" "Homer" has a bank account with balance 1000
    And the customer has an used token
    And the customer is registered with DTUPay
    And the merchant is registered with DTUPay
    When the merchant initiates a payment for 1000 kr using the customer token
    Then the payment is not successful
    And the balance of the customer at the bank is 1000 kr
    And the balance of the merchant at the bank is 2000 kr
    And there is an error saying "Token is already used"

