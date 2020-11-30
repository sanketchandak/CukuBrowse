package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.Find.Find;
import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class Click {
    private static final Logger logger = LoggerFactory.getLogger(Click.class);
    public static Click Click =
            ThreadLocal.withInitial(Click::new).get();
    private WebDriver driver;

    private Click() {
        if (Click != null) {
            throw new RuntimeException("Use Click variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public void clickOn(By elementIdentifierBy, ClickType clickType) {
        clickOn(Find.find(elementIdentifierBy), clickType);
    }

    public void clickOn(WebElement element, ClickType clickType) {
        setDriverSession();
        ExecuteJavascript.executeJavascript("arguments[0].scrollIntoView(true);", element);
        switch (clickType) {
            case JSClick: {
                ExecuteJavascript.executeJavascript("arguments[0].click()", element);
                break;
            }
            case JSDoubleClick: {
                ExecuteJavascript.executeJavascript("var element = arguments[0]; " +
                        "var mouseEventObj = document.createEvent('MouseEvents'); " +
                        "mouseEventObj.initEvent( 'dblclick', true, true ); " +
                        "element.dispatchEvent(mouseEventObj);", element);
                break;
            }
            case JSRightClick: {
                ExecuteJavascript.executeJavascript("var element = arguments[0]; " +
                        "var mouseEventObj = document.createEvent('MouseEvents'); " +
                        "mouseEventObj.initEvent( 'contextmenu', true, true ); " +
                        "element.dispatchEvent(mouseEventObj);", element);
                break;
            }
            case ActionDoubleClick: {
                new Actions(driver).doubleClick(element).perform();
                break;
            }
            case ActionRightClick: {
                new Actions(driver).contextClick(element).perform();
                break;
            }
            case DEFAULT: {
                element.click();
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown click option: " + clickType);
            }
        }
        logger.info("Click On: " + clickType.name() + " on " + element.toString());
    }
}
