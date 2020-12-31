package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetAncestor {
    public static GetAncestor GetAncestor =
            ThreadLocal.withInitial(GetAncestor::new).get();

    private GetAncestor() {
        if (GetAncestor != null) {
            throw new RuntimeException("Use GetAncestor variable to get the single instance of this class.");
        }
    }

    public WebElement getAncestor(By parentBy, String ancestorPath) {
        return Find.find(parentBy, By.xpath("ancestor::" + ancestorPath));
    }

    public WebElement getAncestor(WebElement parentElement, String ancestorPath) {
        return Find.find(parentElement, By.xpath("ancestor::" + ancestorPath));
    }
}
