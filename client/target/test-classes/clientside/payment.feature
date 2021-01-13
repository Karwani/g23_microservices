Feature: Payment


Scenario: Successful Payment
    Given the customer "Gus" "Homer" with CPR "654321-8172" has a bank account with balance 1000
    And the customer is registered with DTUPay
    And the merchant "Mac" "Hunt" with CPR number "123456-7362" has a bank account with balance 2000
    And the merchant is registered with DTUPay
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 990 kr
    And the balance of the merchant at the bank is 2010 kr


Scenario: Merchant is not registered in DTUPay
    Given the customer "Gus" "Homer" with CPR "654321-8172" has a bank account with balance 1000
    And the customer is registered with DTUPay
    And the merchant "Mac" "Hunt" with CPR number "123456-7362" has a bank account with balance 2000
    And the merchant is not registered with DTUPay
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is not successful
    And an error is given with the message "Merchant is not registered with DTUPay"
    And the balance of the customer at the bank is 1000 kr
    And the balance of the merchant at the bank is 2000 kr
