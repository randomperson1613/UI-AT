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
import static ru.at.ui.helpers.BrowserWaits.waitForJavaScriptFunction;
import static ru.at.ui.helpers.BrowserWaits.waitForJQuery;

public class AddRemoveElementsPage {

    private final SelenideElement title = $("h1");
    private final ElementsCollection deleteButtons = $$(".added-manually");

    @Step("Открыть страницу добавления и удаления элементов")
    public AddRemoveElementsPage openPage() {
        open("/add-remove-elements");
        return this;
    }

    @Step("Проверить, что страница добавления и удаления элементов открыта")
    public AddRemoveElementsPage shouldBeOpened() {
        title.shouldHave(text("Add/Remove Elements page"));
        addButton().shouldHave(exactText("Add Element"));
        waitForJQuery();
        waitForJavaScriptFunction("addElement");
        return this;
    }

    @Step("Добавить элементы: количество [{count}]")
    public AddRemoveElementsPage addElements(int count) {
        for (int i = 0; i < count; i++) {
            addButton().click();
        }
        return this;
    }

    @Step("Удалить первый сгенерированный элемент")
    public AddRemoveElementsPage deleteFirstElement() {
        deleteButtons.first().click();
        return this;
    }

    @Step("Проверить количество сгенерированных элементов [{count}]")
    public AddRemoveElementsPage shouldHaveGeneratedElements(int count) {
        deleteButtons.shouldHave(size(count));
        return this;
    }

    private SelenideElement addButton() {
        return $$("button").findBy(exactText("Add Element"));
    }
}
