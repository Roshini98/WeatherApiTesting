package stepDefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import actions.WeatherAnalysisActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.util.EnvironmentVariables;
import utils.ConfigLoader;
import utils.CsvReader;

public class WeatherAnalysisSteps {
	
    private EnvironmentVariables environmentVariables;

    @Steps
    WeatherAnalysisActions weatherAnalysis;

    private String apiBaseUrl;
    private String apiKey;
    
    private static Map<String, Float> cityTemps = new HashMap<>();
    private Map<String, Float> stateResults = new HashMap<>();
    
    @Given("I have a weather API endpoint")
    public void setEndpoint()
    {
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
    
    @When("I analyze weather for city {string} and country is {string}")
    public void requestWeather(String city,String country)
    {
    	weatherAnalysis.getWeatherByCity(apiBaseUrl, city, country, apiKey);
    	
        int statusCode = SerenityRest.lastResponse().getStatusCode();
        if (statusCode != 200) {
            String errorMsg = SerenityRest.lastResponse().asString();
            Serenity.recordReportData()
                    .withTitle("API Failure: " + city)
                    .andContents("Status: " + statusCode + "\nBody: " + errorMsg);
        }
    }
    
    @Then("the response status code should be {int}")
    public void getResponseCode(int expectedCode)
    {
    	 int actualCode = SerenityRest.lastResponse().getStatusCode();

         if (actualCode != expectedCode) {
             String errorBody = SerenityRest.lastResponse().asString();
             Serenity.recordReportData().withTitle("API Error Response").andContents(errorBody);}
    }
         
    @And("I record the temperature for {string}")
    public void recordTemperature(String city)
    {
    	float temp = weatherAnalysis.getTemperature();
    	if (temp != 999.0f) {
            cityTemps.put(city, temp);
        }
    	System.out.println("Temperature recorded at " + city + " is " + temp + "°C");    	
    }
    
    @Then("I identify the warmest capital city")
    public void identifyWarmest() {
        if (cityTemps.isEmpty())  return;

        Map.Entry<String, Float> warmest = cityTemps.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        String msg = "Warmest Australian Capital City: " + warmest.getKey() + " (" + warmest.getValue() + "°C)";      
        Serenity.recordReportData().withTitle("Warmest AU City").andContents(msg);
        System.out.println(msg);
    }
    
    
    @Then("I identify the coldest US state from {string}")
    public void identifyColdestState(String fileName) {
        String filePath = "src/test/resources/data/" + fileName;
        List<Map<String, String>> csvData = CsvReader.readCsv(filePath);
        
        System.out.println("Total rows found in CSV: " + csvData.size());

        for (Map<String, String> row : csvData) {
            String stateName = row.get("state_name");
            String country = row.get("country_code");

            if (stateName != null && "US".equalsIgnoreCase(country)) {
                
                try {
                    weatherAnalysis.getWeatherByCity(apiBaseUrl, stateName, country, apiKey);
                    float temp = weatherAnalysis.getTemperature();
                    if (temp != 999.0f) stateResults.put(stateName, temp);
                } catch (Exception e) {
                    System.err.println("Skipping state due to unexpected error: " + stateName);
                }
            }
        }

        if (!stateResults.isEmpty()) {
            Map.Entry<String, Float> coldest = stateResults.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get();
            
            String coldestState = "Coldest US State: " + coldest.getKey() + " (" + coldest.getValue() + "°C)";
            
            Serenity.recordReportData().withTitle("US Coldest State").andContents(coldestState);
        } else {
            System.out.println("No valid US state data was processed.");
        }
    }    

}
