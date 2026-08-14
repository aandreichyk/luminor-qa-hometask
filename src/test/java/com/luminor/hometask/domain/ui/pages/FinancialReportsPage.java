package com.luminor.hometask.domain.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$$;

public class FinancialReportsPage {

    private static final Logger log = LoggerFactory.getLogger(FinancialReportsPage.class);
    private static final String EXPANDED_CLASS = "is-expanded";

    @Step("Verify the {year} section is open")
    public FinancialReportsPage verifySectionIsOpen(String year) {
        log.info("Verifying the {} financial reports section is open", year);
        yearAccordion(year).shouldHave(Condition.cssClass(EXPANDED_CLASS));
        return this;
    }

    @Step("Verify a report link is present in the {year} section")
    public FinancialReportsPage verifyReportLinkIsPresent(String year) {
        log.info("Verifying a report link is present in the {} section", year);
        yearAccordion(year).$(".accordion__content a").shouldBe(Condition.visible);
        return this;
    }

    private SelenideElement yearAccordion(String year) {
        return $$(".accordion__title").findBy(Condition.exactText(year)).ancestor(".accordion");
    }
}
