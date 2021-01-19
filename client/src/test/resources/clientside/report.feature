Feature: Report

  Scenario: Report payments made by customer with size 1
    Given the customer "Gus" "Homer" has a bank account with balance 1000
    And the customer is registered with DTUPay
    And the customer has an unused token
    And the token is valid
    And the merchant "Mac" "Hunt" has a bank account with balance 2000
    And the merchant is registered with DTUPay
    And the merchant initiates a payment for 10 kr using the customer token
    When the customer requests a report of his payments
    Then the server returns a valid list of size 1

  Scenario: Report payments when there are no payments
    Given the customer "Gus" "Homer" has a bank account with balance 1000
    And the customer is registered with DTUPay
#    And the customer has an unused token
#    And the token is valid
#    And the merchant "Mac" "Hunt" has a bank account with balance 2000
#    And the merchant is registered with DTUPay
#    And the merchant initiates a payment for 10 kr using the customer token
    When the customer requests a report of his payments
    Then the server returns null