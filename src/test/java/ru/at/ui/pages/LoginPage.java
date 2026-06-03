package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitButton = $("#submit-login");
    private final SelenideElement flashMessage = $("#flash");

    @Step("Открыть страницу входа")
    public LoginPage openPage() {
        open("/login");
        return this;
    }

    @Step("Проверить, что страница входа открыта")
    public LoginPage shouldBeOpened() {
        title.shouldHave(text("Test Login page"));
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Войти как пользователь [{username}]")
    public LoginPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    @Step("Проверить сообщение об ошибке входа [{message}]")
    public LoginPage shouldHaveErrorMessage(String message) {
        flashMessage.shouldBe(visible).shouldHave(text(message));
        return this;
    }
}
