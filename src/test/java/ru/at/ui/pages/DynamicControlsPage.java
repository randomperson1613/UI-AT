package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.at.ui.helpers.BrowserWaits.waitForJavaScriptFunction;
import static ru.at.ui.helpers.BrowserWaits.waitForJQuery;

public class DynamicControlsPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement checkboxWrapper = $("#checkbox");
    private final SelenideElement checkboxButton = $("#checkbox-example button");
    private final SelenideElement input = $("#input-example input");
    private final SelenideElement inputButton = $("#input-example button");
    private final SelenideElement message = $("#message");

    @Step("Открыть страницу динамических контролов")
    public DynamicControlsPage openPage() {
        open("/dynamic-controls");
        return this;
    }

    @Step("Проверить, что страница динамических контролов открыта")
    public DynamicControlsPage shouldBeOpened() {
        title.shouldHave(text("Dynamic Controls"));
        checkboxWrapper.shouldBe(visible);
        checkboxButton.shouldHave(exactText("Remove"));
        input.shouldBe(disabled);
        inputButton.shouldHave(exactText("Enable"));
        waitForJQuery();
        waitForJavaScriptFunction("swapCheckbox");
        waitForJavaScriptFunction("swapInput");
        return this;
    }

    @Step("Асинхронно удалить чекбокс")
    public DynamicControlsPage removeCheckbox() {
        checkboxButton.click();
        message.shouldHave(exactText("It's gone!"), Duration.ofSeconds(6));
        $("#checkbox-example input[type='checkbox']").should(disappear, Duration.ofSeconds(6));
        checkboxButton.shouldHave(exactText("Add"));
        return this;
    }

    @Step("Асинхронно вернуть чекбокс")
    public DynamicControlsPage addCheckboxBack() {
        checkboxButton.click();
        message.shouldHave(exactText("It's back!"), Duration.ofSeconds(6));
        $("#checkbox-example input[type='checkbox']").shouldBe(visible);
        checkboxButton.shouldHave(exactText("Remove"));
        return this;
    }

    @Step("Асинхронно активировать поле ввода")
    public DynamicControlsPage enableInput() {
        inputButton.click();
        message.shouldHave(exactText("It's enabled!"), Duration.ofSeconds(6));
        input.shouldBe(enabled);
        inputButton.shouldHave(exactText("Disable"));
        return this;
    }

    @Step("Ввести значение в динамическое поле [{value}]")
    public DynamicControlsPage setInputValue(String value) {
        input.setValue(value);
        return this;
    }

    @Step("Проверить значение динамического поля [{value}]")
    public DynamicControlsPage shouldHaveInputValue(String value) {
        input.shouldHave(com.codeborne.selenide.Condition.value(value));
        return this;
    }
}
