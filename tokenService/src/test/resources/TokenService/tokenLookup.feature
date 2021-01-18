Feature: look up token
  Scenario: Looking for non existing token
    Given The token with id "token1" does not exist
    Then When we lookup that token we do not find anything