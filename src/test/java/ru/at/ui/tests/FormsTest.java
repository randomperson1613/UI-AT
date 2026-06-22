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
import ru.at.ui.pages.DropdownPage;
import ru.at.ui.pages.FileUploadPage;
import ru.at.ui.pages.InputsPage;

import static ru.at.ui.data.TestData.UPLOAD_FILE_NAME;
import static ru.at.ui.data.TestData.UPLOAD_FIXTURE;

@Epic("UI-тесты ExpandTesting")
@Feature("Формы и ввод данных")
@Story("Обработка пользовательских данных")
@Tag("ui")
@Owner("kiber-kot")
public class FormsTest extends BaseTest {

    private final InputsPage inputsPage = new InputsPage();
    private final DropdownPage dropdownPage = new DropdownPage();
    private final FileUploadPage fileUploadPage = new FileUploadPage();

    @Test
    @DisplayName("Введённые значения отображаются в блоке результата")
    @Severity(SeverityLevel.NORMAL)
    void shouldDisplayInputValues() {
        inputsPage.openPage()
                .shouldBeOpened()
                .fillInputs("42", "Diploma UI test", "Secret123!", "2026-06-03")
                .displayInputs()
                .shouldHaveOutputs("42", "Diploma UI test", "Secret123!", "2026-06-03");
    }

    @Test
    @DisplayName("Введённые значения можно очистить")
    @Severity(SeverityLevel.NORMAL)
    void shouldClearInputValues() {
        inputsPage.openPage()
                .shouldBeOpened()
                .fillInputs("2026", "Text before clear", "PasswordBeforeClear", "2026-06-03")
                .clearInputs()
                .shouldHaveEmptyInputs();
    }

    @Test
    @DisplayName("Значения в выпадающих списках можно выбрать")
    @Severity(SeverityLevel.NORMAL)
    void shouldSelectDropdownValues() {
        dropdownPage.openPage()
                .shouldBeOpened()
                .selectSimpleOption("Option 2")
                .selectElementsPerPage("50")
                .selectCountryByCode("RU")
                .shouldHaveSimpleOption("Option 2")
                .shouldHaveElementsPerPage("50")
                .shouldHaveCountryCode("RU");
    }

    @Test
    @DisplayName("Файл небольшого размера можно загрузить")
    @Severity(SeverityLevel.CRITICAL)
    void shouldUploadSmallFile() {
        fileUploadPage.openPage()
                .shouldBeOpened()
                .uploadFileFromClasspath(UPLOAD_FIXTURE)
                .shouldHaveUploadedFile(UPLOAD_FILE_NAME);
    }
}
