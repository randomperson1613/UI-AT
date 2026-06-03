package ru.at.ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FileUploadPage {

    private final SelenideElement title = $("h1");
    private final SelenideElement fileInput = $("#fileInput");
    private final SelenideElement submitButton = $("#fileSubmit");
    private final SelenideElement uploadedFiles = $("#uploaded-files");

    @Step("Открыть страницу загрузки файла")
    public FileUploadPage openPage() {
        open("/upload");
        return this;
    }

    @Step("Проверить, что страница загрузки файла открыта")
    public FileUploadPage shouldBeOpened() {
        title.shouldHave(text("File Uploader page"));
        fileInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Загрузить файл из classpath [{classpathFile}]")
    public FileUploadPage uploadFileFromClasspath(String classpathFile) {
        fileInput.uploadFromClasspath(classpathFile);
        submitButton.click();
        return this;
    }

    @Step("Проверить загруженный файл [{fileName}]")
    public FileUploadPage shouldHaveUploadedFile(String fileName) {
        title.shouldHave(text("File Uploaded!"));
        uploadedFiles.shouldBe(visible).shouldHave(text(fileName));
        return this;
    }
}
