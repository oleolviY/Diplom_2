import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.Collections;
import java.util.List;

import static config.Constants.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest extends AbstractTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final UserSteps userSteps = new UserSteps();
    private User user;
    private String token;
    private List<String> ingredients;

    @Before
    public void setUp() {
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.createUser(user);
        token = userSteps.getToken(user);
        ingredients = orderSteps.getIngredients();
        Collections.shuffle(ingredients);
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией и с ингредиентами")
    public void createOrderWithAuthAndIngredient() {
        orderSteps
                .createOrderWithAuth(token, ingredients.subList(0, 2))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuth() {
        orderSteps
                .createOrderWithoutAuth(ingredients.subList(0, 2))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    public void createOrderWithoutIngredient() {
        orderSteps
                .createOrderWithAuth(token, null)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo(NOT_INGREDIENTS));
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredient() {
        Collections.replaceAll(ingredients, ingredients.get(0), INCORRECT_HASH);
        orderSteps
                .createOrderWithAuth(token, ingredients.subList(0, 2))
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        String token = userSteps.login(user)
                .extract().body().path(TOKEN);
        user.setAccessToken(token);

        if (user.getAccessToken() != null) {
            userSteps.delete(user);
        }
    }
}
