# Selenium (Java) UI + Functional Test Framework with CI/CD

Selenium 4 + JUnit 5 + AssertJ + Maven, using the Page Object Model.
Out of the box it tests the public demo site https://the-internet.herokuapp.com
so you can see it work immediately. Point it at your own app by changing `base.url`
and the page objects.

## Requirements
- JDK 17+, Maven 3.9+, Google Chrome (Selenium Manager downloads the matching driver automatically)

## Run locally
```bash
mvn test                                  # all tests, visible browser
mvn test -Dheadless=true                  # headless
mvn test -Dgroups=smoke                   # only smoke tests
mvn test -Dgroups="smoke | regression"    # tag expression
mvn test -Dbase.url=https://staging.myapp.com
mvn test -Dbrowser=firefox
```

## Run on Selenium Grid (Docker)
```bash
docker compose up -d
mvn test -Dgrid.url=http://localhost:4444 -Dheadless=true
docker compose down
```

## Tags
| Tag | Meaning | When in CI |
|---|---|---|
| `smoke` | Critical happy paths, fast | every push / PR |
| `regression` | Broader functional + UI checks | nightly |
| `visual` | Pixel-diff against baseline | manual / nightly, in the same CI image that made the baseline |

## Visual baselines
First run of `UiTests.loginPageMatchesBaseline` saves `screenshots/baseline/login-page.png`.
Later runs compare against it (fail if >1% pixels differ) and write diffs to `target/screenshots`.
Refresh baselines with `-Dupdate.baseline=true`. Create baselines in the same environment
(Docker/CI) that compares them, since fonts and rendering vary by OS.

## Project layout
```
src/test/java/com/example/framework/
  base/    DriverFactory, DriverManager (ThreadLocal), BaseTest, ScreenshotOnFailure
  pages/   Page Objects (LoginPage, SecureAreaPage)
  tests/   LoginTests (functional), UiTests (UI/visual)
  utils/   ConfigReader, ScreenshotUtil, VisualCompare
src/test/resources/  config.properties, junit-platform.properties (parallel)
.github/workflows/ui-tests.yml   GitHub Actions
Jenkinsfile, .gitlab-ci.yml      alternatives
docker-compose.yml               Selenium Grid
```

## Adapting to your app
1. Set `base.url` in `config.properties` (or pass `-Dbase.url`).
2. Replace locators and flows in `pages/` with your app's (ask devs for `data-testid` attributes).
3. Write tests in `tests/` using the page objects, and tag them.
4. Choose a CI file (GitHub Actions / Jenkins / GitLab), delete the others.
5. If your app is in the same repo, start it in the pipeline before the test step.

## Best practices baked in
- Explicit waits only (no `Thread.sleep`), one WebDriver per thread, screenshot on failure,
  config via `-D` flags, tests independent of each other, parallel classes enabled.

## Optional next steps
- Allure reporting (`allure-junit5` + `allure-maven`)
- Seed test data through API/DB calls instead of the UI
- Add more browsers via extra Grid nodes
