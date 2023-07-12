package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;
import static core.web.commands.ExecuteJavascript.ExecuteJavascript;

public class IsImage {
    private static final Logger logger = LoggerFactory.getLogger(IsImage.class);
    public static IsImage IsImage =
            ThreadLocal.withInitial(IsImage::new).get();

    private IsImage() {
        if (IsImage != null) {
            logger.error("Use IsImage variable to get the single instance of this class.");
            throw new CukeBrowseException("Use IsImage variable to get the single instance of this class.");
        }
    }

    public boolean isImage(By elementBy) {
        boolean status = isImage(Find.find(elementBy));
        logger.info(String.format("Is Image: check if '%s' element is image. Actual status: '%s'", elementBy.toString(), status));
        return status;
    }

    public boolean isImage(WebElement element) {
        if (!"img".equalsIgnoreCase(element.getTagName())) {
            logger.error("Is Image: Method isImage() is only applicable for img elements");
            throw new CukeBrowseException("Method isImage() is only applicable for img elements");
        }
        /*JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver();*/
        ExecuteJavascript.executeJavascript("arguments[0].scrollIntoView(true);", element);
        boolean status = (boolean) ExecuteJavascript.executeJavascript("return arguments[0].tagName.toLowerCase() === 'img' && " +
                "arguments[0].complete && " +
                "typeof arguments[0].naturalWidth != 'undefined' && " +
                "arguments[0].naturalWidth > 0", element);
        logger.info(String.format("Is Image: check if '%s' element is image. Actual status: '%s'", element, status));
        return status;
    }
}
