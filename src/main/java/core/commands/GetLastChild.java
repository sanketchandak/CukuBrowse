package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetLastChild {
    public static GetLastChild GetLastChild =
            ThreadLocal.withInitial(GetLastChild::new).get();

    private GetLastChild() {
        if (GetLastChild != null) {
            throw new RuntimeException("Use GetLastChild variable to get the single instance of this class.");
        }
    }

    public WebElement getLastChild(By parentBy) {
        return Find.find(parentBy, By.xpath("*[last()]"));
    }

    public WebElement getLastChild(WebElement parentElement) {
        return Find.find(parentElement, By.xpath("*[last()]"));
    }
}
