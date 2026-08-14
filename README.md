# luminor-qa-hometask

Test automation framework combining API tests for the Swagger Petstore (v2) and UI
tests for the Luminor website. Built as a single Gradle project with no separate
production code — everything lives under `src/test`.

## Tech stack

- Java 21
- Gradle (Groovy DSL)
- JUnit 6 (Jupiter)
- Selenide (UI)
- REST Assured (API)
- Allure (reporting)
- Datafaker (test data)
- AssertJ (assertions)

## Project structure

There's no `src/main` — this is a test-only project, so all code lives under
`src/test`:

```
src/test/java/com/luminor/hometask/
├── domain/
│   ├── api/
│   │   └── PetApiClient.java          # REST Assured client for the Petstore API
│   ├── data/
│   │   └── PetDataFactory.java        # Datafaker-based random Pet generation
│   ├── model/
│   │   ├── Pet.java                   # Pet record (DTO)
│   │   └── pet/
│   │       ├── Category.java          # Category record
│   │       └── Tag.java               # Tag record
│   └── ui/
│       ├── components/
│       │   ├── CookieConsentComponent.java
│       │   └── HeaderNavigationComponent.java
│       └── pages/
│           └── FinancialReportsPage.java
└── tests/
    ├── api/
    │   └── PetstoreApiTest.java       # @Tag("api")
    └── ui/
        ├── BaseUiTest.java            # Selenide/Allure setup shared by UI tests
        └── LuminorFinancialReportsUiTest.java  # @Tag("ui")

src/test/resources/
└── schemas/
    └── pet-schema.json                # JSON schema used to validate Pet responses
```

## Key architectural points

- **DTOs are Java records.** `Pet`, `Category`, and `Tag` are records annotated with
  `@JsonIgnoreProperties(ignoreUnknown = true)` — no boilerplate getters/setters, and
  unknown API fields don't break deserialization.
- **API test cleanup is thread-safe and never fails the test.** `PetstoreApiTest` tracks
  created pet IDs in a `CopyOnWriteArrayList` and deletes them in `@AfterEach`; any
  cleanup failure is logged, not thrown, so it can't mask the actual test result.
- **UI locators favor accessibility attributes over DOM structure.** Elements are found
  by `role`, `aria-label`, and visible text where possible, and `.ancestor(".selector")`
  is used instead of `.parent()` so locators don't break if a wrapper `div` is added.
- **No brittle waits or hardcoded dates.** The cookie consent banner is handled with a
  non-blocking `isDisplayed()` check instead of a fixed timeout, and the financial
  reports test checks whatever section matches `Year.now()` instead of a hardcoded year.

## How to run

```bash
# Everything
./gradlew test

# API tests only (tagged "api")
./gradlew apiTest

# UI tests only (tagged "ui")
./gradlew uiTest
```

### Useful system properties

| Property      | Applies to | Default                              | Example                                 |
|---------------|------------|---------------------------------------|-------------------------------------------|
| `headless`    | UI         | `false`                               | `-Dheadless=true`                       |
| `browserSize` | UI         | maximized (`1920x1080` if headless)   | `-DbrowserSize=1920x1080`                |
| `timeout`     | UI         | `10000` (ms)                          | `-Dtimeout=15000`                        |
| `baseUrl`     | UI         | `https://luminor.lv/en`               | `-DbaseUrl=https://staging.site/en`     |
| `apiBaseUrl`  | API        | `https://petstore.swagger.io/v2`      | `-DapiBaseUrl=http://localhost:8080/v2` |

Example:

```bash
./gradlew uiTest -Dheadless=true -DbrowserSize=1920x1080
```

## Allure reports

```bash
./gradlew allureServe
```

This generates the report from `build/allure-results` and opens it in your browser. To
just generate the HTML report without opening it, use `./gradlew allureReport` — the
output lands in `build/reports/allure-report/allureReport/index.html`.
