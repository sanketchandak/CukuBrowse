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

public class Clickable extends Condition {
    WebDriver driver;
    private WebDriverWait wait;
    private By elementBy = null;
    private WebElement element = null;
    private final boolean shouldBe;

    public Clickable(By elementBy, boolean shouldBe) {
        this.elementBy = elementBy;
        this.shouldBe = shouldBe;
    }

    public Clickable(WebElement element, boolean shouldBe) {
        this.element = element;
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
            if (exceptionTypes == null || exceptionTypes.size() == 0) {
                wait.ignoring(Exception.class);
            } else {
                wait.ignoreAll(exceptionTypes);
            }
        }
    }

    private boolean checkCondition(){
        if (elementBy != null) {
            if(shouldBe){
                return wait.until(ExpectedConditions.elementToBeClickable(elementBy)) != null ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(elementBy)));
            }
        } else {
            if(shouldBe){
                return wait.until(ExpectedConditions.elementToBeClickable(element)) != null ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(element)));
            }
        }
    }
}
