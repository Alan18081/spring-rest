package com.alex.springrest.integrations;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTest {

    private final String EMAIL = "cooper@gmail.com";
    private final String JSON = "application/json";

    @BeforeEach
    void setup() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=3000;
    }

    @Test
    void testLogin() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", EMAIL);
        loginDetails.put("password", "123456");

        Response response = given().contentType(JSON).accept(JSON)
                .body(loginDetails)
                .when()
                .post("/users/login")
                .then()
                .extract().response();

        String token = response.jsonPath().getString("token");
        Map<String, String> userDetails = response.jsonPath().getMap("user");
        assertNotNull(token);
        assertNotNull(userDetails);
    }

}
