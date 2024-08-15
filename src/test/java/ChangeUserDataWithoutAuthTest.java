import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataWithAuthTest extends AbstractTest{
    private UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){
        user = new User();
        user.setName(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10));
        user.setPassword(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10));
        user.setEmail(org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");

//        String newPassword = RandomStringUtils.randomAlphabetic(10);
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    public void ChangeUserEmail(){
        userSteps
                .createUser(user);
        String newEmail = (RandomStringUtils.randomAlphabetic(10) + "@yandex.ru").toLowerCase();
        userSteps
                .changeUserEmail(user, newEmail)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.email", equalTo(newEmail));
    }
    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void ChangeUserName(){
        userSteps
                .createUser(user);
        String newName = RandomStringUtils.randomAlphabetic(10);
        userSteps
                .changeUserName(user, newName)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.name", equalTo(newName));
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
