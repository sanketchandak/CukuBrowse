package core.web.conditions;

import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class UrlContains extends Condition {
    WebDriver driver;
    private WebDriverWait wait;
    private final String url;
    private final boolean shouldBe;

    public UrlContains(String url, boolean shouldBe) {
        this.url = url;
        this.shouldBe = shouldBe;
    }

    public boolean verifyCondition(boolean safeWaitFlag) {
        return verifyCondition(60, safeWaitFlag, null);
    }

    public boolean verifyCondition(long timeOutInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes) {
        setDriverWaitSession(timeOutInSeconds, safeWaitFlag, exceptionTypes);
        return checkCondition();
    }

    public boolean verifyCondition(long timeOutInSeconds, long pollingIntervalInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes) {
        setDriverWaitSession(timeOutInSeconds, safeWaitFlag, exceptionTypes);
        wait.pollingEvery(Duration.ofMillis(SECONDS.toMillis(pollingIntervalInSeconds)));
        return checkCondition();
    }

    private void setDriverWaitSession(long timeOutInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes) {
        driver = WebDriverRunner.getInstance().getWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        if (safeWaitFlag) {
            if (exceptionTypes == null || exceptionTypes.isEmpty()) {
                wait.ignoring(Exception.class);
            } else {
                wait.ignoreAll(exceptionTypes);
            }
        }
    }

    private boolean checkCondition() {
        return shouldBe ?
                wait.until(ExpectedConditions.urlContains(url)) :
                wait.until(ExpectedConditions.not(ExpectedConditions.urlContains(url)));
    }
}
