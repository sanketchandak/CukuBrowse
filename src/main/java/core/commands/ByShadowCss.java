package core.commands;

import org.openqa.selenium.*;

import java.util.List;

import static core.commands.Find.Find;
import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class ByShadowCss {
    public static ByShadowCss ByShadowCss =
            ThreadLocal.withInitial(ByShadowCss::new).get();

    private ByShadowCss() {
        if (ByShadowCss != null) {
            throw new RuntimeException("Use ByShadowCss variable to get the single instance of this class.");
        }
    }

    public WebElement findElement(String targetElement, String shadowHost, String... innerShadowHosts) {
        WebElement host = Find.find(By.cssSelector(shadowHost));
        for (String innerHost : innerShadowHosts) {
            host = getElementInsideShadowTree(host, innerHost);
        }
        return getElementInsideShadowTree(host, targetElement);
    }

    public List<WebElement> findElements(String targetElement, String shadowHost, String... innerShadowHosts) {
        WebElement host = Find.find(By.cssSelector(shadowHost));
        for (String innerHost : innerShadowHosts) {
            host = getElementInsideShadowTree(host, innerHost);
        }
        return getElementsInsideShadowTree(host, targetElement);
    }

    private WebElement getElementInsideShadowTree(WebElement host, String target) {
        if (isShadowRootAttached(host)) {
            WebElement targetWebElement = (WebElement) ExecuteJavascript.executeJavascript("return arguments[0].shadowRoot.querySelector(" + target + ")", host);
            /*WebElement targetWebElement = (WebElement) ((JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver())
                    .executeScript("return arguments[0].shadowRoot.querySelector(arguments[1])", host, target);*/
            if (targetWebElement == null) {
                throw new NoSuchElementException("The element was not found: " + target);
            }
            return targetWebElement;
        } else {
            throw new NoSuchElementException("The element is not a shadow host or has 'closed' shadow-dom mode: " + host);
        }
    }

    private boolean isShadowRootAttached(WebElement host) {
        return ExecuteJavascript.executeJavascript("return arguments[0].shadowRoot", host) != null;
        /*return ((JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver()).executeScript("return arguments[0].shadowRoot", host) != null;*/
    }

    @SuppressWarnings("unchecked")
    private List<WebElement> getElementsInsideShadowTree(WebElement host, String sh) {
        return (List<WebElement>) ExecuteJavascript.executeJavascript("return arguments[0].shadowRoot.querySelectorAll(" + sh + ")", host);
        /*return (List<WebElement>) ((JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver())
                .executeScript("return arguments[0].shadowRoot.querySelectorAll(arguments[1])", host, sh);*/
    }
}
