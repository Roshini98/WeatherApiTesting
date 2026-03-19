Feature: Weather by Coordinates and major International Cities

  Scenario: Verify API returns valid data for specific coordinates
    Given the weather API endpoint is set
    When I request weather for Latitude "-33.865143" and Longitude "151.209900"
    Then I should get a valid response data
    
    
  Scenario Outline: Verify weather data for major international cities
    Given the weather API endpoint is set
    When I request weather for city "<city>" and country "<country>"
    Then I should get a valid response status code
    And the response should contain the city name "<city>"

    Examples:
      | city      		  | country |
      | London   		  | UK      |
      | Tokyo     		  | JP      |
      | New York	 	  | US      |
      | Paris     		  | FR      |
      | Sydney    		  | AU      |