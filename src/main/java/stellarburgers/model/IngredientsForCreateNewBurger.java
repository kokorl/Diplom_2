package stellarburgers.model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.client.StellarburgersBaseClient;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class IngredientsForCreateNewBurger extends StellarburgersBaseClient {
    private static final String INGREDIENTS_PATH = "/api/ingredients";
    public ArrayList<Object> ingredients;
    public static Faker faker = new Faker();


    public IngredientsForCreateNewBurger(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    @Step("Get information about ingredients: send GET to api/ingredients")
    public static IngredientsForCreateNewBurger getRandomData(){

        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(200);

        ArrayList<Object> ingredients = new ArrayList<>();

        List<Object> bunIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'bun'}._id");
        List<Object> mainIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'main'}._id");
        List<Object> sauceIngredients = response.extract().jsonPath().getList("data.findAll{it.type == 'sauce'}._id");

        int bunIndex = nextInt(0,bunIngredients.size());
        int mainIndex = nextInt(0,mainIngredients.size());
        int sauceIndex = nextInt(0,sauceIngredients.size());

        ingredients.add(bunIngredients.get(bunIndex));
        ingredients.add(mainIngredients.get(mainIndex));
        ingredients.add(sauceIngredients.get(sauceIndex));

        return new IngredientsForCreateNewBurger(ingredients);
    }

    public static IngredientsForCreateNewBurger getWithoutIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        return new IngredientsForCreateNewBurger(ingredients);
    }

    public static IngredientsForCreateNewBurger getNotRealIngredients() {
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        ingredients.add(faker.internet().uuid());
        return new IngredientsForCreateNewBurger(ingredients);
    }
}
