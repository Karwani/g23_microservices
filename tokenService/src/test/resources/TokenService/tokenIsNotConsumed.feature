Feature: Token is used for payment 2
Scenario: Token is not consumed
  Given the used token with id "token1"
  Then we consume it and the token is not consumed
