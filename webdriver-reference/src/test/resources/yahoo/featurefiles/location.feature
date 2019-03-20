Feature: Location
  Scenario: Add Valid Location
    Given main page is loaded
    When I click add location
    When I add new location as "Stockholm"
    Then current location is Stockholm