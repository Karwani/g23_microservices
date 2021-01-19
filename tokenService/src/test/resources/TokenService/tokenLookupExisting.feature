Feature: Find user by token
  Scenario: Lookup existing token
  Given that user with userid "1"
  Then we lookup the token "token1" and we find the userid