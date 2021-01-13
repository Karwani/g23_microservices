Feature:Token

  Scenario: User Generate tokens
    Given Customer with userid "sth"
    And the customer does not have enough active tokens
    When customer asks for tokens
    Then the service has generated tokens for the user (return bool / status code)
    And the user receives an active token
