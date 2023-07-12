package core.web.commands;

import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static core.web.commands.ExecuteJavascript.ExecuteJavascript;
import static core.web.commands.WindowTabsHandle.WindowTabsHandle;
import static core.web.commands.FindAll.FindAll;
import static core.web.commands.Find.Find;
import static core.web.commands.GetAttribute.GetAttribute;

/**
 * The {@code DownloadFile} class provides methods to interact with downloaded files in the browser.
 * It supports retrieving the path of the downloaded file and obtaining the file itself.
 */
public class DownloadFile {
    private static final Logger logger = LoggerFactory.getLogger(DownloadFile.class);
    public static DownloadFile DownloadFile =
            ThreadLocal.withInitial(DownloadFile::new).get();

    private WebDriver driver;
    private static final String CHROME_DOWNLOAD_PATH = "chrome://downloads/";
    private static final String EDGE_DOWNLOAD_PATH = "edge://downloads/";

    /**
     * Constructs a new instance of {@code DownloadFile}.
     * It throws an exception if called directly, as the class is designed to be accessed through the singleton instance.
     */
    private DownloadFile() {
        if (DownloadFile != null) {
            logger.error("Use DownloadFile variable to get the single instance of this class.");
            throw new RuntimeException("Use DownloadFile variable to get the single instance of this class.");
        }
    }

    /**
     * Sets the WebDriver session to interact with the browser.
     */
    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    /**
     * Retrieves the downloaded file path based on the file pattern and file extension.
     *
     * @param filePattern The pattern to match the file name (can be null or empty for any file name).
     * @param fileExt The file extension to match.
     * @return The path of the downloaded file matching the criteria.
     * @throws NullPointerException if the fileExt parameter is null.
     */
    public String getDownloadedFilePath(String filePattern, String fileExt) {
        if (fileExt == null) {
            throw new NullPointerException("@param fileExt can not be null.");
        }
        setDriverSession();
        String path = "";
        String browserName = ((RemoteWebDriver) ((WrapsDriver) driver).getWrappedDriver())
                .getCapabilities()
                .getBrowserName()
                .toLowerCase();

        if (browserName.contains("chrome")) {
            String script = "return document.querySelector('downloads-manager).shadowRoot.querySelector('#downloadsList').items.filter(e => e.state === 'COMPLETE').map(e => e.filePath || e.file_path || e.fileUrl || e.file_url)";
            if( !driver.getCurrentUrl().startsWith(CHROME_DOWNLOAD_PATH)) {
                ExecuteJavascript.executeJavascript("window.open()");
                Set<String> currentWindowHandles = WindowTabsHandle.getCurrentWindowHandles();
                for (String windowHandle : currentWindowHandles) {
                    driver.switchTo().window(windowHandle);
                    if(driver.getTitle().isEmpty()) {
                        driver.get(CHROME_DOWNLOAD_PATH);
                        break;
                    }
                }
            }

            String filesList = ExecuteJavascript.executeJavascript(script).toString();
            filesList = filesList.substring(1, filesList.length() - 1).replace('\\', '/');
            String[] filePaths = filesList.split(",");
            for (String filePath : filePaths) {
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                if ((filePattern != null && !filePattern.isEmpty()) ? fileName.startsWith(filePattern) && fileName.endsWith(fileExt) : fileName.endsWith(fileExt))  {
                    path = filePath.trim();
                    break;
                }
            }
        } else if (browserName.contains("edge")) {
            if( !driver.getCurrentUrl().startsWith(EDGE_DOWNLOAD_PATH)) {
                ExecuteJavascript.executeJavascript("window.open()");
                Set<String> currentWindowHandles = WindowTabsHandle.getCurrentWindowHandles();
                for (String windowHandle : currentWindowHandles) {
                    driver.switchTo().window(windowHandle);
                    if(driver.getTitle().isEmpty()) {
                        driver.get(EDGE_DOWNLOAD_PATH);
                        break;
                    }
                }
            }
            String fileXpath;
            if (filePattern != null && !filePattern.isEmpty()) {
                fileXpath = "//img[starts-with(@aria-label,'"+filePattern+"')][contains(@aria-label,'"+fileExt+"')]/ancestor::*[@role='listitem']";
            } else {
                fileXpath = "//img[contains(@aria-label,'"+fileExt+"') or contains(@aria-label,'."+fileExt+"')]/ancestor::*[@role='listitem']";
            }
            List<WebElement> fileWebElementList = FindAll.findAll(By.xpath(fileXpath));
            if (!fileWebElementList.isEmpty()) {
                WebElement firstFileElement = fileWebElementList.get(0);
                Condition.visible(Find.find(firstFileElement, By.xpath("//*[contains(text(), 'Show in folder')]"))).verifyCondition(false);
                String srcOfFirstFileElement;
                if (filePattern != null && !filePattern.isEmpty()) {
                    srcOfFirstFileElement = GetAttribute.getAttribute(By.xpath("//img[starts-with(@aria-label,'"+filePattern+"')][contains(@aria-label,'"+fileExt+"')])"),"src");
                } else {
                    srcOfFirstFileElement = GetAttribute.getAttribute(By.xpath("//img[contains(@aria-label,'"+fileExt+"') or contains(@aria-label,'."+fileExt+"')]"),"src");
                }
                try {
                    srcOfFirstFileElement = URLDecoder.decode(srcOfFirstFileElement, "UTF-8");
                } catch (UnsupportedEncodingException ignore) {
                }
                path = srcOfFirstFileElement.substring(srcOfFirstFileElement.indexOf("path=")+5, srcOfFirstFileElement.indexOf("&scale"));
            }
        }
        return path;
    }

    /**
     * Retrieves the downloaded file from the browser and saves it to the target file path.

     * @param filePattern The pattern to match the file name (can be null or empty for any file name).
     * @param fileExt The file extension to match.
     * @param targetFilePath The path where the downloaded file should be saved.
     * @return The downloaded file as a {@code File} object.
     * @throws IOException if an I/O error occurs while saving the file.
     * @throws NullPointerException if the targetFilePath parameter is null.
     */
    public File getDownloadedFileFrom(String filePattern, String fileExt, String targetFilePath) throws IOException {
        if (targetFilePath == null) {
            throw new NullPointerException("@param targetFilePath can not be null.");
        }
        String currentWindowHandle = WindowTabsHandle.getCurrentWindowHandle();
        String filePath = getDownloadedFilePath(filePattern, fileExt);
        if (filePath.contains("%20")) {
            filePath = filePath.replace("%20", " ");
        }

        String script = "var input = window.document.createElement('INPUT'); " +
                "input.setAttribute('type', 'file'); " +
                "input.setAttribute('id', 'downloadedFileContent'); " +
                "input.hidden = true; " +
                "input.onchange = function (e) {e.stopPropagation() }; " +
                "return window.document.documentElement.appendChild(input); ";
        WebElement fileContent = (WebElement) ExecuteJavascript.executeJavascript(script);
        fileContent.sendKeys(filePath);

        String asyncScript = "var input = arguments[0], callback = arguments[1]; " +
                "var reader = new FileReader(); " +
                "reader.onload = function (ev) { callback(reader.result) }; " +
                "reader.onerror = function (ex) { callback(ex.message) }; " +
                "reader.readAsDataURL(input.files[0]); " +
                "input.remove(); ";
        String content = ExecuteJavascript.executeJavascript(asyncScript, fileContent).toString();
        int fromIndex = content.indexOf("base64,") + 7;
        content = content.substring(fromIndex);

        Files.write(Paths.get(targetFilePath), DatatypeConverter.parseBase64Binary(content));
        File file = new File(targetFilePath);
        if (("Downloads").equals(driver.switchTo().window(driver.getWindowHandle()).getTitle())) {
            WindowTabsHandle.closeCurrentWindow();
            WindowTabsHandle.switchToWindowById(currentWindowHandle);
        }
        return file;
    }


}
