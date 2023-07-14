package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.web.commands.Find.Find;
import static core.web.commands.ExecuteJavascript.ExecuteJavascript;

public class ScrollIntoView {
    private static final Logger logger = LoggerFactory.getLogger(ScrollIntoView.class);
    public static ScrollIntoView ScrollIntoView =
            ThreadLocal.withInitial(ScrollIntoView::new).get();

    private ScrollIntoView() {
        if (ScrollIntoView != null) {
            logger.error("Use ScrollIntoView variable to get the single instance of this class.");
            throw new RuntimeException("Use ScrollIntoView variable to get the single instance of this class.");
        }
    }

    public void scroll(By elementBy) {
        scroll(Find.findElement(elementBy));
        logger.info(String.format("Scroll Into View: scroll to '%s' element.", elementBy.toString()));
    }

    public void scroll(WebElement element) {
        //JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver();
        ExecuteJavascript.execute("arguments[0].scrollIntoView(true);", element);
        logger.info(String.format("Scroll Into View: scroll to '%s' element.", element.toString()));
    }
}
