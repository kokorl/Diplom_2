package stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.model.User;
import stellarburgers.model.UserCredentials;
import stellarburgers.model.UserCredentialsChange;

import static io.restassured.RestAssured.given;

public class UserClient extends StellarburgersBaseClient{
    final static private String USER_PATH = "api/auth";

    @Step("Create a new user: send POST to /api/auth/register")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("Login your user: send POST to /api/auth/login")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(USER_PATH + "/login")
                .then();
    }

    @Step("Login your user: sent DELETE to /api/auth/user")
    public void deleteUser(String accessToken) {

            given()
                    .header("Authorization", accessToken)
                    .spec(getBaseSpec())
                    .when()
                    .delete(USER_PATH + "/user")
                    .then()
                    .statusCode(202);
    }

    @Step("Change user's credentials: send PATCH to /user")
    public ValidatableResponse changeUser(UserCredentialsChange credentialsChange, String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(credentialsChange)
                .when()
                .patch(USER_PATH + "/user")
                .then();
    }

}
