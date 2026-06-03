package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static ru.at.ui.helpers.BrowserWaits.waitForJQuery;
import static ru.at.ui.helpers.BrowserWaits.waitForJQueryClickHandler;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class InputsPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement numberInput = $("#input-number");
    private final SelenideElement textInput = $("#input-text");
    private final SelenideElement passwordInput = $("#input-password");
    private final SelenideElement dateInput = $("#input-date");
    private final SelenideElement displayButton = $("#btn-display-inputs");
    private final SelenideElement clearButton = $("#btn-clear-inputs");
    private final SelenideElement outputNumber = $("#output-number");
    private final SelenideElement outputText = $("#output-text");
    private final SelenideElement outputPassword = $("#output-password");
    private final SelenideElement outputDate = $("#output-date");

    @Step("Открыть страницу полей ввода")
    public InputsPage openPage() {
        open("/inputs");
        return this;
    }

    @Step("Проверить, что страница полей ввода открыта")
    public InputsPage shouldBeOpened() {
        title.shouldHave(text("Web inputs page"));
        numberInput.shouldBe(visible);
        textInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        dateInput.shouldBe(visible);
        waitForJQuery();
        waitForJQueryClickHandler("#btn-display-inputs");
        waitForJQueryClickHandler("#btn-clear-inputs");
        return this;
    }

    @Step("Заполнить поля ввода")
    public InputsPage fillInputs(String number, String text, String password, String date) {
        numberInput.setValue(number);
        textInput.setValue(text);
        passwordInput.setValue(password);
        executeJavaScript("arguments[0].value = arguments[1];", dateInput, date);
        return this;
    }

    @Step("Показать введённые значения")
    public InputsPage displayInputs() {
        displayButton.click();
        return this;
    }

    @Step("Очистить введённые значения")
    public InputsPage clearInputs() {
        clearButton.click();
        return this;
    }

    @Step("Проверить отображённые значения")
    public InputsPage shouldHaveOutputs(String number, String text, String password, String date) {
        outputNumber.shouldHave(exactText(number));
        outputText.shouldHave(exactText(text));
        outputPassword.shouldHave(exactText(password));
        outputDate.shouldHave(exactText(date));
        return this;
    }

    @Step("Проверить, что поля ввода пустые")
    public InputsPage shouldHaveEmptyInputs() {
        numberInput.shouldHave(exactValue(""));
        textInput.shouldHave(exactValue(""));
        passwordInput.shouldHave(exactValue(""));
        dateInput.shouldHave(exactValue(""));
        return this;
    }
}
