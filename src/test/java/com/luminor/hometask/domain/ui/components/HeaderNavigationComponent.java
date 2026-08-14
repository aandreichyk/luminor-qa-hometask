package com.luminor.hometask.domain.ui.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.luminor.hometask.domain.ui.pages.FinancialReportsPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class HeaderNavigationComponent {

    private static final String ABOUT_US_LABEL = "About Us";
    private static final String FINANCIAL_REPORTS_LABEL = "Financial Reports";

    private final SelenideElement hamburgerToggle = $("a[data-open-toggle='meta-menu'][aria-label='Site menu']");
    private final SelenideElement metaMenu = $("#meta-menu");

    @Step("Open hamburger menu")
    public HeaderNavigationComponent openHamburgerMenu() {
        hamburgerToggle.shouldBe(Condition.visible, Condition.enabled).click();
        metaMenu.shouldBe(Condition.visible);
        return this;
    }

    @Step("Open About Us section")
    public HeaderNavigationComponent openAboutUsSection() {
        aboutUsSection().$("[role='button'], .meta-menu__link")
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        return this;
    }

    @Step("Open Financial Reports")
    public FinancialReportsPage openFinancialReports() {
        aboutUsSection().$(byText(FINANCIAL_REPORTS_LABEL))
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        return new FinancialReportsPage();
    }

    private SelenideElement aboutUsSection() {
        return metaMenu.$(byText(ABOUT_US_LABEL)).ancestor("li");
    }
}