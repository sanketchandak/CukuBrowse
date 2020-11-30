package core.commands;

import core.browser.runner.WebDriverRunner;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;

public class TakeScreenshot {
    private WebDriver driver;
    public static TakeScreenshot TakeScreenshot =
            ThreadLocal.withInitial(TakeScreenshot::new).get();

    private TakeScreenshot() {
        if (TakeScreenshot != null) {
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
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File takeScreenshotAsFile(WebElement... element) {
        assert element != null;
        setDriverSession();
        return element.length == 0 ?
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE) :
                element[0].getScreenshotAs(OutputType.FILE);
    }

    public String takeScreenshotAsBase64(WebElement... element) {
        assert element != null;
        setDriverSession();
        return element.length == 0 ?
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64) :
                element[0].getScreenshotAs(OutputType.BASE64);
    }

}
