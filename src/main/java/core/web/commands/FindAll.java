package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.List;

import static core.web.commands.Find.Find;

public class FindAll {
    private static final Logger logger = LoggerFactory.getLogger(FindAll.class);
    private WebDriver driver;
    public static FindAll FindAll =
            ThreadLocal.withInitial(FindAll::new).get();

    private FindAll() {
        if (FindAll != null) {
            logger.error("Use FindAll variable to get the single instance of this class.");
            throw new CukeBrowseException("Use FindAll variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public List<WebElement> find(By elementBy) {
        setDriverSession();
        logger.info(String.format("Find All Elements: find all elements by '%s'", elementBy));
        return driver.findElements(elementBy);
    }

    public List<WebElement> find(By parentBy, By elementBy) {
        logger.info(String.format("Find All Elements: find all child elements: '%s' under Parent element: '%s'", elementBy.toString(), parentBy.toString()));
        return find(Find.findElement(parentBy), elementBy);
    }

    public List<WebElement> find(WebElement parentElement, By elementBy) {
        logger.info(String.format("Find All Elements: find all child elements: '%s' under Parent element: '%s'", elementBy.toString(), parentElement.toString()));
        return parentElement.findElements(elementBy);
    }
}
