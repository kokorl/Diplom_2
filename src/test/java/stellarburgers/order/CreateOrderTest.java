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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class CreateOrderTest {
    private UserClient userClient;
    private User user;
    String accessToken;
    private IngredientsForCreateNewBurger ingredientsForCreateNewBurger;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        //Сгенерировать случайные данные для клиента
        user = User.getRandomUser();
        orderClient = new OrderClient();
        //Сгенерировать случайный бургер
        ingredientsForCreateNewBurger = IngredientsForCreateNewBurger.getRandomData();
    }

    @After
    public void tearDown() {
        //Удалить пользователя
            userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create order: with authorization/ with ingredients")
    public void successfulCreateOrderWithIngredientsAndWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.createUser(user);

        //Получить токен для авторизации
        accessToken = response.extract().path("accessToken");

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(ingredientsForCreateNewBurger, accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        // Получить подтверджение, что заказ создан успешно
        boolean isOrderCreated = responseOrder.extract().path("success");
        // Получить номер созданого заказа
        int orderNumber = responseOrder.extract().path("order.number");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(200));
        //Проверить создание заказа
        assertTrue("Order is not created", isOrderCreated);
        //Проверить номер созданного заказа
        assertNotNull("Пустой номе заказа", orderNumber);
    }
    @Test
    @DisplayName("Create order: fake hash of ingredients")
    public void unsuccessfulCreateOrderWithNotReallyIngredientsAndWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.createUser(user);

        //Получить токен для авторизации
        accessToken = response.extract().path("accessToken");

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(IngredientsForCreateNewBurger.getNotRealIngredients(), accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(500));
    }

    @Test
    @DisplayName("Create order: without ingredients")
    public void unsuccessfulCreateOrderWithoutIngredientsAndWithLogin() {

        //Создать пользователя
        ValidatableResponse response = userClient.createUser(user);

        //Получить токен для авторизации
        accessToken = response.extract().path("accessToken");

        //Создать заказ
        ValidatableResponse responseOrder = orderClient.createOrder(IngredientsForCreateNewBurger.getWithoutIngredients(), accessToken);

        //Получить статус кода запроса
        int statusCodeResponseOrder = responseOrder.extract().statusCode();
        //Получить подтверждение, что заказ создан успешно
        boolean isOrderCreated = responseOrder.extract().path("success");
        // Получение номера ключа "message"
        String orderMessage = responseOrder.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeResponseOrder, equalTo(400));
        //Проверить создание заказа
        assertFalse("Order is created", isOrderCreated);
        //Проверить текст
        assertThat(orderMessage, equalTo("Ingredient ids must be provided"));
    }
}
