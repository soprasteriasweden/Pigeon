Feature: subfolder feature
  Scenario: Subfolder Scenario
    Given I have a feature file in a subfolder
    And it has a scenario in it
    When I run a test
    Then Is add location page loaded

  Scenario: Another scenario
    Given I have a feature file in a subfolder
    And it has a scenario in it
    When I run a test
    Then it should parse that feature file
