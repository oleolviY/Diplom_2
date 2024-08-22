import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static config.Constants.NOT_AUTH;
import static config.Constants.TOKEN;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataWithoutAuthTest extends AbstractTest {
    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setName(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10));
        user.setPassword(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10));
        user.setEmail(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
    }

    @Test
    @DisplayName("Изменение почты пользователя без авторизации")
    public void editUsersEmailWithoutAuth() {
        String newEmail = (RandomStringUtils.randomAlphabetic(10) + "@yandex.ru").toLowerCase();
        userSteps
                .changeUserEmailWithoutAuth(user, newEmail)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo(NOT_AUTH));
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void editUsersNameWithoutAuth() {
        String newName = RandomStringUtils.randomAlphabetic(8);
        userSteps
                .changeUserNameWithoutAuth(user, newName)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
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
