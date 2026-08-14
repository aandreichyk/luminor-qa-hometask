package com.luminor.hometask.domain.ui.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

public class CookieConsentComponent {

    private static final Logger log = LoggerFactory.getLogger(CookieConsentComponent.class);

    private final SelenideElement banner = $("#onetrust-banner-sdk");
    private final SelenideElement acceptButton = $("#onetrust-accept-btn-handler");

    @Step("Accept cookies if the consent banner is present")
    public void acceptIfPresent() {
        if (banner.isDisplayed()) {
            acceptButton.click();
            banner.shouldNotBe(Condition.visible);
        } else {
            log.debug("Cookie consent banner is not displayed, proceeding without action.");
        }
    }
}
