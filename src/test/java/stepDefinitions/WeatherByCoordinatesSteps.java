package stepDefinitions;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.thucydides.model.util.EnvironmentVariables;
import net.serenitybdd.rest.SerenityRest;
import utils.ConfigLoader;
import actions.WeatherByCoordinatesActions;

import static org.hamcrest.Matchers.*;

public class WeatherByCoordinatesSteps {

    private EnvironmentVariables environmentVariables;

    @Steps
    WeatherByCoordinatesActions weatherByCoordinates;

    private String apiBaseUrl;
    private String apiKey;

    @Given("the weather API endpoint is set")
    public void setEndpoint() {

    	try {
            apiBaseUrl = ConfigLoader.getProp(environmentVariables, "base.url");
            apiKey = ConfigLoader.getProp(environmentVariables, "weather.api.key");
            
            if (apiBaseUrl == null || apiKey == null) {
                throw new RuntimeException("Missing API configuration in serenity.conf or properties file.");
            }
        } catch (Exception e) {
            Serenity.recordReportData().withTitle("Configuration Error").andContents(e.getMessage());
            throw e;
        }
    }

    @When("I request weather for Latitude {string} and Longitude {string}")
    public void requestWeatherByCoords(String lat, String lon) {
    	try {
    		weatherByCoordinates.getCoordinateWeather(apiBaseUrl, apiKey, lat, lon);
        } catch (Exception e) {
            Serenity.recordReportData().withTitle("Network Exception")
                    .andContents("Failed to connect for coordinates: " + lat + ", " + lon + "\nError: " + e.getMessage());
            throw new RuntimeException("API Connection Failed: " + e.getMessage());
        }
    }
    
    @Then("I should get a valid response data")
    public void verifyValidData() {
        int status = SerenityRest.lastResponse().getStatusCode();
        if (status != 200) {
            String errorDetails = SerenityRest.lastResponse().asString();
            Serenity.recordReportData().withTitle("API Error Details").andContents("Status: " + status + "\nBody: " + errorDetails);
            throw new AssertionError("Expected 200 OK but received " + status);
        }

        SerenityRest.lastResponse().then()
            .body("data", notNullValue())
            .body("data[0].city_name", not(emptyOrNullString()));
    }

    @When("I request weather for city {string} and country {string}")
    public void requestWeatherByCity(String city, String country) {
    	weatherByCoordinates.getCityWeather(apiBaseUrl, apiKey, city, country);
        int status = SerenityRest.lastResponse().getStatusCode();
        if (status != 200) {
            Serenity.recordReportData().withTitle("Request Failed for " + city)
                    .andContents("Status: " + status + "\nResponse: " + SerenityRest.lastResponse().asString());
        }
    }

    @Then("I should get a valid response status code")
    public void verifyStatus() {
        SerenityRest.lastResponse().then()
            .statusCode(200)
            .statusLine(containsString("OK"));
    }

    @And("the response should contain the city name {string}")
    public void verifyCityInResponse(String expectedCity) {
        try {
        	
            int dataSize = SerenityRest.lastResponse().jsonPath().getList("data").size();
            
            if (dataSize == 0) {
                throw new NoSuchFieldException("API returned 200 but 'data' array is empty for city: " + expectedCity);
            }

            SerenityRest.lastResponse().then()
                .body("data[0].city_name", containsStringIgnoringCase(expectedCity));
                
        } catch (NoSuchFieldException e) {
            Serenity.recordReportData().withTitle("Data Validation Error").andContents(e.getMessage());
            throw new AssertionError(e.getMessage());
        } catch (Exception e) {
            Serenity.recordReportData().withTitle("Parsing Error").andContents("Could not parse JSON for " + expectedCity);
            throw new AssertionError("Failed to parse weather data: " + e.getMessage());
        }
    }
}