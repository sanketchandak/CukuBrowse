package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetParent {
    public static GetParent GetParent =
            ThreadLocal.withInitial(GetParent::new).get();

    private GetParent() {
        if (GetParent != null) {
            throw new RuntimeException("Use GetParent variable to get the single instance of this class.");
        }
    }

    public WebElement getParent(By parentBy) {
        return Find.find(parentBy, By.xpath(".."));
    }

    public WebElement getParent(WebElement parentElement) {
        return Find.find(parentElement, By.xpath(".."));
    }
}
