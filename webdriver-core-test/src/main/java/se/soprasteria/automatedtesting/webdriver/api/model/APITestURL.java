package se.soprasteria.automatedtesting.webdriver.api.model;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class APITestURL {
    public ValidatableResponse isPageLoaded() {
        return given().when().get("https://pigeon-apitest.github.io/pages/index.html").then();
    }
}
