package com.example;

import com.example.api.CourierApi;
import com.example.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.*;

@Epic("API тесты")
@Feature("Курьеры")
@Story("Авторизация курьера")
public class CourierLoginTest {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courier = Courier.random();
        CourierApi.createCourier(courier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            ResponseValidator.validateResponse(
                "Delete Courier",
                CourierApi.deleteCourier(courierId),
                SC_OK
            );
        }
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка успешной авторизации курьера")
    public void courierCanLoginSuccessfully() {
        courierId = CourierApi.getCourierId(courier);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации без пароля")
    public void loginFailsWithoutPassword() {
        Map<String, Object> request = new HashMap<>();
        request.put("login", courier.getLogin()); // без пароля

        Response response = CourierApi.loginCourier(new Courier(courier.getLogin(), null, courier.getFirstName()));
        ResponseValidator.validateResponse("Login Without Password", response, SC_BAD_REQUEST);
        ResponseValidator.validateResponseMessage(response, "Недостаточно данных для входа");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации без логина")
    public void loginFailsWithoutLogin() {
        Map<String, Object> request = new HashMap<>();
        request.put("password", courier.getPassword()); // без логина

        Response response = CourierApi.loginCourier(new Courier(null, courier.getPassword(), courier.getFirstName()));
        ResponseValidator.validateResponse("Login Without Login", response, SC_BAD_REQUEST);
        ResponseValidator.validateResponseMessage(response, "Недостаточно данных для входа");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации с неверным паролем")
    public void loginFailsWithWrongPassword() {
        Courier wrongPass = new Courier(courier.getLogin(), "wrong123", courier.getFirstName());
        Response response = CourierApi.loginCourier(wrongPass);
        ResponseValidator.validateResponse("Login With Wrong Password", response, SC_NOT_FOUND);
        ResponseValidator.validateResponseMessage(response, "Учетная запись не найдена");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка авторизации несуществующего пользователя")
    public void loginFailsWithNonExistentUser() {
        Courier ghost = new Courier("ghostUser", "1234", "Nobody");
        Response response = CourierApi.loginCourier(ghost);
        ResponseValidator.validateResponse("Login Non-existent User", response, SC_NOT_FOUND);
        ResponseValidator.validateResponseMessage(response, "Учетная запись не найдена");
    }
}

