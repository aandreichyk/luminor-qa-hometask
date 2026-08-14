package com.luminor.hometask.tests.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public abstract class BaseUiTest {

    private static final String ALLURE_SELENIDE_LISTENER = "AllureSelenide";

    protected static final String BASE_URL = System.getProperty("baseUrl", "https://luminor.lv/en");

    private static final String DEFAULT_HEADLESS_BROWSER_SIZE = "1920x1080";

    @BeforeAll
    static void setUpSelenide() {
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        String customBrowserSize = System.getProperty("browserSize");
        String browserSize = (customBrowserSize != null && !customBrowserSize.isBlank())
                ? customBrowserSize
                : DEFAULT_HEADLESS_BROWSER_SIZE;

        Configuration.browserSize = browserSize;
        Configuration.timeout = Long.parseLong(System.getProperty("timeout", "10000"));

        Configuration.browserCapabilities = getChromeOptions(browserSize);

        if (!SelenideLogger.hasListener(ALLURE_SELENIDE_LISTENER)) {
            SelenideLogger.addListener(ALLURE_SELENIDE_LISTENER,
                    new AllureSelenide().screenshots(true).savePageSource(true));
        }
    }

    private static ChromeOptions getChromeOptions(String browserSize) {
        ChromeOptions options = new ChromeOptions();
        String windowSizeArg = browserSize.replace('x', ',');

        if (Configuration.headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=" + windowSizeArg);
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }

        return options;
    }

    @AfterEach
    void closeBrowser() {
        closeWebDriver();
    }
}