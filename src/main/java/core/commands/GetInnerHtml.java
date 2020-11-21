package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetInnerHtml {
    public static GetInnerHtml GetInnerHtml =
            ThreadLocal.withInitial(GetInnerHtml::new).get();

    private GetInnerHtml() {
        if (GetInnerHtml != null) {
            throw new RuntimeException("Use GetInnerHtml variable to get the single instance of this class.");
        }
    }

    public String getInnerHtml(By elementBy) {
        return getInnerHtml(Find.find(elementBy));
    }

    public String getInnerHtml(WebElement element) {
        return element.getAttribute("innerHTML");
    }
}
