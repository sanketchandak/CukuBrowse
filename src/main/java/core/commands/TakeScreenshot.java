package core.commands;

import core.browser.runner.WebDriverRunner;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TakeScreenshot {
    private static final Logger logger = LoggerFactory.getLogger(TakeScreenshot.class);
    private WebDriver driver;
    public static TakeScreenshot TakeScreenshot =
            ThreadLocal.withInitial(TakeScreenshot::new).get();

    private TakeScreenshot() {
        if (TakeScreenshot != null) {
            logger.error("Use TakeScreenshot variable to get the single instance of this class.");
            throw new RuntimeException("Use TakeScreenshot variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public File takeScreenshotAsFile(String fileNameWithPath, WebElement... element) {
        try {
            File file = new File(fileNameWithPath);
            FileUtils.copyFile(takeScreenshotAsFile(element), file);
            logger.info(String.format("Take Screenshot As File: take screenshot and save it at '%s'", fileNameWithPath));
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File takeScreenshotAsFile(WebElement... element) {
        assert element != null;
        setDriverSession();
        logger.info(String.format("Take Screenshot As File: take screenshot of element '%s' as file", (element.length != 0) ? Arrays.stream(element).map(Object::toString).collect(Collectors.toList()) : ""));
        return element.length == 0 ?
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE) :
                element[0].getScreenshotAs(OutputType.FILE);
    }

    public String takeScreenshotAsBase64(WebElement... element) {
        assert element != null;
        setDriverSession();
        logger.info(String.format("Take Screenshot As Base64: take screenshot of element '%s' as Base64", (element.length != 0) ? Arrays.stream(element).map(Object::toString).collect(Collectors.toList()) : ""));
        return element.length == 0 ?
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64) :
                element[0].getScreenshotAs(OutputType.BASE64);
    }

}
