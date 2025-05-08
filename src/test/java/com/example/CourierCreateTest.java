package com.example;

import com.example.api.CourierApi;
import com.example.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("API тесты")
@Feature("Курьеры")
@Story("Создание курьера")
public class CourierCreateTest {
    
    private int courierId;
    private final Courier courier = Courier.random();

    @Before
    public void setUp() {
        // Инициализация базового URL перенесена в CourierApi
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
    @Description("Проверка успешного создания курьера")
    public void canCreateCourierSuccessfully() {
        Response response = CourierApi.createCourier(courier);
        ResponseValidator.validateResponse("Create Courier Successfully", response, SC_CREATED);
        ResponseValidator.validateResponseBody(response, "ok", is(true));
        courierId = CourierApi.getCourierId(courier);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка невозможности создания курьера с существующим логином")
    public void cannotCreateSameCourierTwice() {
        Response firstResponse = CourierApi.createCourier(courier);
        ResponseValidator.validateResponse("Create Courier First Attempt", firstResponse, SC_CREATED);

        Response secondResponse = CourierApi.createCourier(courier);
        ResponseValidator.validateResponse("Create Courier Second Attempt", secondResponse, SC_CONFLICT);
        ResponseValidator.validateResponseMessage(secondResponse, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка создания курьера без пароля")
    public void createCourierWithoutPasswordFails() {
        Courier noPassword = new Courier(courier.getLogin(), null, courier.getFirstName());
        Response response = CourierApi.createCourier(noPassword);
        ResponseValidator.validateResponse("Create Courier Without Password", response, SC_BAD_REQUEST);
        ResponseValidator.validateResponseMessage(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка создания курьера без логина")
    public void createCourierWithoutLoginFails() {
        Courier noLogin = new Courier(null, courier.getPassword(), courier.getFirstName());
        Response response = CourierApi.createCourier(noLogin);
        ResponseValidator.validateResponse("Create Courier Without Login", response, SC_BAD_REQUEST);
        ResponseValidator.validateResponseMessage(response, "Недостаточно данных для создания учетной записи");
    }
}
