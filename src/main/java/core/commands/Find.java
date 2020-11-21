package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Find {
    private WebDriver driver;
    public static Find Find =
            ThreadLocal.withInitial(Find::new).get();

    private Find() {
        if (Find != null) {
            throw new RuntimeException("Use Find variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public WebElement find(By elementBy) {
        setDriverSession();
        return driver.findElement(elementBy);
    }

    public WebElement find(By parentBy, By elementBy, int... index) {
        setDriverSession();
        return find(driver.findElement(parentBy), elementBy, index);
    }

    public WebElement find(WebElement parentElement, By elementBy, int... index) {
        assert index != null;
        return index.length == 0 ?
                parentElement.findElement(elementBy) :
                parentElement.findElements(elementBy).get(index[0]);
    }
}
