import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import static config.Constants.NOT_AUTH;
import static config.Constants.TOKEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderTest extends AbstractTest {
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private User user;
    private String token;

    @Before
    public void setUp() {
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.createUser(user);
        token = userSteps.getToken(user);
    }

    @Test
    @DisplayName("Проверка получения заказов авторизованного пользователя")
    public void getOrdersAuth() {
        ValidatableResponse response = orderSteps.getOrdersAuth(token);
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка получения заказов не авторизованного пользователя")
    public void getOrderWithoutAuth() {
        ValidatableResponse response = orderSteps.getOrdersWithoutAuth();
        response
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(NOT_AUTH));
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
