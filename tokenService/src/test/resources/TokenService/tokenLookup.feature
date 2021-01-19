Feature: Look up Token

  Scenario: Looking for non existing token
    Given The token with id "token1" does not exist
    Then When we lookup that token we do not find anything

  Scenario: Lookup existing token
    Given that user with userid "1"
    Then we lookup the token "token1" and we find the userid