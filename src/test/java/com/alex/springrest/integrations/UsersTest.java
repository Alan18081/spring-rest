package com.alex.springrest.integrations;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=3000;
    }

    @Test
    void createUser() {
        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("type", "shipping");
        shippingAddress.put("city", "New York");
        shippingAddress.put("country", "Ukraine");
        shippingAddress.put("postalCode", "ABC123");
        shippingAddress.put("streetName", "Central street");

        List<Object> addresses = new ArrayList<>();
        addresses.add(shippingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Alan");
        userDetails.put("lastName", "Morgan");
        userDetails.put("email", "cooper@gmail.com");
        userDetails.put("password", "123456");
        userDetails.put("adresses", addresses);

        Response response = given()
            .contentType("application/json")
            .accept("application/json")
            .body(userDetails)
            .when()
            .post("/users")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();

        String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);

        String bodyString = response.body().toString();
        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray resAddresses = responseBodyJson.getJSONArray("addresses");
            assertNotNull(resAddresses);
            assertEquals(resAddresses.length(), 1);

            String addressId = resAddresses.getJSONObject(0).getString("addressId");
            assertNotNull(addressId);
            assertEquals(addressId.length(), 30);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

}
