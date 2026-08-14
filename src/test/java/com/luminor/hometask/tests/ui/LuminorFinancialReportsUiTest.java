package com.luminor.hometask.tests.ui;

import com.luminor.hometask.domain.ui.components.CookieConsentComponent;
import com.luminor.hometask.domain.ui.components.HeaderNavigationComponent;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static com.codeborne.selenide.Selenide.open;

@Epic("Luminor website")
@Feature("Financial reports navigation")
@Tag("ui")
class LuminorFinancialReportsUiTest extends BaseUiTest {

    @Test
    void financialReportsCurrentYearSectionIsOpenWithReportLink() {
        String currentYear = String.valueOf(Year.now().getValue());

        open(BASE_URL);

        new CookieConsentComponent().acceptIfPresent();

        new HeaderNavigationComponent()
                .openHamburgerMenu()
                .openAboutUsSection()
                .openFinancialReports()
                .verifySectionIsOpen(currentYear)
                .verifyReportLinkIsPresent(currentYear);
    }
}
