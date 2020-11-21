package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetSibling {
    public static GetSibling GetSibling =
            ThreadLocal.withInitial(GetSibling::new).get();

    private GetSibling() {
        if (GetSibling != null) {
            throw new RuntimeException("Use GetSibling variable to get the single instance of this class.");
        }
    }

    public WebElement getSibling(By parentBy, int siblingIndex) {
        return Find.find(parentBy, By.xpath(String.format("following-sibling::*[%d]", siblingIndex)));
    }

    public WebElement getSibling(WebElement parentElement, int siblingIndex) {
        return Find.find(parentElement, By.xpath(String.format("following-sibling::*[%d]", siblingIndex)));
    }
}
