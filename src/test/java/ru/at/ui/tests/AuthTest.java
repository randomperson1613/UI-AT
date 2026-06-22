package ru.at.ui.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.at.ui.BaseTest;
import ru.at.ui.pages.LoginPage;
import ru.at.ui.pages.RegisterPage;
import ru.at.ui.pages.SecurePage;

import static ru.at.ui.data.TestData.INVALID_LOGIN_PASSWORD;
import static ru.at.ui.data.TestData.LOGIN_PASSWORD;
import static ru.at.ui.data.TestData.LOGIN_USERNAME;
import static ru.at.ui.data.TestData.REGISTRATION_PASSWORD;
import static ru.at.ui.data.TestData.randomRegistrationUsername;

@Epic("UI-тесты ExpandTesting")
@Feature("Авторизация")
@Story("Вход и регистрация пользователя")
@Tag("ui")
@Owner("kiber-kot")
public class AuthTest extends BaseTest {

    private final LoginPage loginPage = new LoginPage();
    private final SecurePage securePage = new SecurePage();
    private final RegisterPage registerPage = new RegisterPage();

    @Test
    @DisplayName("Пользователь может войти с валидными учётными данными")
    @Severity(SeverityLevel.CRITICAL)
    void shouldLoginWithValidCredentials() {
        loginPage.openPage()
                .shouldBeOpened()
                .login(LOGIN_USERNAME, LOGIN_PASSWORD);

        securePage.shouldBeOpened()
                .shouldHaveSuccessfulLoginMessage();
    }

    @Test
    @DisplayName("Пользователь видит ошибку при неверном пароле")
    @Severity(SeverityLevel.CRITICAL)
    void shouldShowErrorForInvalidPassword() {
        loginPage.openPage()
                .shouldBeOpened()
                .login(LOGIN_USERNAME, INVALID_LOGIN_PASSWORD)
                .shouldHaveErrorMessage("Your password is invalid!");
    }

    @Test
    @DisplayName("Новый пользователь может зарегистрироваться")
    @Severity(SeverityLevel.NORMAL)
    void shouldRegisterNewUser() {
        String username = randomRegistrationUsername();

        registerPage.openPage()
                .shouldBeOpened()
                .register(username, REGISTRATION_PASSWORD, REGISTRATION_PASSWORD)
                .shouldHaveSuccessfulRegistrationMessage();

        loginPage.shouldBeOpened();
    }
}
