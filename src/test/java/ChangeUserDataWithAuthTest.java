import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataWithAutthTest {
    private String email;
    private String name;
    private String password;
    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){
        user = new User();
        userSteps.createUser(user);
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    public void ChangeUserEmail(){
        String newEmail = (RandomStringUtils.randomAlphabetic(10) + "@yandex.ru").toLowerCase();
        user.setEmail(newEmail);
        userSteps
                .changeUserData(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.email", equalTo(newEmail));
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
