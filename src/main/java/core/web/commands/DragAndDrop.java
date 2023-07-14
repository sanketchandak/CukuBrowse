package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class DragAndDrop {
    private static final Logger logger = LoggerFactory.getLogger(DragAndDrop.class);
    public static DragAndDrop DragAndDrop =
            ThreadLocal.withInitial(DragAndDrop::new).get();
    private final Find find;
    private WebDriver driver;

    private DragAndDrop() {
        find = Find;
        if (DragAndDrop != null) {
            logger.error("Use DragAndDrop variable to get the single instance of this class.");
            throw new CukeBrowseException("Use DragAndDrop variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public void dragAndDropTo(By sourceElementBy, By destinationElementBy) {
        dragAndDropTo(find.findElement(sourceElementBy), find.findElement(destinationElementBy));
        logger.info("Drag And Drop: Drag And Drop From: " + sourceElementBy.toString() + " To: " + destinationElementBy.toString());
    }

    public void dragAndDropTo(WebElement sourceElement, WebElement destinationElement) {
        setDriverSession();
        new Actions(driver).dragAndDrop(sourceElement, destinationElement).perform();
        logger.info("Drag And Drop: Drag And Drop From: " + sourceElement.toString() + " To: " + destinationElement.toString());
    }

    public void dragAndDropTo(By sourceElementBy, int xOffset, int yOffset) {
        dragAndDropTo(find.findElement(sourceElementBy), xOffset, yOffset);
        logger.info("Drag And Drop: Drag And Drop From: " + sourceElementBy.toString() + " To X-Offset: " + xOffset + " And Y-Offset: " + yOffset);
    }

    public void dragAndDropTo(WebElement sourceElement, int xOffset, int yOffset) {
        setDriverSession();
        new Actions(driver).dragAndDropBy(sourceElement, xOffset, yOffset).perform();
        logger.info("Drag And Drop: Drag And Drop From: " + sourceElement.toString() + " To X-Offset: " + xOffset + " And Y-Offset: " + yOffset);
    }
}
