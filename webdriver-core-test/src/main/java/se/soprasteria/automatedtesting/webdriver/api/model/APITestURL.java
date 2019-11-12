package se.soprasteria.automatedtesting.webdriver.api.model;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class APITestURL {
    public ValidatableResponse isPageLoaded() {
        HashMap<String, String> map = new HashMap();
        map.put("key", "e5879e12bc684f5fb52b4f4a8f81e616");
        map.put("stationsonly", "True");
        map.put("searchstring", "Fenixvägen");
        map.put("maxresults", "1");

        return given()
                .queryParams(map)
                .get("https://api.sl.se/api2/typeahead.json").then();
    }

    public List getIdsFromJSONResponseWithQuery(String apiEndpoint, HashMap<String, String> queryParam) {
        Response response = given()
                .queryParams(queryParam)
                .get(apiEndpoint).then().extract().response();
        List<String> listOfIds = response.jsonPath().getList("results");
        System.out.println("The List with queryParam of id: " + listOfIds + " for PATH: " + apiEndpoint);
        return listOfIds;
    }


}
