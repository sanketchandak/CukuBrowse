package core.web.conditions;

import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Visible extends Condition {
    WebDriver driver;
    private WebDriverWait wait;
    private By elementBy = null;
    private WebElement element = null;

    public Visible(By elementBy) {
        this.elementBy = elementBy;
    }

    public Visible(WebElement element) {
        this.element = element;
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
        wait = new WebDriverWait(driver, timeOutInSeconds);
        if (safeWaitFlag) {
            if (exceptionTypes == null || exceptionTypes.size() == 0) {
                wait.ignoring(Exception.class);
            } else {
                wait.ignoreAll(exceptionTypes);
            }
        }
    }

    private boolean checkCondition() {
        if (elementBy != null) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy)) != null ? Boolean.TRUE : Boolean.FALSE;
        } else {
            return !(wait.until(ExpectedConditions.visibilityOfAllElements(element)).isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
        }
    }
}
