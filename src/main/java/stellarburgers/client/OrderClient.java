package stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.model.IngredientsForCreateNewBurger;

import static io.restassured.RestAssured.given;

public class OrderClient extends StellarburgersBaseClient {
    private static final String ORDER_PATH = "api/orders";

    @Step("Create an order: send POST request to /api/orders")
    public ValidatableResponse createOrder(IngredientsForCreateNewBurger ingredientsForCreateNewBurger, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(ingredientsForCreateNewBurger)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Get list of user's orders: send GET to /api/orders")
    public ValidatableResponse getOrderList(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
