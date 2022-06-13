package stellarburgers.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.client.UserClient;
import stellarburgers.model.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class UserCreationTest  {

    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create unique user")
    public void checkUserCreationPassedTest() {
        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        String accessToken = response.extract().path("accessToken");

        assertTrue("Пользователь не создан", isUserCreated);
        assertThat("Некорректный код статуса", statusCode, equalTo(200));
        assertTrue("Некорректный accessToken в теле ответа", accessToken.startsWith("Bearer"));
    }

    @Test
    @DisplayName("Impossible to create two identical users")
    public void checkCannotCreateIdenticalUsersTest() {
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isIdenticalUserNotCreated = response.extract().path("message").equals("User already exists");

        assertThat("Некорректный код статуса", statusCode, equalTo(403));
        assertTrue("Создано два одинаковых пользователя", isIdenticalUserNotCreated);
    }

}