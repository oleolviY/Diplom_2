import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static config.Constants.TOKEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest extends AbstractTest {
    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.createUser(user);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginUser() {
        userSteps
                .login(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
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
