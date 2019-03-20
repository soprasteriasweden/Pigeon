@billing @bicker @annoy
Feature: Array feature

  Scenario Outline: feeding a suckler cow
    Given the cow weighs <weight> kg
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
      | 450    | 26500  | 215     |
      | 500    | 29500  | 245     |
      | 575    | 31500  | 255     |
      | 600    | 37000  | 305     |


  Scenario Outline: feeding a suckler cow
    Given the cow weighs <weight> kg
    When we calculate the feeding requirements
    Then the energy should be <energy> MJ
    And the protein should be <protein> kg

    Examples: scenario 2 examples 1
      | one    | two    | three   |
      | 450    | 26500  | 215     |
      | 500    | 29500  | 245     |
      | 575    | 31500  | 255     |
      | 600    | 37000  | 305     |
