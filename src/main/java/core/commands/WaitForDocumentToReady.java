package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitForDocumentToReady {
    private WebDriver driver;
    public static WaitForDocumentToReady WaitForDocumentToReady =
            ThreadLocal.withInitial(WaitForDocumentToReady::new).get();

    private WaitForDocumentToReady() {
        if (WaitForDocumentToReady != null) {
            throw new RuntimeException("Use WaitForDocumentToReady variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public boolean waitForPageToReady() {
        try {
            setDriverSession();
            return new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
            {
                assert wd != null;
                return ((JavascriptExecutor) wd).executeScript("return (document.readyState == 'complete' && jQuery.active == 0)").equals(true);
            });
        } catch (Exception e) {
            return false;
        }
    }

}
