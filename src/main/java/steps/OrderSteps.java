package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

import java.util.List;

import static config.Constants.AUTH;
import static config.EndPoints.INGREDIENTS;
import static config.EndPoints.ORDER;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class OrderSteps extends UserSteps {
    @Step("Создание заказа с авторизацией и ингредиентами")
    public ValidatableResponse createOrderWithAuth(String token, List<String> ingredients) {
        return given()
                .header(AUTH, token)
                .body(new Order(ingredients))
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(List<String> ingredients) {
        return given()
                .body(new Order(ingredients))
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Получение данных об ингредиентах")
    public List<String> getIngredients() {
        return given()
                .when()
                .get(INGREDIENTS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("data._id");
    }

    @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getOrdersAuth(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(ORDER)
                .then();
    }

    @Step("Получение заказов неавторизованного пользователя")
    public ValidatableResponse getOrdersWithoutAuth() {
        return given()
                .when()
                .get(ORDER)
                .then();
    }
}
