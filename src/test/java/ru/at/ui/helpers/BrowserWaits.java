package ru.at.ui.helpers;

import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public final class BrowserWaits {

    private static final int MAX_PAGE_LOAD_ATTEMPTS = 3;

    private BrowserWaits() {
    }

    @Step("Дождаться загрузки jQuery")
    public static void waitForJQuery() {
        waitForConditionWithRefresh(
                "return typeof window.jQuery === 'function' && typeof window.$ === 'function';"
        );
    }

    @Step("Дождаться обработчика клика jQuery для селектора [{selector}]")
    public static void waitForJQueryClickHandler(String selector) {
        waitForConditionWithRefresh(
                "const element = document.querySelector(arguments[0]);"
                        + "if (!element || typeof window.jQuery !== 'function' || !window.jQuery._data) return false;"
                        + "const events = window.jQuery._data(element, 'events');"
                        + "return !!(events && events.click && events.click.length > 0);",
                selector
        );
    }

    @Step("Дождаться JavaScript-функции [{functionName}]")
    public static void waitForJavaScriptFunction(String functionName) {
        Wait().until(webDriver -> Boolean.TRUE.equals(
                executeJavaScript("return typeof window[arguments[0]] === 'function';", functionName)
        ));
    }

    private static void waitForConditionWithRefresh(String script, Object... args) {
        TimeoutException lastError = null;

        for (int attempt = 1; attempt <= MAX_PAGE_LOAD_ATTEMPTS; attempt++) {
            try {
                waitForCondition(script, args);
                return;
            } catch (TimeoutException error) {
                lastError = error;
                if (attempt < MAX_PAGE_LOAD_ATTEMPTS) {
                    refresh();
                }
            }
        }

        throw lastError;
    }

    private static void waitForCondition(String script, Object... args) {
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(200))
                .until(webDriver -> Boolean.TRUE.equals(executeJavaScript(script, args)));
    }
}
