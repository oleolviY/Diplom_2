import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserNegativeTests extends AbstractTest{
    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void duplicateUser(){
        userSteps
                .createUser(user);

        userSteps
                .createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown(){
        String token = userSteps.login(user)
                .extract().body().path("accessToken");
        user.setAccessToken(token);

        if (user.getAccessToken() != null) {
            userSteps.delete(user);
        }
    }
}
