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

import java.util.UUID;

@Epic("UI-тесты ExpandTesting")
@Feature("Авторизация")
@Story("Вход и регистрация пользователя")
@Tag("ui")
public class AuthTest extends BaseTest {

    private final LoginPage loginPage = new LoginPage();
    private final SecurePage securePage = new SecurePage();
    private final RegisterPage registerPage = new RegisterPage();

    @Test
    @DisplayName("Пользователь может войти с валидными учётными данными")
    @Owner("kiber-kot")
    @Severity(SeverityLevel.CRITICAL)
    void shouldLoginWithValidCredentials() {
        loginPage.openPage()
                .shouldBeOpened()
                .login("practice", "SuperSecretPassword!");

        securePage.shouldBeOpened()
                .shouldHaveSuccessfulLoginMessage();
    }

    @Test
    @DisplayName("Пользователь видит ошибку при неверном пароле")
    @Owner("kiber-kot")
    @Severity(SeverityLevel.CRITICAL)
    void shouldShowErrorForInvalidPassword() {
        loginPage.openPage()
                .shouldBeOpened()
                .login("practice", "WrongPassword")
                .shouldHaveErrorMessage("Your password is invalid!");
    }

    @Test
    @DisplayName("Новый пользователь может зарегистрироваться")
    @Owner("kiber-kot")
    @Severity(SeverityLevel.NORMAL)
    void shouldRegisterNewUser() {
        String username = "diploma" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String password = "StrongPassword123!";

        registerPage.openPage()
                .shouldBeOpened()
                .register(username, password, password)
                .shouldHaveSuccessfulRegistrationMessage();

        loginPage.shouldBeOpened();
    }
}
