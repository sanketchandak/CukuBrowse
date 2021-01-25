package core;

import core.conditions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class Condition {

    public abstract boolean verifyCondition(boolean safeWaitFlag);

    public abstract boolean verifyCondition(long timeOutInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes);

    public abstract boolean verifyCondition(long timeOutInSeconds, long pollingIntervalInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes);

    public static Condition clickable(By elementBy) {
        return new Clickable(elementBy);
    }

    public static Condition clickable(WebElement element) {
        return new Clickable(element);
    }

    public static Condition invisible(By elementBy) {
        return new Invisible(elementBy);
    }

    public static Condition invisible(WebElement element) {
        return new Invisible(element);
    }

    public static Condition visible(By elementBy) {
        return new Visible(elementBy);
    }

    public static Condition visible(WebElement element) {
        return new Visible(element);
    }

    public static Condition presence(By elementBy) {
        return new Presence(elementBy);
    }

    public static Condition exactAttribute(By elementBy, String attributeName, String attributeValue) {
        return new ExactAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition exactAttribute(WebElement element, String attributeName, String attributeValue) {
        return new ExactAttribute(element, attributeName, attributeValue);
    }

    public static Condition containsCaseInsensitiveAttribute(By elementBy, String attributeName, String attributeValue) {
        return new ContainsCaseInsensitiveAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition containsCaseInsensitiveAttribute(WebElement element, String attributeName, String attributeValue) {
        return new ContainsCaseInsensitiveAttribute(element, attributeName, attributeValue);
    }

    public static Condition containsCaseSensitiveAttribute(By elementBy, String attributeName, String attributeValue) {
        return new ContainsCaseSensitiveAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition containsCaseSensitiveAttribute(WebElement element, String attributeName, String attributeValue) {
        return new ContainsCaseSensitiveAttribute(element, attributeName, attributeValue);
    }

    public static Condition notContainsAttribute(By elementBy, String attributeName, String attributeValue) {
        return new NotContainsAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition notContainsAttribute(WebElement element, String attributeName, String attributeValue) {
        return new NotContainsAttribute(element, attributeName, attributeValue);
    }

    public static Condition exactText(By elementBy, String elementText) {
        return new ExactText(elementBy, elementText);
    }

    public static Condition exactText(WebElement element, String elementText) {
        return new ExactText(element, elementText);
    }

    public static Condition containsCaseInsensitiveText(By elementBy, String elementText) {
        return new ContainsCaseInsensitiveText(elementBy, elementText);
    }

    public static Condition containsCaseInsensitiveText(WebElement element, String elementText) {
        return new ContainsCaseInsensitiveText(element, elementText);
    }

    public static Condition containsCaseSensitiveText(By elementBy, String elementText) {
        return new ContainsCaseSensitiveText(elementBy, elementText);
    }

    public static Condition containsCaseSensitiveText(WebElement element, String elementText) {
        return new ContainsCaseSensitiveText(element, elementText);
    }

    public static Condition notContainsText(By elementBy, String elementText) {
        return new NotContainsText(elementBy, elementText);
    }

    public static Condition notContainsText(WebElement element, String elementText) {
        return new NotContainsText(element, elementText);
    }

    public static Condition titleContains(String title) {
        return new TitleContains(title);
    }

    public static Condition titleIs(String title) {
        return new TitleIs(title);
    }

    public static Condition urlContains(String url) {
        return new UrlContains(url);
    }

    public static Condition urlIs(String url) {
        return new UrlIs(url);
    }

    public static Condition AlertPresence() {
        return new AlertPresence();
    }

    public static Condition CountOfElementsLessThan(By elementBy, int elementCount) {
        return new CountOfElementsLessThan(elementBy, elementCount);
    }

    public static Condition CountOfElementsMoreThan(By elementBy, int elementCount) {
        return new CountOfElementsMoreThan(elementBy, elementCount);
    }

    public static Condition ExactCountOfElements(By elementBy, int elementCount) {
        return new ExactCountOfElements(elementBy, elementCount);
    }

    public static Condition IsFramePresent(String frameLocator) {
        return new IsFramePresent(frameLocator);
    }

    public static Condition IsFramePresent(By locator) {
        return new IsFramePresent(locator);
    }

    public static Condition IsFramePresent(int frameLocator) {
        return new IsFramePresent(frameLocator);
    }

    public static Condition IsFramePresent(WebElement frameLocator) {
        return new IsFramePresent(frameLocator);
    }
}
