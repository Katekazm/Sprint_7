package com.example.api;

import com.example.Order;
import com.example.utils.ResponsePrinter;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа: {order}")
    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(BASE_URL + ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public static Response getOrderList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(BASE_URL + ORDER_PATH);
    }

    @Step("Получение заказа по трек-номеру: {track}")
    public static Response getOrderByTrack(int track) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("t", track)
                .when()
                .get(BASE_URL + ORDER_PATH + "/track");
    }

    @Step("Принятие заказа курьером")
    public static Response acceptOrder(int courierId, int orderId) {
        return given()
                .header("Content-type", "application/json")
                .put(BASE_URL + ORDER_PATH + "/accept/" + orderId + "?courierId=" + courierId);
    }

    @Step("Завершение заказа")
    public static Response completeOrder(int orderId) {
        return given()
                .header("Content-type", "application/json")
                .put(BASE_URL + ORDER_PATH + "/finish/" + orderId);
    }

    @Step("Отмена заказа")
    public static Response cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .put(BASE_URL + ORDER_PATH + "/cancel?track=" + track);
    }
} 