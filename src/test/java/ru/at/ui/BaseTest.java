package ru.at.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
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

        MutableCapabilities browserCapabilities = configureBrowserCapabilities();

        String remote = getProperty("remote", "");
        if (!remote.isBlank()) {
            Configuration.remote = withBasicAuth(remote, getProperty("remoteUser", ""), getProperty("remotePassword", ""));
            enableSelenoidCapabilities(browserCapabilities);
        }

        if (!browserCapabilities.asMap().isEmpty()) {
            Configuration.browserCapabilities = browserCapabilities;
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
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }

        value = System.getProperty("selenide." + key);
        if (value != null) {
            return value;
        }

        value = System.getenv(toEnvironmentVariableName(key));
        return value == null ? defaultValue : value;
    }

    private static String toEnvironmentVariableName(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.ROOT);
    }

    private static MutableCapabilities configureBrowserCapabilities() {
        if (!"chrome".equalsIgnoreCase(Configuration.browser)) {
            return new MutableCapabilities();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");

        if (Boolean.parseBoolean(getProperty("blockAds", "true"))) {
            options.addArguments("--host-resolver-rules="
                    + "MAP *.doubleclick.net 0.0.0.0,"
                    + "MAP *.googlesyndication.com 0.0.0.0,"
                    + "MAP *.googleadservices.com 0.0.0.0,"
                    + "MAP googleads.g.doubleclick.net 0.0.0.0,"
                    + "MAP adservice.google.com 0.0.0.0,"
                    + "EXCLUDE localhost");
        }

        if (Boolean.parseBoolean(getProperty("chromeNoSandbox", "false"))) {
            options.addArguments("--no-sandbox");
        }

        return options;
    }

    private static void enableSelenoidCapabilities(MutableCapabilities capabilities) {
        boolean enableVnc = Boolean.parseBoolean(getProperty("enableVnc", "true"));
        boolean enableVideo = Boolean.parseBoolean(getProperty("enableVideo", "true"));

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", enableVnc);
        selenoidOptions.put("enableVideo", enableVideo);

        String sessionName = getProperty("sessionName", "");
        if (!sessionName.isBlank()) {
            selenoidOptions.put("name", sessionName);
        }

        capabilities.setCapability("selenoid:options", selenoidOptions);
    }

    private static String withBasicAuth(String remote, String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            return remote;
        }

        try {
            URI uri = new URI(remote);
            if (uri.getUserInfo() != null || uri.getHost() == null || uri.getScheme() == null) {
                return remote;
            }

            String path = uri.getPath();
            if (path == null || path.isBlank()) {
                path = "/";
            }

            return new URI(
                    uri.getScheme(),
                    username + ":" + password,
                    uri.getHost(),
                    uri.getPort(),
                    path,
                    uri.getQuery(),
                    uri.getFragment()
            ).toString();
        } catch (URISyntaxException | IllegalArgumentException ignored) {
            return remote;
        }
    }
}
