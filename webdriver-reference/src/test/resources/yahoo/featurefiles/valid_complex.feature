@billing @bicker @annoy
Feature: Examples feature

  Scenario Outline: Working out
    Given I weigh <weight> kg
    When we calculate the feeding requirements
    Then the energy should be <energy> MJ
    And the protein should be <protein> kg

    Examples: scenario 1 examples 1
      | weight | energy | protein |
      | 450    | 26500  | 215     |
      | 500    | 29500  | 245     |
      | 575    | 31500  | 255     |
      | 600    | 37000  | 305     |

    Examples: scenario 1 examples 2
      | weight | energy | protein |
      | 1      | 2      | 3       |
      | 2      | 3      | 4       |
      | 3      | 4      | 5       |
      | 4      | 5      | 6       |

