package actions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

public class WeatherByCoordinatesActions {

	@Step("Requesting weather by Coordinates: Lat {1}, Lon {2}")
    public void getCoordinateWeather(String url, String key, String lat, String lon) {
        SerenityRest.given()
            .baseUri(url)
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("key", key)
        .when()
            .get("/current");
    }

	@Step("Requesting weather by City: {1}, {2}")
    public void getCityWeather(String url, String key, String city, String country) {
        SerenityRest.given()
            .baseUri(url)
            .queryParam("city", city + "," + country)
            .queryParam("key", key)
        .when()
            .get("/current");
    }
}