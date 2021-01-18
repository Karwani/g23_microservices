Feature: Generate Token

  Scenario: User is valid to generate tokens
    Given that the user with userid "9999" has less than two tokens
    When tokens are generated for the user
    Then the user has 5 tokens

  Scenario: User is invalid to generate tokens
    Given that the user with id "1" has two tokens or more
    When the user tries to generate tokens
    Then the user will not receive more tokens
