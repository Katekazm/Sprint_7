package com.example;

import com.example.api.OrderApi;
import com.example.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

@Epic("API тесты")
@Feature("Заказы")
@Story("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Test with color: {0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()} // без цвета
        });
    }

    @Before
    public void setUp() {
        // Инициализация базового URL перенесена в OrderApi
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка создания заказа с различными цветами")
    public void canCreateOrderWithVariousColors() {
        Order order = new Order(
            "Naruto",
            "Uchiha",
            "Konoha, 142 apt.",
            "4",
            "+7 800 355 35 35",
            5,
            "2025-05-04",
            "Saske, come back!",
            color
        );

        Response response = OrderApi.createOrder(order);
        ResponseValidator.validateResponse("Create Order", response, SC_CREATED);
        ResponseValidator.validateResponseBody(response, "track", notNullValue());
    }
}
