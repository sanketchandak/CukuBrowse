package core.web.conditions;

import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ContainsCaseInsensitiveText extends Condition {
    WebDriver driver;
    private WebDriverWait wait;
    private By elementBy = null;
    private WebElement element = null;
    private final String elementText;

    public ContainsCaseInsensitiveText(By elementBy, String elementText) {
        this.elementBy = elementBy;
        this.elementText = elementText;
    }

    public ContainsCaseInsensitiveText(WebElement element, String elementText) {
        this.element = element;
        this.elementText = elementText;
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
        AtomicReference<String> uiElementText = new AtomicReference<>();
        try {
            return wait.until(d -> {
                        try {
                            WebElement element;
                            if (elementBy != null) {
                                element = d.findElement(elementBy);
                            } else {
                                element = this.element;
                            }
                            uiElementText.set(element.getText());
                            return uiElementText.get().toLowerCase().contains(elementText.toLowerCase());
                        } catch (StaleElementReferenceException e) {
                            return null;
                        }
                    }
            );
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(String.format("Insensitive text '%s' to be present in element '%s'. Actual text is '%s'.", elementText, elementBy != null ? elementBy : element.toString(), uiElementText.get()),
                    timeoutException);
        }
    }
}
