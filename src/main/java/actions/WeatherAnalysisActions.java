package actions;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

public class WeatherAnalysisActions {
	
	@Step("Requesting weather for city : {1},{2}")
    public void getWeatherByCity(String url, String city, String country, String apikey) {
        
        if (city == null || city.isBlank()) {
            System.err.println("Unable to make API call as City name is blank");
            return;
        }

        SerenityRest.given()
            .baseUri(url)
            .queryParam("city", city)
            .queryParam("country", country)
            .queryParam("key", apikey)
        .when()
            .get("/current");
    }
	
	public float getTemperature()
	{
		Response response = SerenityRest.lastResponse();
        
        if (response.getStatusCode() != 200) {
            System.out.println("API Error: Received status code " + response.getStatusCode());
            return 999.0f;
        }

        try {
            return response.jsonPath().getFloat("data[0].temp");
        } catch (Exception e) {
            System.out.println("Parsing Error: Could not find temperature in response body.");
            return 999.0f; 
        }
	}

}
