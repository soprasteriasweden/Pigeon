# Reference testcase
Reference testcases showing framework capabilities

## BDD using Gherkin
### Installation
Before starting to use BDD it is recommended to install the IntelliJ gherkin plugin.


### Usage
Below is an example usage showcasing all the supported features of gherkin.

    @billing @bicker @annoy
    Feature: Examples feature

      Scenario Outline: Working out
        Given I weigh <weight> kg
        When we calculate the feeding requirements
        Then the energy should be <energy> MJ
        Then the protein should be <protein> kg

        Examples: scenario 1 examples 1
          | weight | energy | protein |
          | 450    | 26500  | 215     |
          | 500    | 29500  | 245     |
          | 575    | 31500  | 255     |
          | 600    | 37000  | 305     |

      Scenario Outline: Getting fit
        Given I go to the gym
        When I workout
        Then I should get healthier
