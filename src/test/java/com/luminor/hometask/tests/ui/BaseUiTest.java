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

    @BeforeAll
    static void setUpSelenide() {
        String customBrowserSize = System.getProperty("browserSize");

        if (customBrowserSize != null && !customBrowserSize.isBlank()) {
            Configuration.browserSize = customBrowserSize;
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            Configuration.browserCapabilities = options;
        }

        Configuration.timeout = Long.parseLong(System.getProperty("timeout", "10000"));
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if (!SelenideLogger.hasListener(ALLURE_SELENIDE_LISTENER)) {
            SelenideLogger.addListener(ALLURE_SELENIDE_LISTENER,
                    new AllureSelenide().screenshots(true).savePageSource(true));
        }
    }

    @AfterEach
    void closeBrowser() {
        closeWebDriver();
    }
}