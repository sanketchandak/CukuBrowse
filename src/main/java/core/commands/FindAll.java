package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static core.commands.Find.Find;

public class FindAll {
    private WebDriver driver;
    public static FindAll FindAll =
            ThreadLocal.withInitial(FindAll::new).get();

    private FindAll() {
        if (FindAll != null) {
            throw new RuntimeException("Use FindAll variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public List<WebElement> findAll(By elementBy) {
        setDriverSession();
        return driver.findElements(elementBy);
    }

    public List<WebElement> findAll(By parentBy, By elementBy) {
        return findAll(Find.find(parentBy), elementBy);
    }

    public List<WebElement> findAll(WebElement parentElement, By elementBy) {
        return parentElement.findElements(elementBy);
    }
}
