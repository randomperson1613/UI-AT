package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class SecurePage {

    private final SelenideElement flashMessage = $("#flash");

    @Step("Проверить, что защищённая страница открыта")
    public SecurePage shouldBeOpened() {
        flashMessage.shouldBe(visible);
        Assertions.assertTrue(url().contains("/secure"), "Ожидалось, что текущий URL содержит /secure");
        return this;
    }

    @Step("Проверить сообщение об успешном входе")
    public SecurePage shouldHaveSuccessfulLoginMessage() {
        flashMessage.shouldHave(text("You logged into a secure area!"));
        return this;
    }
}
