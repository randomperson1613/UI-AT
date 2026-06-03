package ru.at.ui.helpers;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.stream.Collectors;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

public final class AllureAttachments {

    private AllureAttachments() {
    }

    public static void attachScreenshot(String name) {
        if (!hasWebDriverStarted()) {
            return;
        }

        try {
            WebDriver driver = getWebDriver();
            if (driver instanceof TakesScreenshot screenshotDriver) {
                Allure.getLifecycle().addAttachment(
                        name,
                        "image/png",
                        "png",
                        screenshotDriver.getScreenshotAs(OutputType.BYTES)
                );
            }
        } catch (WebDriverException ignored) {
            Allure.addAttachment("Ошибка получения скриншота", "Скриншот недоступен");
        }
    }

    public static void attachPageSource() {
        if (!hasWebDriverStarted()) {
            return;
        }

        try {
            Allure.addAttachment("HTML страницы", "text/html", getWebDriver().getPageSource(), ".html");
        } catch (WebDriverException ignored) {
            Allure.addAttachment("Ошибка получения HTML страницы", "HTML страницы недоступен");
        }
    }

    public static void attachBrowserConsoleLogs() {
        if (!hasWebDriverStarted()) {
            return;
        }

        try {
            String logs = getWebDriver().manage().logs().get(LogType.BROWSER).getAll().stream()
                    .map(AllureAttachments::formatLogEntry)
                    .collect(Collectors.joining(System.lineSeparator()));

            Allure.addAttachment(
                    "Логи браузера",
                    logs.isBlank() ? "Консоль браузера пустая" : logs
            );
        } catch (WebDriverException ignored) {
            Allure.addAttachment("Логи браузера", "Логи браузера недоступны");
        }
    }

    public static void attachSelenoidVideo() {
        if (!hasWebDriverStarted() || Configuration.remote == null || Configuration.remote.isBlank()) {
            return;
        }

        WebDriver driver = getWebDriver();
        if (driver instanceof RemoteWebDriver remoteWebDriver && remoteWebDriver.getSessionId() != null) {
            String videoUrl = videoUrl(remoteWebDriver.getSessionId().toString());
            String html = "<html><body>"
                    + "<video width='100%' height='100%' controls autoplay>"
                    + "<source src='" + videoUrl + "' type='video/mp4'>"
                    + "</video>"
                    + "</body></html>";
            Allure.addAttachment("Видео Selenoid", "text/html", html, ".html");
        }
    }

    private static String formatLogEntry(LogEntry logEntry) {
        return String.format("%s %s %s", logEntry.getLevel(), logEntry.getTimestamp(), logEntry.getMessage());
    }

    private static String videoUrl(String sessionId) {
        String remoteBaseUrl = Configuration.remote.replaceAll("/wd/hub/?$", "");
        return remoteBaseUrl + "/video/" + sessionId + ".mp4";
    }
}
