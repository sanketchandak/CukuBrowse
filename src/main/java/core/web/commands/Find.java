package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.Arrays;

public class Find {
    private static final Logger logger = LoggerFactory.getLogger(Find.class);
    private WebDriver driver;
    public static Find Find =
            ThreadLocal.withInitial(Find::new).get();

    private Find() {
        if (Find != null) {
            logger.error("Use Find variable to get the single instance of this class.");
            throw new CukeBrowseException("Use Find variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public WebElement findElement(By elementBy) {
        setDriverSession();
        logger.info(String.format("Find Element: find element by '%s'", elementBy));
        return driver.findElement(elementBy);
    }

    public WebElement findElement(By parentBy, By elementBy, int... index) {
        setDriverSession();
        logger.info(String.format("Find Element: find child element: '%s' under Parent element: '%s' at '%s' index", elementBy.toString(), parentBy.toString(), (index.length != 0) ? Arrays.toString(index) : '0'));
        return findElement(driver.findElement(parentBy), elementBy, index);
    }

    public WebElement findElement(WebElement parentElement, By elementBy, int... index) {
        assert index != null;
        logger.info(String.format("Find Element: find child element: '%s' under Parent element: '%s' at '%s' index", elementBy.toString(), parentElement.toString(), (index.length != 0) ? Arrays.toString(index) : '0'));
        return index.length == 0 ?
                parentElement.findElement(elementBy) :
                parentElement.findElements(elementBy).get(index[0]);
    }
}
