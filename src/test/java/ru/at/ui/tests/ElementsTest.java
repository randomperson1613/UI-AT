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
import ru.at.ui.pages.AddRemoveElementsPage;
import ru.at.ui.pages.CheckboxesPage;
import ru.at.ui.pages.DynamicControlsPage;
import ru.at.ui.pages.TablesPage;

@Epic("UI-тесты ExpandTesting")
@Feature("Интерактивные элементы")
@Story("Изменение DOM и работа с виджетами")
@Tag("ui")
@Owner("kiber-kot")
public class ElementsTest extends BaseTest {

    private final CheckboxesPage checkboxesPage = new CheckboxesPage();
    private final AddRemoveElementsPage addRemoveElementsPage = new AddRemoveElementsPage();
    private final DynamicControlsPage dynamicControlsPage = new DynamicControlsPage();
    private final TablesPage tablesPage = new TablesPage();

    @Test
    @DisplayName("Состояния чекбоксов можно изменить")
    @Severity(SeverityLevel.NORMAL)
    void shouldChangeCheckboxStates() {
        checkboxesPage.openPage()
                .shouldBeOpened()
                .shouldHaveFirstCheckboxSelected(false)
                .shouldHaveSecondCheckboxSelected(true)
                .setFirstCheckbox(true)
                .setSecondCheckbox(false)
                .shouldHaveFirstCheckboxSelected(true)
                .shouldHaveSecondCheckboxSelected(false);
    }

    @Test
    @DisplayName("Динамические элементы можно добавить и удалить")
    @Severity(SeverityLevel.NORMAL)
    void shouldAddAndRemoveElements() {
        addRemoveElementsPage.openPage()
                .shouldBeOpened()
                .shouldHaveGeneratedElements(0)
                .addElements(3)
                .shouldHaveGeneratedElements(3)
                .deleteFirstElement()
                .shouldHaveGeneratedElements(2);
    }

    @Test
    @DisplayName("Динамические контролы изменяются асинхронно")
    @Severity(SeverityLevel.CRITICAL)
    void shouldHandleDynamicControls() {
        dynamicControlsPage.openPage()
                .shouldBeOpened()
                .removeCheckbox()
                .addCheckboxBack()
                .enableInput()
                .setInputValue("Enabled input value")
                .shouldHaveInputValue("Enabled input value");
    }

    @Test
    @DisplayName("Таблица пользователей содержит ожидаемые бизнес-данные")
    @Severity(SeverityLevel.MINOR)
    void shouldReadUsersTableData() {
        tablesPage.openPage()
                .shouldBeOpened()
                .shouldHaveUsersCount(4)
                .shouldHaveUserData("Doe", "Jason", "jdoe@hotmail.com", "$100.00");
    }
}
