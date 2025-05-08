package com.example;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.example.utils.ResponsePrinter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API тесты")
@Feature("Курьеры")
@Story("Создание курьера")
public class CourierCreateTest {
    
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private int courierId;
    private final Courier courier = Courier.random();

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUrl;
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешного создания курьера")
    public void canCreateCourierSuccessfully() {
        Response response = createCourier(courier);
        ResponsePrinter.validateResponse("Create Courier Successfully", response, 201);
        response.then().body("ok", is(true));
        courierId = loginCourier(courier);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка невозможности создания курьера с существующим логином")
    public void cannotCreateSameCourierTwice() {
        Response firstResponse = createCourier(courier);
        ResponsePrinter.validateResponse("Create Courier First Attempt", firstResponse, 201);

        Response secondResponse = createCourier(courier);
        ResponsePrinter.validateResponse("Create Courier Second Attempt", secondResponse, 409);
        secondResponse.then()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка создания курьера без пароля")
    public void createCourierWithoutPasswordFails() {
        Courier noPassword = new Courier(courier.login, null, courier.firstName);
        Response response = createCourier(noPassword);
        ResponsePrinter.validateResponse("Create Courier Without Password", response, 400);
        response.then()
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Step("Создание курьера: {courier}")
    private Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин под курьером")
    private int loginCourier(Courier courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        ResponsePrinter.printResponse("Login Courier", response);
        return response.then()
                .statusCode(200)
                .extract().path("id");
    }

    @Step("Удаление курьера по id")
    private void deleteCourier(int id) {
        Response response = given()
                .delete("/api/v1/courier/" + id)
                .thenReturn();
        ResponsePrinter.printResponse("Delete Courier", response);
        response.then().statusCode(200);
    }
}
