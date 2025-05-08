package com.example;

import com.example.utils.ResponsePrinter;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API тесты")
@Feature("Заказы")
@Story("Получение списка заказов")
public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка получения списка заказов")
    public void getOrderListReturnsListOfOrders() {
        Response response = getOrders();
        ResponsePrinter.validateResponse("Get Order List", response, 200);
        response.then()
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }

    @Step("Получение списка заказов")
    private Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }
}
