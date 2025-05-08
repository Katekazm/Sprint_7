package com.example;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.example.utils.ResponsePrinter;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API тесты")
@Feature("Курьеры")
@Story("Авторизация курьера")
public class CourierLoginTest {

    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUrl;
        courier = Courier.random();
        createCourier(courier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешной авторизации курьера")
    public void courierCanLoginSuccessfully() {
        courierId = loginCourier(courier, 200)
                .then().statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации без пароля")
    public void loginFailsWithoutPassword() {
        Map<String, Object> request = new HashMap<>();
        request.put("login", courier.login); // без пароля

        given()
                .header("Content-type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации без логина")
    public void loginFailsWithoutLogin() {
        Map<String, Object> request = new HashMap<>();
        request.put("password", courier.password); // без логина

        given()
                .header("Content-type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации с неверным паролем")
    public void loginFailsWithWrongPassword() {
        Courier wrongPass = new Courier(courier.login, "wrong123", courier.firstName);
        loginCourier(wrongPass, 404).then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации несуществующего пользователя")
    public void loginFailsWithNonExistentUser() {
        Courier ghost = new Courier("ghostUser", "1234", "Nobody");
        loginCourier(ghost, 404).then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Создание курьера")
    private void createCourier(Courier courier) {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Step("Логин курьера")
    private Response loginCourier(Courier courier, int expectedStatusCode) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        ResponsePrinter.validateResponse("Courier Login", response, expectedStatusCode);
        return response;
    }

    @Step("Удаление курьера по id")
    private void deleteCourier(int id) {
        Response response = given()
                .delete("/api/v1/courier/" + id)
                .thenReturn();
        ResponsePrinter.validateResponse("Delete Courier", response, 200);
    }
}

