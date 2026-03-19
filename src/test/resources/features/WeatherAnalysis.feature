Feature: Australian Capital Cities and US states Weather Analysis

  Scenario Outline: Identify the current temperature for all Australian capital cities
    Given I have a weather API endpoint
    When I analyze weather for city "<city>" and country is "AU"
    Then the response status code should be 200
    And I record the temperature for "<city>"

    Examples:
      | city      |
      | Sydney    |
      | Melbourne |
      | Brisbane  |
      | Perth     |
      | Adelaide  |
      | Hobart    |
      | Canberra  |
      | Darwin    |
      
  Scenario: Finding Warmest Australian Capital City
    Then I identify the warmest capital city
    
  Scenario: Identify the coldest US state from external metadata
    Given I have a weather API endpoint
    Then I identify the coldest US state from "states.csv"