package steps;

import config.RestConfig;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;

import static config.Constants.AUTH;
import static config.Constants.TOKEN;
import static config.EndPoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class UserSteps extends RestConfig {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .post(CREATE_USER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Получение токена")
    public String getToken(User user) {
        ValidatableResponse loginResponse = given()
                .body(user)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
                .statusCode(SC_OK);

        String token = loginResponse.extract().path(TOKEN);
        if (token == null) {
            throw new IllegalArgumentException("Token is null");
        }
        return token;
    }

    @Step("Изменение почты пользователя с авторизацией")
    public ValidatableResponse changeUserEmail(User user, String newEmail) {
        String token = getToken(user);
        user.setEmail(newEmail);
        return given()
                .header(AUTH, token)
                .body(user)
                .when()
                .patch(USER_DELETE)
                .then();
    }

    @Step("Изменение имени пользователя с авторизацией")
    public ValidatableResponse changeUserName(User user, String newName) {
        String token = getToken(user);
        user.setName(newName);
        return given()
                .header(AUTH, token)
                .body(user)
                .when()
                .patch(USER_DELETE)
                .then();
    }

    @Step("Изменение почты пользователя без авторизации")
    public ValidatableResponse changeUserEmailWithoutAuth(User user, String newEmail) {
        user.setEmail(newEmail);
        return given()
                .body(user)
                .when()
                .patch(USER_DELETE)
                .then();
    }

    @Step("Изменение имени пользователя без авторизации")
    public ValidatableResponse changeUserNameWithoutAuth(User user, String newName) {
        user.setName(newName);
        return given()
                .body(user)
                .when()
                .patch(USER_DELETE)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(User user) {
        return given()
                .header(TOKEN, user.getAccessToken())
                .when()
                .delete(USER_DELETE)
                .then();
    }
}
