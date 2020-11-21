package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;
import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class ScrollIntoView {
    public static ScrollIntoView ScrollIntoView =
            ThreadLocal.withInitial(ScrollIntoView::new).get();

    private ScrollIntoView() {
        if (ScrollIntoView != null) {
            throw new RuntimeException("Use ScrollIntoView variable to get the single instance of this class.");
        }
    }

    public void scrollIntoView(By elementBy) {
        scrollIntoView(Find.find(elementBy));
    }

    public void scrollIntoView(WebElement element) {
        //JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver();
        ExecuteJavascript.executeJavascript("arguments[0].scrollIntoView(true);", element);
    }
}
