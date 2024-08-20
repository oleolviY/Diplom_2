import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.UserSteps;

import static config.Constants.LOGIN_INCORRECT_DATA;
import static config.Constants.TOKEN;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class LoginNegativeParamTest extends AbstractTest {
    private String name;
    private String password;
    private UserSteps userSteps = new UserSteps();
    private User user;

    public LoginNegativeParamTest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {null, RandomStringUtils.randomAlphabetic(10)},
                {RandomStringUtils.randomAlphabetic(10), null},
        };
    }

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void LoginUserWithoutField() {
        userSteps
                .login(user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo(LOGIN_INCORRECT_DATA));
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
