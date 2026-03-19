package actions;

import net.serenitybdd.rest.SerenityRest;

public class WeatherByCoordinatesActions {

    public void getCoordinateWeather(String url, String key, String lat, String lon) {
        SerenityRest.given()
            .baseUri(url)
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("key", key)
        .when()
            .get("/current");
    }

    public void getCityWeather(String url, String key, String city, String country) {
        SerenityRest.given()
            .baseUri(url)
            .queryParam("city", city + "," + country)
            .queryParam("key", key)
        .when()
            .get("/current");
    }
}