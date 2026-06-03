package ru.at.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;

import static ru.at.ui.helpers.AllureAttachments.attachBrowserConsoleLogs;
import static ru.at.ui.helpers.AllureAttachments.attachPageSource;
import static ru.at.ui.helpers.AllureAttachments.attachScreenshot;
import static ru.at.ui.helpers.AllureAttachments.attachSelenoidVideo;

public abstract class BaseTest {

    @BeforeAll
    static void configure() {
        Configuration.baseUrl = getProperty("baseUrl", "https://practice.expandtesting.com");
        Configuration.browser = getProperty("browser", "chrome");
        Configuration.browserSize = getProperty("browserSize", "1920x1080");
        Configuration.timeout = Long.parseLong(getProperty("timeout", "10000"));
        Configuration.pageLoadTimeout = Long.parseLong(getProperty("pageLoadTimeout", "30000"));
        Configuration.pageLoadStrategy = getProperty("pageLoadStrategy", "normal");
        Configuration.headless = Boolean.parseBoolean(getProperty("headless", "false"));
        Configuration.reportsFolder = getProperty("reportsFolder", "build/selenide/reports");
        Configuration.downloadsFolder = getProperty("downloadsFolder", "build/selenide/downloads");

        String browserVersion = getProperty("browserVersion", "");
        if (!browserVersion.isBlank()) {
            Configuration.browserVersion = browserVersion;
        }

        String remote = getProperty("remote", "");
        if (!remote.isBlank()) {
            Configuration.remote = remote;
            enableSelenoidCapabilities();
        }

        SelenideLogger.removeListener("AllureSelenide");
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .includeSelenideSteps(true)
                .screenshots(false)
                .savePageSource(false));
    }

    @AfterEach
    void addAttachments() {
        attachScreenshot("Скриншот страницы");
        attachPageSource();
        attachBrowserConsoleLogs();
        attachSelenoidVideo();
        Selenide.closeWebDriver();
    }

    private static String getProperty(String key, String defaultValue) {
        return System.getProperty(key, System.getProperty("selenide." + key, defaultValue));
    }

    private static void enableSelenoidCapabilities() {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", true);
        capabilities.setCapability("selenoid:options", selenoidOptions);

        Configuration.browserCapabilities = capabilities;
    }
}
