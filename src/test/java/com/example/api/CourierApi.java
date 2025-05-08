package com.example.api;

import com.example.Courier;
import com.example.utils.ResponsePrinter;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Создание курьера: {courier}")
    public static Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(BASE_URL + COURIER_PATH);
    }

    @Step("Логин курьера")
    public static Response loginCourier(Courier courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(BASE_URL + COURIER_PATH + "/login");
        ResponsePrinter.printResponse("Login Courier", response);
        return response;
    }

    @Step("Удаление курьера по id: {id}")
    public static Response deleteCourier(int id) {
        Response response = given()
                .delete(BASE_URL + COURIER_PATH + "/" + id)
                .thenReturn();
        ResponsePrinter.printResponse("Delete Courier", response);
        return response;
    }

    @Step("Получение id курьера")
    public static int getCourierId(Courier courier) {
        return loginCourier(courier)
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }
} 