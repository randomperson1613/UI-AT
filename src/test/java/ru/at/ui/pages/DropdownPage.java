package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DropdownPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement simpleDropdown = $("#dropdown");
    private final SelenideElement elementsPerPageDropdown = $("#elementsPerPageSelect");
    private final SelenideElement countryDropdown = $("#country");

    @Step("Открыть страницу выпадающих списков")
    public DropdownPage openPage() {
        open("/dropdown");
        return this;
    }

    @Step("Проверить, что страница выпадающих списков открыта")
    public DropdownPage shouldBeOpened() {
        title.shouldHave(text("Dropdown List page"));
        simpleDropdown.shouldBe(visible);
        elementsPerPageDropdown.shouldBe(visible);
        countryDropdown.shouldBe(visible);
        return this;
    }

    @Step("Выбрать значение простого списка [{option}]")
    public DropdownPage selectSimpleOption(String option) {
        simpleDropdown.selectOption(option);
        return this;
    }

    @Step("Выбрать количество элементов на странице [{count}]")
    public DropdownPage selectElementsPerPage(String count) {
        elementsPerPageDropdown.selectOption(count);
        return this;
    }

    @Step("Выбрать страну по коду [{countryCode}]")
    public DropdownPage selectCountryByCode(String countryCode) {
        countryDropdown.selectOptionByValue(countryCode);
        return this;
    }

    @Step("Проверить выбранное значение простого списка [{option}]")
    public DropdownPage shouldHaveSimpleOption(String option) {
        simpleDropdown.getSelectedOption().shouldHave(exactText(option));
        return this;
    }

    @Step("Проверить количество элементов на странице [{count}]")
    public DropdownPage shouldHaveElementsPerPage(String count) {
        elementsPerPageDropdown.shouldHave(value(count));
        return this;
    }

    @Step("Проверить выбранную страну по коду [{countryCode}]")
    public DropdownPage shouldHaveCountryCode(String countryCode) {
        countryDropdown.shouldHave(value(countryCode));
        return this;
    }
}
