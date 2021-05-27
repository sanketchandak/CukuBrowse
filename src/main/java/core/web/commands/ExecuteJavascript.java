package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ExecuteJavascript {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteJavascript.class);
    private WebDriver driver;
    public static ExecuteJavascript ExecuteJavascript =
            ThreadLocal.withInitial(ExecuteJavascript::new).get();

    private ExecuteJavascript() {
        if (ExecuteJavascript != null) {
            logger.error("Use ExecuteJavascript variable to get the single instance of this class.");
            throw new RuntimeException("Use ExecuteJavascript variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public Object executeJavascript(String script, WebElement... elements) {
        assert elements != null;
        setDriverSession();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if (elements.length == 0) {
            logger.info(String.format("Execute Javascript: '%s'", script));
            return js.executeScript(script);
        } else {
            logger.info(String.format("Execute Javascript: '%s' with elements: %s", script, Arrays.toString(elements)));
            return js.executeScript(script, elements);
        }
    }
}
