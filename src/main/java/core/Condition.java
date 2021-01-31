package core;

import core.conditions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class Condition {

    private static final Logger logger = LoggerFactory.getLogger(Condition.class);

    public abstract boolean verifyCondition(boolean safeWaitFlag);

    public abstract boolean verifyCondition(long timeOutInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes);

    public abstract boolean verifyCondition(long timeOutInSeconds, long pollingIntervalInSeconds, boolean safeWaitFlag, List<Class<? extends Throwable>> exceptionTypes);

    public static Condition clickable(By elementBy) {
        logger.info(String.format("Clickable: Wait for '%s' element to get clickable", elementBy.toString()));
        return new Clickable(elementBy);
    }

    public static Condition clickable(WebElement element) {
        logger.info(String.format("Clickable: Wait for '%s' element to get clickable", element.toString()));
        return new Clickable(element);
    }

    public static Condition invisible(By elementBy) {
        logger.info(String.format("Invisible: Wait for '%s' element to get invisible", elementBy.toString()));
        return new Invisible(elementBy);
    }

    public static Condition invisible(WebElement element) {
        logger.info(String.format("Invisible: Wait for '%s' element to get invisible", element.toString()));
        return new Invisible(element);
    }

    public static Condition visible(By elementBy) {
        logger.info(String.format("Visible: Wait for '%s' element to get visible", elementBy.toString()));
        return new Visible(elementBy);
    }

    public static Condition visible(WebElement element) {
        logger.info(String.format("Visible: Wait for '%s' element to get visible", element.toString()));
        return new Visible(element);
    }

    public static Condition presence(By elementBy) {
        logger.info(String.format("Presence: Wait for presence of '%s' element in DOM", elementBy.toString()));
        return new Presence(elementBy);
    }

    public static Condition exactAttribute(By elementBy, String attributeName, String attributeValue) {
        logger.info(String.format("Exact Attribute: Wait for '%s' element '%s' attribute value exactly to be '%s'", elementBy.toString(), attributeName, attributeValue));
        return new ExactAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition exactAttribute(WebElement element, String attributeName, String attributeValue) {
        logger.info(String.format("Exact Attribute: Wait for '%s' element '%s' attribute value exactly to be '%s'", element.toString(), attributeName, attributeValue));
        return new ExactAttribute(element, attributeName, attributeValue);
    }

    public static Condition containsCaseInsensitiveAttribute(By elementBy, String attributeName, String attributeValue) {
        logger.info(String.format("Contains Case Insensitive Attribute: Wait for '%s' element '%s' attribute value contains insensitive to be '%s'", elementBy.toString(), attributeName, attributeValue));
        return new ContainsCaseInsensitiveAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition containsCaseInsensitiveAttribute(WebElement element, String attributeName, String attributeValue) {
        logger.info(String.format("Contains Case Insensitive Attribute: Wait for '%s' element '%s' attribute value contains insensitive to be '%s'", element.toString(), attributeName, attributeValue));
        return new ContainsCaseInsensitiveAttribute(element, attributeName, attributeValue);
    }

    public static Condition containsCaseSensitiveAttribute(By elementBy, String attributeName, String attributeValue) {
        logger.info(String.format("Contains Case Sensitive Attribute: Wait for '%s' element '%s' attribute value contains sensitive to be '%s'", elementBy.toString(), attributeName, attributeValue));
        return new ContainsCaseSensitiveAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition containsCaseSensitiveAttribute(WebElement element, String attributeName, String attributeValue) {
        logger.info(String.format("Contains Case Sensitive Attribute: Wait for '%s' element '%s' attribute value contains sensitive to be '%s'", element.toString(), attributeName, attributeValue));
        return new ContainsCaseSensitiveAttribute(element, attributeName, attributeValue);
    }

    public static Condition notContainsAttribute(By elementBy, String attributeName, String attributeValue) {
        logger.info(String.format("Not Contains Attribute: Wait for '%s' element '%s' attribute not contains value as '%s'", elementBy.toString(), attributeName, attributeValue));
        return new NotContainsAttribute(elementBy, attributeName, attributeValue);
    }

    public static Condition notContainsAttribute(WebElement element, String attributeName, String attributeValue) {
        logger.info(String.format("Not Contains Attribute: Wait for '%s' element '%s' attribute not contains value as '%s'", element.toString(), attributeName, attributeValue));
        return new NotContainsAttribute(element, attributeName, attributeValue);
    }

    public static Condition exactText(By elementBy, String elementText) {
        logger.info(String.format("Exact Text: Wait for '%s' element having exact text as '%s'", elementBy.toString(), elementText));
        return new ExactText(elementBy, elementText);
    }

    public static Condition exactText(WebElement element, String elementText) {
        logger.info(String.format("Exact Text: Wait for '%s' element having exact text as '%s'", element.toString(), elementText));
        return new ExactText(element, elementText);
    }

    public static Condition containsCaseInsensitiveText(By elementBy, String elementText) {
        logger.info(String.format("Contains Case Insensitive Text: Wait for '%s' element contains insensitive text as '%s'", elementBy.toString(), elementText));
        return new ContainsCaseInsensitiveText(elementBy, elementText);
    }

    public static Condition containsCaseInsensitiveText(WebElement element, String elementText) {
        logger.info(String.format("Contains Case Insensitive Text: Wait for '%s' element contains insensitive text as '%s'", element.toString(), elementText));
        return new ContainsCaseInsensitiveText(element, elementText);
    }

    public static Condition containsCaseSensitiveText(By elementBy, String elementText) {
        logger.info(String.format("Contains Case Sensitive Text: Wait for '%s' element contains sensitive text as '%s'", elementBy.toString(), elementText));
        return new ContainsCaseSensitiveText(elementBy, elementText);
    }

    public static Condition containsCaseSensitiveText(WebElement element, String elementText) {
        logger.info(String.format("Contains Case Sensitive Text: Wait for '%s' element contains sensitive text as '%s'", element.toString(), elementText));
        return new ContainsCaseSensitiveText(element, elementText);
    }

    public static Condition notContainsText(By elementBy, String elementText) {
        logger.info(String.format("Not Contains Text: Wait for '%s' element not contains text as '%s'", elementBy.toString(), elementText));
        return new NotContainsText(elementBy, elementText);
    }

    public static Condition notContainsText(WebElement element, String elementText) {
        logger.info(String.format("Not Contains Text: Wait for '%s' element not contains text as '%s'", element.toString(), elementText));
        return new NotContainsText(element, elementText);
    }

    public static Condition titleContains(String title) {
        logger.info(String.format("Title Contains: Wait for page contains title as '%s'", title));
        return new TitleContains(title);
    }

    public static Condition titleIs(String title) {
        logger.info(String.format("Title Is: Wait for page title to be '%s'", title));
        return new TitleIs(title);
    }

    public static Condition urlContains(String url) {
        logger.info(String.format("URL Contains: Wait for page url contains '%s'", url));
        return new UrlContains(url);
    }

    public static Condition urlIs(String url) {
        logger.info(String.format("URL Is: Wait for page url to be '%s'", url));
        return new UrlIs(url);
    }

    public static Condition AlertPresence() {
        logger.info("Alert Presence: Wait for alert presence");
        return new AlertPresence();
    }

    public static Condition CountOfElementsLessThan(By elementBy, int elementCount) {
        logger.info(String.format("Count Of Elements Less Than: Wait for '%s' element count to be less than '%s'", elementBy.toString(), elementCount));
        return new CountOfElementsLessThan(elementBy, elementCount);
    }

    public static Condition CountOfElementsMoreThan(By elementBy, int elementCount) {
        logger.info(String.format("Count Of Elements More Than: Wait for '%s' element count to be more than '%s'", elementBy.toString(), elementCount));
        return new CountOfElementsMoreThan(elementBy, elementCount);
    }

    public static Condition ExactCountOfElements(By elementBy, int elementCount) {
        logger.info(String.format("Exact Count Of Elements: Wait for '%s' element count to be '%s'", elementBy.toString(), elementCount));
        return new ExactCountOfElements(elementBy, elementCount);
    }

    public static Condition IsFramePresent(String frameLocator) {
        logger.info(String.format("Is Frame Present: Wait for frame to be present with name '%s'", frameLocator));
        return new IsFramePresent(frameLocator);
    }

    public static Condition IsFramePresent(By locator) {
        logger.info(String.format("Is Frame Present: Wait for '%s' frame to be present", locator.toString()));
        return new IsFramePresent(locator);
    }

    public static Condition IsFramePresent(int frameNumber) {
        logger.info(String.format("Is Frame Present: Wait for frame to be present at frame number as '%s'", frameNumber));
        return new IsFramePresent(frameNumber);
    }

    public static Condition IsFramePresent(WebElement frameLocator) {
        logger.info(String.format("Is Frame Present: Wait for '%s' frame to be present", frameLocator.toString()));
        return new IsFramePresent(frameLocator);
    }
}
