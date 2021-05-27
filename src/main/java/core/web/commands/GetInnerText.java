package core.web.commands;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.web.commands.Find.Find;
import static core.web.commands.GetAttribute.GetAttribute;

public class GetInnerText {
    private static final Logger logger = LoggerFactory.getLogger(GetInnerText.class);
    private WebDriver driver;
    public static GetInnerText GetInnerText =
            ThreadLocal.withInitial(GetInnerText::new).get();

    private GetInnerText() {
        if (GetInnerText != null) {
            logger.error("Use GetInnerText variable to get the single instance of this class.");
            throw new RuntimeException("Use GetInnerText variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public String getInnerText(By elementBy) {
        String retrievedInnerText = getInnerText(Find.find(elementBy));
        logger.info(String.format("Get Inner Text: inner text of element: '%s' having value as '%s'", elementBy.toString(), retrievedInnerText));
        return retrievedInnerText;
    }

    public String getInnerText(WebElement element) {
        setDriverSession();
        logger.info(String.format("Get Inner Text: get inner text of element: '%s'", element.toString()));
        if (((RemoteWebDriver) driver).getCapabilities().getBrowserName().equalsIgnoreCase(BrowserType.IE)) {
            String retrievedInnerText = GetAttribute.getAttribute(element,"innerText");
            logger.info(String.format("Get Inner Text: inner text of element: '%s' having value as '%s'", element, retrievedInnerText));
            return retrievedInnerText;
        }
        String retrievedInnerText = GetAttribute.getAttribute(element,"textContent");
        logger.info(String.format("Get Inner Text: inner text of element: '%s' having value as '%s'", element, retrievedInnerText));
        return retrievedInnerText;
    }
}
