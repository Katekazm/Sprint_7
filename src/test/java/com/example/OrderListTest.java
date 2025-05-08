package com.example;

import com.example.api.OrderApi;
import com.example.utils.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

@Epic("API тесты")
@Feature("Заказы")
@Story("Получение списка заказов")
public class OrderListTest {

    @Before
    public void setUp() {
        // Инициализация базового URL перенесена в OrderApi
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка получения списка заказов")
    public void getOrderListReturnsListOfOrders() {
        Response response = OrderApi.getOrderList();
        ResponseValidator.validateResponse("Get Order List", response, 200);
        ResponseValidator.validateResponseBody(response, "orders", notNullValue());
        ResponseValidator.validateResponseBody(response, "orders.size()", greaterThan(0));
    }
}
