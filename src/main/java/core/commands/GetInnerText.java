package core.commands;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static core.commands.Find.Find;

public class GetInnerText {
    private WebDriver driver;
    public static GetInnerText GetInnerText =
            ThreadLocal.withInitial(GetInnerText::new).get();

    private GetInnerText() {
        if (GetInnerText != null) {
            throw new RuntimeException("Use GetInnerText variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public String getInnerText(By elementBy) {
        return getInnerText(Find.find(elementBy));
    }

    public String getInnerText(WebElement element) {
        setDriverSession();
        if (((RemoteWebDriver) driver).getCapabilities().getBrowserName().equalsIgnoreCase(BrowserType.IE)) {
            return element.getAttribute("innerText");
        }
        return element.getAttribute("textContent");
    }
}
