package core.conditions;

import core.Condition;
import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class IsFramePresent extends Condition {
    WebDriver driver;
    private WebDriverWait wait;
    private String frameLocator = null;
    private By locator = null;
    private int framePosition = -1;
    private WebElement frameElement = null;

    public IsFramePresent(String frameLocator) {
        this.frameLocator = frameLocator;
    }

    public IsFramePresent(By locator) {
        this.locator = locator;
    }

    public IsFramePresent(int frameNumber) {
        this.framePosition = frameNumber;
    }

    public IsFramePresent(WebElement frameLocator) {
        this.frameElement = frameLocator;
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
        try {
            WebDriver tempDriver = wait.until(d -> {
                        try {
                            if (frameLocator != null) {
                                return d.switchTo().frame(frameLocator);
                            } else if (framePosition != -1) {
                                return d.switchTo().frame(framePosition);
                            } else if (locator != null) {
                                return d.switchTo().frame(d.findElement(locator));
                            } else {
                                return d.switchTo().frame(frameElement);
                            }
                        } catch (NoSuchFrameException e) {
                            return null;
                        }
                    }
            );
            return tempDriver != null ? Boolean.TRUE : Boolean.FALSE;
        } catch (TimeoutException timeoutException) {
            if (frameLocator != null) {
                throw new TimeoutException("frame to be available: " + frameLocator, timeoutException);
            } else if (framePosition != -1) {
                throw new TimeoutException("frame to be available: " + framePosition, timeoutException);
            } else if (locator != null) {
                throw new TimeoutException("frame to be available: " + locator, timeoutException);
            } else {
                throw new TimeoutException("frame to be available: " + frameElement, timeoutException);
            }
        }
    }
}
