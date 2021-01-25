package core;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import static core.commands.BrowserAlert.BrowserAlert;
import static core.commands.ExecuteJavascript.ExecuteJavascript;
import static core.commands.TakeScreenshot.TakeScreenshot;
import static core.commands.RadioOrCheckbox.RadioOrCheckbox;
import static core.commands.ByShadowCss.ByShadowCss;
import static core.commands.Find.Find;
import static core.commands.FindAll.FindAll;
import static core.commands.Frame.Frame;
import static core.commands.ScrollIntoView.ScrollIntoView;
import static core.commands.SetBrowserZoomPercentage.SetBrowserZoomPercentage;
import static core.commands.WaitForDocumentToReady.WaitForDocumentToReady;
import static core.commands.WindowTabsHandle.WindowTabsHandle;

public class Commander {

    private static final WebDriverRunner webDriverRunner = WebDriverRunner.getInstance();

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
     * Reload current page
     */
    public static void refresh() {
        webDriverRunner.getWebDriver().navigate().refresh();
    }

    /**
     * Navigate browser back to previous page
     */
    public static void back() {
        webDriverRunner.getWebDriver().navigate().back();
    }

    /**
     * Navigate browser forward to next page
     */
    public static void forward() {
        webDriverRunner.getWebDriver().navigate().forward();
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
    public static void switchToWindow(String windowId) {
        WindowTabsHandle.switchToWindow(windowId);
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Test should should wait for page to get ready & jQuery get finish.
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
    public static File screenshot(String fileName) {
        return TakeScreenshot.takeScreenshotAsFile(fileName);
    }

    /**
     * Take the screenshot of current page and save to file fileName parameter
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
    public void setBrowserZoomPercentage(String browserZoomLevel) {
        SetBrowserZoomPercentage.setBrowserZoomPercentage(browserZoomLevel);
    }

    /**
     * To reset browser zoom level to 100% by executing javascript
     */
    public void resetBrowserZoomLevel() {
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
    public static String confirm() {
        return BrowserAlert.acceptAlert();
    }

    /**
     * Accept (Click "Yes" or "Ok") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @param expectedDialogText if not null, check that confirmation dialog displays this message (case-sensitive)
     * @return actual dialog text
     */
    public static String confirm(String expectedDialogText) throws Exception {
        return BrowserAlert.acceptAlert(expectedDialogText);
    }

    /**
     * Dismiss (click "No" or "Cancel") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @return actual dialog text
     */
    public static String dismiss() {
        return BrowserAlert.dismissAlert();
    }

    /**
     * Dismiss (click "No" or "Cancel") in the confirmation dialog ( 'alert' or 'confirm').
     *
     * @param expectedDialogText if not null, check that confirmation dialog displays this message (case-sensitive)
     * @return actual dialog text
     */
    public static String dismiss(String expectedDialogText) throws Exception {
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
     */
    public static void waitUntil(Condition condition, boolean safeWaitFlag) throws Exception {
        condition.verifyCondition(safeWaitFlag);
    }

    /**
     * Wait on page.
     *
     * @param condition        is condition used to wait for specific element or page condition.
     * @param timeOutInSeconds is used to specify max time out in seconds.
     * @param safeWaitFlag     is used to specify True or False value which is used to decide if exception should be thrown or not.
     * @param exceptionTypes   is used to specify '.class' exceptions class names to ignore it.
     */
    @SafeVarargs
    public static void waitUntil(Condition condition, long timeOutInSeconds, boolean safeWaitFlag, Class<? extends Throwable>... exceptionTypes) throws Exception {
        if (exceptionTypes != null) {
            condition.verifyCondition(timeOutInSeconds, safeWaitFlag, Arrays.asList(exceptionTypes));
        } else {
            condition.verifyCondition(timeOutInSeconds, safeWaitFlag, null);
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
     */
    @SafeVarargs
    public static void waitUntil(Condition condition, long timeOutInSeconds, long pollingIntervalInSeconds, boolean safeWaitFlag, Class<? extends Throwable>... exceptionTypes) throws Exception {
        if (exceptionTypes != null) {
            condition.verifyCondition(timeOutInSeconds, pollingIntervalInSeconds, safeWaitFlag, Arrays.asList(exceptionTypes));
        } else {
            condition.verifyCondition(timeOutInSeconds, pollingIntervalInSeconds, safeWaitFlag, null);
        }
    }
}
