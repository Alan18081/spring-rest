package com.alex.springrest.integrations;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersTest {

    private final String EMAIL = "cooper@gmail.com";
    private final String PASSWORD = "123456";
    private final String JSON = "application/json";
    private String userId;
    private String authToken;

    @BeforeEach
    void setup() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=3000;
    }

    @Test
    @Order(1)
    void a_createUser() {
        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("type", "shipping");
        shippingAddress.put("city", "New York");
        shippingAddress.put("country", "Ukraine");
        shippingAddress.put("postalCode", "ABC123");
        shippingAddress.put("streetName", "Central street");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("type", "billing");
        billingAddress.put("city", "New York");
        billingAddress.put("country", "Ukraine");
        billingAddress.put("postalCode", "ABC123");
        billingAddress.put("streetName", "Central street");

        List<Object> addresses = new ArrayList<>();
        addresses.add(shippingAddress);
        addresses.add(billingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Alan");
        userDetails.put("lastName", "Morgan");
        userDetails.put("email", EMAIL);
        userDetails.put("password", PASSWORD);
        userDetails.put("addresses", addresses);

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

        JsonPath jsonPath = response.jsonPath();
        userId = jsonPath.getString("userId");
        List<Map<String, String>> resAddresses = jsonPath.getList("addresses");
        assertNotNull(userId);
        assertNotNull(resAddresses);
        assertEquals(resAddresses.size(), 2);

        String addressId = resAddresses.get(0).get("addressId");
        assertNotNull(addressId);
        assertEquals(addressId.length(), 30);
}

    @Test
    @Order(2)
    void b_testLogin() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", EMAIL);
        loginDetails.put("password", PASSWORD);

        Response response = given().contentType(JSON).accept(JSON)
                .body(loginDetails)
                .when()
                .post("/users/login")
                .then()
//                .statusCode(200)
                .contentType(JSON)
                .extract().response();

        authToken = response.jsonPath().getString("token");
        System.out.println(response.getBody().print());
        Map<String, String> userDetails = response.jsonPath().getMap("user");
        assertNotNull(authToken);
        assertNotNull(userDetails);
    }

    @Test
    @Order(3)
    void c_getUserById() {
        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", "Bearer " + authToken)
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.getString("userId"));
        assertNotNull(jsonPath.getString("firstName"));
        assertNotNull(jsonPath.getString("lastName"));
        assertNotNull(jsonPath.getString("email"));
        assertEquals(EMAIL, jsonPath.getString("email"));
        assertEquals(jsonPath.getList("addresses").size(), 2);
    }

    @Test
    @Order(4)
    void d_updateUserById() {
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("firstName", "Elon");
        userDetails.put("lastName", "Musk");

        Response response = given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", "Bearer " + authToken)
                .pathParam("id", userId)
                .body(userDetails)
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.get("firstName"));
        assertNotNull(jsonPath.get("lastName"));
        assertNotNull(jsonPath.get("email"));
        assertNotNull(jsonPath.get("addresses"));

        assertEquals(jsonPath.get("firstName"), "Elon");
        assertEquals(jsonPath.get("lastName"), "Musk");
        assertEquals(EMAIL, jsonPath.getString("email"));
        assertEquals(jsonPath.getList("addresses").size(), 2);
    }

    @Test
    @Order(5)
    void e_deleteUserById() {
        given()
                .contentType(JSON)
                .accept(JSON)
                .header("Authorization", "Bearer " + authToken)
                .pathParam("id", userId)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(200)
                .contentType(JSON);
    }

}
