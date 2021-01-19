Feature: Token is used for payment 1
Scenario: Tokens is consumed
Given that the token with id "token1" is active
Then we consume it and the token is used
