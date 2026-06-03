package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RegisterPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement confirmPasswordInput = $("#confirmPassword");
    private final SelenideElement submitButton = $("#register button[type='submit']");
    private final SelenideElement flashMessage = $("#flash");

    @Step("Открыть страницу регистрации")
    public RegisterPage openPage() {
        open("/register");
        return this;
    }

    @Step("Проверить, что страница регистрации открыта")
    public RegisterPage shouldBeOpened() {
        title.shouldHave(text("Test Register page"));
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        confirmPasswordInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Зарегистрировать пользователя [{username}]")
    public RegisterPage register(String username, String password, String confirmPassword) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(confirmPassword);
        submitButton.click();
        return this;
    }

    @Step("Проверить сообщение об успешной регистрации")
    public RegisterPage shouldHaveSuccessfulRegistrationMessage() {
        flashMessage.shouldBe(visible).shouldHave(text("Successfully registered, you can log in now."));
        return this;
    }
}
