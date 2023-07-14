package core.web.commands;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.Arrays;
import java.util.List;

import static core.web.commands.Find.Find;
import static core.web.commands.ExecuteJavascript.ExecuteJavascript;

public class ByShadowCss {
    private static final Logger logger = LoggerFactory.getLogger(ByShadowCss.class);
    public static ByShadowCss ByShadowCss =
            ThreadLocal.withInitial(ByShadowCss::new).get();

    private ByShadowCss() {
        if (ByShadowCss != null) {
            logger.error("Use ByShadowCss variable to get the single instance of this class.");
            throw new CukeBrowseException("Use ByShadowCss variable to get the single instance of this class.");
        }
    }

    public WebElement findElement(String targetElement, String shadowHost, String... innerShadowHosts) {
        logger.info(String.format("Find Shadow Element: Get '%s' under Shadow host '%s' with inner shadow hosts as '%s'", targetElement, shadowHost, (innerShadowHosts.length != 0) ? Arrays.toString(innerShadowHosts) : ""));
        WebElement host = Find.findElement(By.cssSelector(shadowHost));
        for (String innerHost : innerShadowHosts) {
            host = getElementInsideShadowTree(host, innerHost);
        }
        return getElementInsideShadowTree(host, targetElement);
    }

    public List<WebElement> findElements(String targetElement, String shadowHost, String... innerShadowHosts) {
        logger.info(String.format("Find Shadow Elements: Get all '%s' under Shadow host '%s' with inner shadow hosts as '%s'", targetElement, shadowHost, (innerShadowHosts.length != 0) ? Arrays.toString(innerShadowHosts) : ""));
        WebElement host = Find.findElement(By.cssSelector(shadowHost));
        for (String innerHost : innerShadowHosts) {
            host = getElementInsideShadowTree(host, innerHost);
        }
        return getElementsInsideShadowTree(host, targetElement);
    }

    private WebElement getElementInsideShadowTree(WebElement host, String target) {
        if (isShadowRootAttached(host)) {
            WebElement targetWebElement = (WebElement) ExecuteJavascript.execute("return arguments[0].shadowRoot.querySelector(" + target + ")", host);
            /*WebElement targetWebElement = (WebElement) ((JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver())
                    .executeScript("return arguments[0].shadowRoot.querySelector(arguments[1])", host, target);*/
            if (targetWebElement == null) {
                logger.error(String.format("Get Element Inside Shadow Tree: The element was not found: '%s'", target));
                throw new CukeBrowseException("Get Element Inside Shadow Tree: The element was not found: " + target);
            }
            return targetWebElement;
        } else {
            logger.error(String.format("Get Element Inside Shadow Tree: The element is not a shadow host or has 'closed' shadow-dom mode: '%s'", host.toString()));
            throw new CukeBrowseException("Get Element Inside Shadow Tree: The element is not a shadow host or has 'closed' shadow-dom mode: " + host);
        }
    }

    private boolean isShadowRootAttached(WebElement host) {
        return ExecuteJavascript.execute("return arguments[0].shadowRoot", host) != null;
        /*return ((JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver()).executeScript("return arguments[0].shadowRoot", host) != null;*/
    }

    @SuppressWarnings("unchecked")
    private List<WebElement> getElementsInsideShadowTree(WebElement host, String sh) {
        return (List<WebElement>) ExecuteJavascript.execute("return arguments[0].shadowRoot.querySelectorAll(" + sh + ")", host);
        /*return (List<WebElement>) ((JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver())
                .executeScript("return arguments[0].shadowRoot.querySelectorAll(arguments[1])", host, sh);*/
    }
}
