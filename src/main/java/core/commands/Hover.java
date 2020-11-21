package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static core.commands.Find.Find;
import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class Hover {
    private WebDriver driver;
    public static Hover Hover =
            ThreadLocal.withInitial(Hover::new).get();

    private Hover() {
        if (Hover != null) {
            throw new RuntimeException("Use Hover variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public void hover(By hoverElementBy, HoverType hoverType) {
        hover(Find.find(hoverElementBy), hoverType);
    }

    public void hover(WebElement hoverElement, HoverType hoverType) {
        /*WebDriver driver = WebDriverRunner.getInstance().getWebDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;*/
        setDriverSession();
        ExecuteJavascript.executeJavascript("arguments[0].scrollIntoView(true);", hoverElement);
        switch (hoverType) {
            case JSHover: {
                ExecuteJavascript.executeJavascript("var element = arguments[0]; " +
                        "var mouseEventObj = document.createEvent('MouseEvents'); " +
                        "mouseEventObj.initEvent( 'mouseover', true, true ); " +
                        "element.dispatchEvent(mouseEventObj);", hoverElement);
                break;
            }
            case JSOut: {
                ExecuteJavascript.executeJavascript("var element = arguments[0]; " +
                        "var mouseEventObj = document.createEvent('MouseEvents'); " +
                        "mouseEventObj.initEvent( 'mouseout', true, true ); " +
                        "element.dispatchEvent(mouseEventObj);", hoverElement);
                break;
            }
            case ActionHover: {
                new Actions(driver).moveToElement(hoverElement).perform();
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown Hover option: " + hoverType);
            }
        }
    }

}
