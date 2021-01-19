Feature: Consume Token

  Scenario: Tokens is consumed
    Given that the token with id "token1" is active
    Then we consume it and the token is used

  Scenario: Token is not consumed
    Given the used token with id "token1"
    Then we consume it and the token is not consumed