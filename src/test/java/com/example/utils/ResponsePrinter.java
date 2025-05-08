package com.example.utils;

import io.restassured.response.Response;

public class ResponsePrinter {

    public static void printResponse(String action, Response response) {
        System.out.println(action + " - Status Code: " + response.getStatusCode());
        System.out.println(action + " - Response Body: " + response.getBody().asString());
    }

    public static void validateResponse(String action, Response response, int expectedStatusCode) {
        printResponse(action, response);
        if (response.getStatusCode() != expectedStatusCode) {
            throw new AssertionError(action + " failed: Expected status code " + expectedStatusCode +
                    " but got " + response.getStatusCode() + ". Response body: " + response.getBody().asString());
        }
    }
}
