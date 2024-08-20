import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.UserSteps;

import static config.Constants.ACCOUNT_NOT_ENOUGH_DATA;
import static config.Constants.TOKEN;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateUserNegativeParamTest extends AbstractTest {
    private String email;
    private String name;
    private String password;
    private UserSteps userSteps = new UserSteps();
    private User user;

    public CreateUserNegativeParamTest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {null, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", null, RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", RandomStringUtils.randomAlphabetic(10), null},
        };
    }

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Создание пользователя без одного из обязательных полей")
    public void CreateUserWithoutField() {
        userSteps
                .createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo(ACCOUNT_NOT_ENOUGH_DATA));
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
