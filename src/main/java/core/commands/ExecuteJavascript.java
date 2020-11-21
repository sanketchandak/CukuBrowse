package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExecuteJavascript {
    private WebDriver driver;
    public static ExecuteJavascript ExecuteJavascript =
            ThreadLocal.withInitial(ExecuteJavascript::new).get();

    private ExecuteJavascript() {
        if (ExecuteJavascript != null) {
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
            return js.executeScript(script);
        } else {
            return js.executeScript(script, (Object) elements);
        }
    }
}
