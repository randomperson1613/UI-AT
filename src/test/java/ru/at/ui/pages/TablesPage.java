package ru.at.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TablesPage {

    private final SelenideElement title = $("h1");
    private final ElementsCollection tableRows = $$("#table2 tbody tr");

    @Step("Открыть страницу таблиц")
    public TablesPage openPage() {
        open("/tables");
        return this;
    }

    @Step("Проверить, что страница таблиц открыта")
    public TablesPage shouldBeOpened() {
        title.shouldHave(text("Data Tables page"));
        tableRows.shouldHave(size(4));
        return this;
    }

    @Step("Проверить количество строк в таблице пользователей [{expectedRows}]")
    public TablesPage shouldHaveUsersCount(int expectedRows) {
        tableRows.shouldHave(size(expectedRows));
        return this;
    }

    @Step("Проверить данные пользователя с фамилией [{lastName}]")
    public TablesPage shouldHaveUserData(String lastName, String firstName, String email, String due) {
        SelenideElement row = rowByLastName(lastName);
        row.$(".last-name").shouldHave(exactText(lastName));
        row.$(".first-name").shouldHave(exactText(firstName));
        row.$(".email").shouldHave(exactText(email));
        row.$(".dues").shouldHave(exactText(due));
        return this;
    }

    private SelenideElement rowByLastName(String lastName) {
        return tableRows.findBy(text(lastName));
    }
}
