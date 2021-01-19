Feature:Token

  Scenario: User Generate tokens
    Given Customer with userid "sth"
    And the customer does not have enough active tokens
    When customer asks for tokens
    Then the service has generated tokens for the user (return bool / status code)
    And the user receives an active token

  Scenario: Lookup existing token
    Given that user with userid "1" with id "token1"
    When we lookup the token "token1"
    Then we find the userid

#  Scenario: Tokens is consumed
#    Given that the token with id "token1" is active
#    When we consume it
#    Then the token is used

#  Scenario: Token is not consumed
#    Given that the token with id “token1” is inactive
#    When we consume the token
#    Then the tokens is not consumed
