package com.example.utils;

import io.restassured.response.Response;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public class ResponseValidator {
    
    public static void validateResponse(String operation, Response response, int expectedStatusCode) {
        ResponsePrinter.validateResponse(operation, response, expectedStatusCode);
    }

    public static void validateResponseBody(Response response, String path, Matcher<?> matcher) {
        response.then().body(path, matcher);
    }

    public static void validateResponseMessage(Response response, String expectedMessage) {
        response.then().body("message", equalTo(expectedMessage));
    }

    public static <T> T extractValue(Response response, String path) {
        return response.then().extract().path(path);
    }
} 