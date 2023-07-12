package core.web;

import core.web.browser.runner.WebDriverRunner;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.CukeBrowseException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static core.web.commands.BrowserAlert.BrowserAlert;
import static core.web.commands.ExecuteJavascript.ExecuteJavascript;
import static core.web.commands.TakeScreenshot.TakeScreenshot;
import static core.web.commands.RadioOrCheckbox.RadioOrCheckbox;
import static core.web.commands.ByShadowCss.ByShadowCss;
import static core.web.commands.Find.Find;
import static core.web.commands.FindAll.FindAll;
import static core.web.commands.Frame.Frame;
import static core.web.commands.ScrollIntoView.ScrollIntoView;
import static core.web.commands.SetBrowserZoomPercentage.SetBrowserZoomPercentage;
import static core.web.commands.WaitForDocumentToReady.WaitForDocumentToReady;
import static core.web.commands.WindowTabsHandle.WindowTabsHandle;
import static core.web.commands.DownloadFile.DownloadFile;

public class Commander {

    private static final WebDriverRunner webDriverRunner = WebDriverRunner.getInstance();

    /**
     * Get WebDriverRunner.
     */
    public static WebDriverRunner getWebDriverRunner() {
        return webDriverRunner;
    }

    /**
     * Open an browser with provided pages.
     */
    public static void open(String url) {
        webDriverRunner.getWebDriver().get(url);
    }

    /**
     * Open an empty browser (without opening any pages).
     */
    public static void open() {
        webDriverRunner.getWebDriver().get("");
    }

    /**
     * Close the current window, quitting the browser if it's the last window currently open.
     */
    public static void closeWindow() {
        webDriverRunner.closeWindow();
    }

    /**
     * <p>Close the browser if it's open.</p>
     * <br>
     * <p>NB! Method quits this driver, closing every associated window.</p>
     */
    public static void closeWebDriver() {
        webDriverRunner.closeWebDriver();
    }

    /**
     * Navigate browser to specified url
     * @param url Its url to navigate
     */
    public static void navigateTo(String url) {
        webDriverRunner.getWebDriver().navigate().to(url);
        waitForPageToReady();
    }

    /**
     * Reload current page
     */
    public static void browserRefresh() {
        webDriverRunner.getWebDriver().navigate().refresh();
        waitForPageToReady();
    }

    /**
     * Navigate browser back to previous page
     */
    public static void navigateBack() {
        webDriverRunner.getWebDriver().navigate().back();
        waitForPageToReady();
    }

    /**
     * Navigate browser forward to next page
     */
    public static void navigateForward() {
        webDriverRunner.getWebDriver().navigate().forward();
        waitForPageToReady();
    }

    /**
     * Get URL of current page
     */
    public static String getBrowserURL() {
        waitForPageToReady();
        return webDriverRunner.getWebDriver().getCurrentUrl();
    }

    /**
     * @return title of the page
     */
    public static String title() {
        return webDriverRunner.getWebDriver().getTitle();
    }

    /**
     * Maximize browser
     */
    public static void maximizeBrowser() {
        webDriverRunner.maximizeBrowser();
    }

    /**
     * Switch to frame by index
     *
     * @param frameIndex index of frame
     */
    public static void switchToFrameByIndex(int frameIndex) {
        Frame.switchToFrameByIndex(frameIndex);
    }

    /**
     * Switch to frame by Name or Id
     *
     * @param frameNameOrId name or id of frame
     */
    public static void switchToFrameByNameOrId(String frameNameOrId) {
        Frame.switchToFrameByNameOrId(frameNameOrId);
    }

    public static void switchToFrameByFrameElement(CommanderElement frameElement) {
        Frame.switchToFrameByFrameElement(frameElement.getElement());
    }

    /**
     * Switch to Main frame
     */
    public static void switchToMainFrame() {
        Frame.switchToMainFrame();
    }

    /**
     * Switch to Parent frame
     */
    public static void switchToParentFrame() {
        Frame.switchToParentFrame();
    }

    /**
     * To get current browser window id
     */
    public static String getCurrentBrowserWindowId() {
        return WindowTabsHandle.getCurrentWindowHandle();
    }

    /**
     * To get browser window id's
     */
    public static Set<String> getBrowserWindowIds() {
        return WindowTabsHandle.getCurrentWindowHandles();
    }

    /**
     * To close browser current window
     */
    public static void closeCurrentBrowserWindow() {
        WindowTabsHandle.closeCurrentWindow();
    }

    /**
     * To close browser all child window
     */
    public static void closeAllChildBrowserWindows() {
        WindowTabsHandle.closeAllChildWindows();
    }

    /**
     * To switch on specified window id
     */
    public static void switchToWindowById(String windowId) {
        WindowTabsHandle.switchToWindowById(windowId);
    }

    /**
     * To switch on specified window Title
     */
    public static void switchToWindowByTitle(String windowTitle) {
        WindowTabsHandle.switchToWindowByTitle(windowTitle);
    }

    /**
     * To open new tab and switch on it
     */
    public static void openAndSwitchOnNewTab() {
        WindowTabsHandle.openAndSwitchOnNewTab();
    }

    /**
     * To open new window and switch on it
     */
    public static void openAndSwitchOnNewWindow() {
        WindowTabsHandle.openAndSwitchOnNewWindow();
    }

    /**
     * Not recommended. Test should not sleep, but should wait for some condition instead.
     *
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CukeBrowseException("Sleep for "+milliseconds, e);
        }
    }

    /**
     * Test should wait for page to get ready & jQuery get finish.
     */
    public static boolean waitForPageToReady() {
        return WaitForDocumentToReady.waitForPageToReady();
    }

    /**
     * Take the screenshot of current page and save to file fileName.html and fileName.png
     *
     * @param fileName Name of file (with extension) to save HTML and PNG to
     * @return The name of resulting file
     */
    public static File screenshotOfPage(String fileName) {
        return TakeScreenshot.takeScreenshotAsFile(fileName);
    }

    /**
     * Take the screenshot of current page and return bytes of image
     *
     * @return The byte[] of screenshot
     */
    public static byte[] screenshotOfPageAsBytes() {
        return TakeScreenshot.takeScreenshotAsBytes();
    }

    /**
     * Take the screenshot of element and return bytes of image
     *
     * @param elementBy take screenshot of the element
     * @return The byte[] of screenshot
     */
    public static byte[] screenshotOfElementAsBytes(By elementBy) {
        return TakeScreenshot.takeScreenshotAsBytes(find(elementBy).getElement());
    }

    /**
     * Take the screenshot of element and save to file fileName parameter
     *
     * @param fileName         Name of file (with extension) to save HTML and PNG to
     * @param commanderElement to take screenshot of specific element
     * @return The name of resulting file
     */
    public static File screenshotElement(String fileName, CommanderElement commanderElement) {
        return TakeScreenshot.takeScreenshotAsFile(fileName, commanderElement.getElement());
    }

    /**
     * To execute javascript
     *
     * @param jsCode javascript code to run with optional CommanderElement
     */
    public static Object executeJavaScript(String jsCode, CommanderElement... commanderElements) {
        assert commanderElements != null;
        WebElement[] elements = (WebElement[]) Arrays.stream(commanderElements).map(CommanderElement::getElement).toArray();
        return ExecuteJavascript.executeJavascript(jsCode, elements);
    }

    /**
     * Execute javascript to set browser zoom percentage
     *
     * @param browserZoomLevel set zoom percentage of browser
     */
    public static void setBrowserZoomPercentage(int browserZoomLevel) {
        SetBrowserZoomPercentage.setBrowserZoomPercentage(browserZoomLevel);
    }

    /**
     * To reset browser zoom level to 100% by executing javascript
     */
    public static void resetBrowserZoomLevel() {
        SetBrowserZoomPercentage.resetBrowserZoomLevel();
    }

    /**
     * Returns selected element in radio group
     *
     * @return null if nothing selected
     */
    public static CommanderElement getSelectedRadio(By radioField) {
        return new CommanderElement(RadioOrCheckbox.getSelectedRadioOrCheckbox(radioField));
    }

    /**
     * Returns selected element in checkbox group
     *
     * @return null if nothing selected
     */
    public static CommanderElement getSelectedCheckbox(By radioField) {
        return new CommanderElement(RadioOrCheckbox.getSelectedRadioOrCheckbox(radioField));
    }

    /**
     * Returns all selected element in radio group
     *
     * @return CommanderElements for all matching condition
     */
    public CommanderElements getSelectedRadios(By radioField) {
        return new CommanderElements(RadioOrCheckbox.getSelectedRadiosOrCheckboxes(radioField));
    }

    /**
     * Returns all selected element in Checkbox group
     *
     * @return CommanderElements for all matching condition
     */
    public CommanderElements getSelectedCheckboxes(By radioField) {
        return new CommanderElements(RadioOrCheckbox.getSelectedRadiosOrCheckboxes(radioField));
    }

    /**
     * Select radio or checkbox optionally radio or checkbox Text
     */
    public void selectRadioOrCheckbox(By elementBy, String... radioOrCheckboxText) {
        RadioOrCheckbox.selectRadioOrCheckbox(elementBy, radioOrCheckboxText);
    }

    /**
     * Deselect checkbox optionally radio or checkbox Text
     */
    public void deselectCheckbox(By elementBy, String... checkboxText) {
        RadioOrCheckbox.deselectCheckbox(elementBy, checkboxText);
    }

    /**
     * Accept (Click "Yes" or "Ok") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @return actual dialog text
     */
    public static String confirmAlert() {
        return BrowserAlert.acceptAlert();
    }

    /**
     * Accept (Click "Yes" or "Ok") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @param expectedDialogText if not null, check that confirmation dialog displays this message (case-sensitive)
     * @return actual dialog text
     */
    public static String confirmAlertWithMessage(String expectedDialogText) {
        return BrowserAlert.acceptAlert(expectedDialogText);
    }

    /**
     * Dismiss (click "No" or "Cancel") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @return actual dialog text
     */
    public static String dismissAlert() {
        return BrowserAlert.dismissAlert();
    }

    /**
     * Dismiss (click "No" or "Cancel") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @param expectedDialogText if not null, check that confirmation dialog displays this message (case-sensitive)
     * @return actual dialog text
     */
    public static String dismissAlertWithMessage(String expectedDialogText) {
        return BrowserAlert.dismissAlert(expectedDialogText);
    }

    /**
     * AlertMessage in the confirmation dialog .
     *
     * @return actual dialog text
     */
    public static String getAlertMessage() {
        return BrowserAlert.getAlertMessage();
    }

    /**
     * Clear browser cookies.
     * <p>
     * It can be useful e.g. if you are trying to avoid restarting browser between tests
     */
    public static void clearBrowserCookies() {
        webDriverRunner.clearBrowserCache();
    }

    /**
     * Scroll to element identified using By class.
     */
    public static void scrollIntoView(By elementBy) {
        ScrollIntoView.scrollIntoView(elementBy);
    }

    /**
     * Find Element By ShadowCss.
     *
     * @param targetElement    is element on which you want to perform action.
     * @param shadowHost       is the starting shadow host
     * @param innerShadowHosts is inner shadow hosts to reach targetElement
     * @return CommanderElement
     */
    public static CommanderElement findElementByShadowCss(String targetElement, String shadowHost, String... innerShadowHosts) {
        return new CommanderElement(ByShadowCss.findElement(targetElement, shadowHost, innerShadowHosts));
    }

    /**
     * Find Elements By ShadowCss.
     *
     * @param targetElement    is element on which you want to perform action.
     * @param shadowHost       is the starting shadow host
     * @param innerShadowHosts is inner shadow hosts to reach targetElement
     * @return CommanderElements
     */
    public static CommanderElements findElementsByShadowCss(String targetElement, String shadowHost, String... innerShadowHosts) {
        return new CommanderElements(ByShadowCss.findElements(targetElement, shadowHost, innerShadowHosts));
    }

    /**
     * Find Element on page.
     *
     * @param elementBy is used to identify element (using By class) on which you want to perform action.
     * @return CommanderElement
     */
    public static CommanderElement find(By elementBy) {
        return new CommanderElement(Find.find(elementBy));
    }

    /**
     * Find Element on page.
     *
     * @param parentBy        is Parent element (By) used to identify target element as root element.
     * @param targetElementBy is used to identify target element (using By class) on which you want to perform action.
     * @param index           is optional parameter used to get element at specified position.
     * @return CommanderElement
     */
    public static CommanderElement find(By parentBy, By targetElementBy, int... index) {
        return new CommanderElement(Find.find(parentBy, targetElementBy, index));
    }

    /**
     * Find Element on page.
     *
     * @param elementBy is used to identify element (using By class) on which you want to perform action.
     * @return CommanderElements
     */
    public static CommanderElements findAll(By elementBy) {
        return new CommanderElements(FindAll.findAll(elementBy));
    }

    /**
     * Find Element on page.
     *
     * @param parentBy        is Parent element (By) used to identify target element as root element.
     * @param targetElementBy is used to identify target element (using By class) on which you want to perform action.
     * @return CommanderElements
     */
    public static CommanderElements findAll(By parentBy, By targetElementBy) {
        return new CommanderElements(FindAll.findAll(parentBy, targetElementBy));
    }

    /**
     * Wait on page.
     *
     * @param condition    is condition used to wait for specific element or page condition.
     * @param safeWaitFlag is used to specify True or False value which is used to decide if exception should be thrown or not.
     * @return boolean value as per condition
     */
    public static boolean waitUntil(Condition condition, boolean safeWaitFlag) {
        return condition.verifyCondition(safeWaitFlag);
    }

    /**
     * Wait on page.
     *
     * @param condition        is condition used to wait for specific element or page condition.
     * @param timeOutInSeconds is used to specify max time out in seconds.
     * @param safeWaitFlag     is used to specify True or False value which is used to decide if exception should be thrown or not.
     * @param exceptionTypes   is used to specify '.class' exceptions class names to ignore it.
     * @return boolean value as per condition
     */
    @SafeVarargs
    public static boolean waitUntil(Condition condition, long timeOutInSeconds, boolean safeWaitFlag, Class<? extends Throwable>... exceptionTypes) {
        if (exceptionTypes != null) {
            return condition.verifyCondition(timeOutInSeconds, safeWaitFlag, Arrays.asList(exceptionTypes));
        } else {
            return condition.verifyCondition(timeOutInSeconds, safeWaitFlag, null);
        }
    }

    /**
     * Wait on page.
     *
     * @param condition                is condition used to wait for specific element or page condition.
     * @param timeOutInSeconds         is used to specify max time out in seconds.
     * @param pollingIntervalInSeconds is used to specify polling interval in which condition should be check.
     * @param safeWaitFlag             is used to specify True or False value which is used to decide if exception should be thrown or not.
     * @param exceptionTypes           is used to specify '.class' exceptions class names to ignore it.
     * @return boolean value as per condition
     */
    @SafeVarargs
    public static boolean waitUntil(Condition condition, long timeOutInSeconds, long pollingIntervalInSeconds, boolean safeWaitFlag, Class<? extends Throwable>... exceptionTypes) {
        if (exceptionTypes != null) {
            return condition.verifyCondition(timeOutInSeconds, pollingIntervalInSeconds, safeWaitFlag, Arrays.asList(exceptionTypes));
        } else {
            return condition.verifyCondition(timeOutInSeconds, pollingIntervalInSeconds, safeWaitFlag, null);
        }
    }

    /**
     * Retrieves the downloaded file path based on the given file pattern and file extension.
     *
     * @param filePattern The file pattern used to match the downloaded file.
     * @param fileExt The file extension of the downloaded file.
     * @return The file path of the downloaded file.
     */
    public static String getDownloadedFilePath(String filePattern, @NotNull String fileExt) {
        return DownloadFile.getDownloadedFilePath(filePattern, fileExt);
    }

    /**
     * Retrieves the downloaded file based on the given file pattern and file extension and store at targetFilePath.
     *
     * @param filePattern The file pattern used to match the downloaded file.
     * @param fileExt The file extension of the downloaded file.
     * @param targetFilePath The file path where clone of downloaded file should be created.
     * @return The file path of the downloaded file.
     */
    public static File getDownloadedFileFrom(String filePattern, @NotNull String fileExt, @NotNull String targetFilePath) throws IOException {
        return DownloadFile.getDownloadedFileFrom(filePattern, fileExt, targetFilePath);
    }
}
