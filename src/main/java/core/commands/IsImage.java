package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;
import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class IsImage {
    public static IsImage IsImage =
            ThreadLocal.withInitial(IsImage::new).get();

    private IsImage() {
        if (IsImage != null) {
            throw new RuntimeException("Use IsImage variable to get the single instance of this class.");
        }
    }

    public boolean isImage(By elementBy) {
        return isImage(Find.find(elementBy));
    }

    public boolean isImage(WebElement element) {
        if (!"img".equalsIgnoreCase(element.getTagName())) {
            throw new IllegalArgumentException("Method isImage() is only applicable for img elements");
        }
        /*JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getInstance().getWebDriver();*/
        ExecuteJavascript.executeJavascript("arguments[0].scrollIntoView(true);", element);
        return (boolean) ExecuteJavascript.executeJavascript("return arguments[0].tagName.toLowerCase() === 'img' && " +
                "arguments[0].complete && " +
                "typeof arguments[0].naturalWidth != 'undefined' && " +
                "arguments[0].naturalWidth > 0", element);
    }
}
