package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CheckboxesPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement firstCheckbox = $("#checkbox1");
    private final SelenideElement secondCheckbox = $("#checkbox2");

    @Step("Открыть страницу чекбоксов")
    public CheckboxesPage openPage() {
        open("/checkboxes");
        return this;
    }

    @Step("Проверить, что страница чекбоксов открыта")
    public CheckboxesPage shouldBeOpened() {
        title.shouldHave(text("Sample Checkboxes page"));
        firstCheckbox.shouldBe(visible);
        secondCheckbox.shouldBe(visible);
        return this;
    }

    @Step("Установить состояние первого чекбокса: выбран = [{selected}]")
    public CheckboxesPage setFirstCheckbox(boolean selected) {
        firstCheckbox.setSelected(selected);
        return this;
    }

    @Step("Установить состояние второго чекбокса: выбран = [{selected}]")
    public CheckboxesPage setSecondCheckbox(boolean selected) {
        secondCheckbox.setSelected(selected);
        return this;
    }

    @Step("Проверить состояние первого чекбокса: выбран = [{selected}]")
    public CheckboxesPage shouldHaveFirstCheckboxSelected(boolean selected) {
        shouldHaveCheckedState(firstCheckbox, selected);
        return this;
    }

    @Step("Проверить состояние второго чекбокса: выбран = [{selected}]")
    public CheckboxesPage shouldHaveSecondCheckboxSelected(boolean selected) {
        shouldHaveCheckedState(secondCheckbox, selected);
        return this;
    }

    private void shouldHaveCheckedState(SelenideElement checkbox, boolean selected) {
        if (selected) {
            checkbox.shouldBe(checked);
        } else {
            checkbox.shouldNotBe(checked);
        }
    }
}
