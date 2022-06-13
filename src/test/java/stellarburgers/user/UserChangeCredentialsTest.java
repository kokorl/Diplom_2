package stellarburgers.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.client.UserClient;
import stellarburgers.model.User;
import stellarburgers.model.UserCredentials;
import stellarburgers.model.UserCredentialsChange;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserChangeCredentialsTest {

    private User userObj;
    private UserClient userClient;

    @Before
    public void setUp() {
        userObj = User.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(userObj);
    }

    @Test
    @DisplayName("User can change credentials after authorization")
    public void checkAuthUserCredentialsChangeTest() {
        String accessToken = userClient.loginUser(UserCredentials.from(userObj)).extract().path("accessToken");
        accessToken = StringUtils.remove(accessToken, "Bearer ");
        ValidatableResponse response = userClient.changeUser(UserCredentialsChange.changeUserCredentials(), accessToken);
        int statusCode = response.extract().statusCode();
        boolean isChangesSuccess = response.extract().path("success");

        assertThat("Некорректный код статуса", statusCode, equalTo(200));
        assertThat("Удалось авторизоваться пользователем с некорректными данными", isChangesSuccess);
    }

    @Test
    @DisplayName("User can't change credentials without authorization")
    public void checkNotAuthUserCanNotChangeCredentialsTest() {
        ValidatableResponse response = userClient.changeUser(UserCredentialsChange.changeUserCredentials(), "");
        int statusCode = response.extract().statusCode();
        boolean isNotSuccessChanges = response.extract().path("message").equals("You should be authorised");

        assertThat("Некорректный код статуса", statusCode, equalTo(401));
        assertThat("Успешный запрос на изменение данных без авторизации", isNotSuccessChanges);
    }

}