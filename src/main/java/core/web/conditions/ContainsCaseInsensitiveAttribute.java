package core.web.conditions;

import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ContainsCaseInsensitiveAttribute extends Condition {
    WebDriver driver;
    private WebDriverWait wait;
    private By elementBy = null;
    private WebElement element = null;
    private final String attributeName;
    private final String attributeValue;

    public ContainsCaseInsensitiveAttribute(By elementBy, String attributeName, String attributeValue) {
        this.elementBy = elementBy;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public ContainsCaseInsensitiveAttribute(WebElement element, String attributeName, String attributeValue) {
        this.element = element;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
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

    private boolean checkCondition() {
        AtomicReference<String> uiAttributeValue = new AtomicReference<>();
        try {
            return wait.until(d -> {
                        try {
                            WebElement element;
                            if (elementBy != null) {
                                element = d.findElement(elementBy);
                            } else {
                                element = this.element;
                            }
                            uiAttributeValue.set(element.getAttribute(attributeName));
                            if (uiAttributeValue.get() == null || uiAttributeValue.get().isEmpty()) {
                                uiAttributeValue.set(element.getCssValue(attributeName));
                            }
                            return uiAttributeValue.get().toLowerCase().contains(attributeValue.toLowerCase());
                        } catch (StaleElementReferenceException e) {
                            return null;
                        }
                    }
            );
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(String.format(attributeName + " to be \"%s\". Current " + attributeName + ": \"%s\"", attributeValue, uiAttributeValue.get()),
                    timeoutException);
        }
    }
}
