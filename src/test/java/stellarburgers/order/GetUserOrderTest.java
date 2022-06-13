package stellarburgers.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.client.OrderClient;
import stellarburgers.client.UserClient;
import stellarburgers.model.IngredientsForCreateNewBurger;
import stellarburgers.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GetUserOrderTest {

    private UserClient userClient;
    private User user;
    String accessToken;
    private IngredientsForCreateNewBurger ingredientsForCreateNewBurger;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        //Сгенерировать случайные данные для пользователя
        user = User.getRandomUser();
        orderClient = new OrderClient();
        //Сгенерировать случайный бургер
        ingredientsForCreateNewBurger = IngredientsForCreateNewBurger.getRandomData();

        //Создать пользователя
        ValidatableResponse response = userClient.createUser(user);
        //Получить access токен
        accessToken = response.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);

    }

    @Test
    @DisplayName("Get orders: registered user")
    public void successfulGetOrdersWithLogin() {


        //Получить заказ пользователя
        ValidatableResponse responseOrder = orderClient.getOrderList(accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить подтверждение, что заказ создан
        boolean isGetOrders = responseOrder.extract().path("success");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(200));
        //Проверить успешность получения заказов
        assertTrue(isGetOrders);
    }

    @Test
    @DisplayName("Get orders: unregistered user")
    public void unsuccessfulGetOrdersWithoutLogin() {

        //Получить заказ
        ValidatableResponse responseOrder = orderClient.getOrderList("");

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить подтверждение, что заказ создан
        boolean isGetOrders = responseOrder.extract().path("success");
        //Получить значение ключа "message"
        String message = responseOrder.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(401));
        //Проверить успешность получения заказов
        assertFalse("Операция успешна", isGetOrders);
        //Проверить сообщение
        assertThat(message, equalTo("You should be authorised"));
    }
}
