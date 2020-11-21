package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetPreceding {
    public static GetPreceding GetPreceding =
            ThreadLocal.withInitial(GetPreceding::new).get();

    private GetPreceding() {
        if (GetPreceding != null) {
            throw new RuntimeException("Use GetPreceding variable to get the single instance of this class.");
        }
    }

    public WebElement getPreceding(By parentBy, int siblingIndex) {
        return Find.find(parentBy, By.xpath(String.format("preceding-sibling::*[%d]", siblingIndex)));
    }

    public WebElement getPreceding(WebElement parentElement, int siblingIndex) {
        return Find.find(parentElement, By.xpath(String.format("preceding-sibling::*[%d]", siblingIndex)));
    }
}
