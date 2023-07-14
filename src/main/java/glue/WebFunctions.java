package glue;

import core.web.CommanderElement;
import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import core.web.commands.ClickType;
import core.web.commands.HoverType;
import io.cucumber.java.Scenario;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;
import utils.Helper;
import utils.service.ServiceHooks;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static core.ElementImporter.ElementImporter;
import static core.web.Commander.*;

public class WebFunctions {

    private static final Logger logger = LoggerFactory.getLogger(WebFunctions.class);
    public static WebFunctions WebFunctions =
            ThreadLocal.withInitial(WebFunctions::new).get();

    private WebFunctions() {
        if (WebFunctions != null) {
            logger.error("Use WebFunctions variable to get the single instance of this class.");
            throw new RuntimeException("Use WebFunctions variable to get the single instance of this class.");
        }
    }

    public By normalize_element(String elementName) {
        logger.info("Normalize element: " + elementName);
        return ElementImporter.getElement(elementName);
    }

    public void wait_for_page_to_get_ready() {
        logger.info("Wait for page to get ready.");
        waitForPageToReady();
    }

    public void I_accept_alert() {
        logger.info("Accept alert.");
        confirmAlert();
    }

    public void I_dismiss_alert() {
        logger.info("Dismiss alert.");
        dismissAlert();
    }

    public void I_accept_alert_with_message(String expectedAlertMessage) {
        try {
            logger.info("Accept alert having '" + expectedAlertMessage + "' message.");
            confirmAlertWithMessage(expectedAlertMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void I_dismiss_alert_with_message(String expectedAlertMessage) {
        try {
            logger.info("Dismiss alert having '" + expectedAlertMessage + "' message.");
            dismissAlertWithMessage(expectedAlertMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean alert_window_should_be_displayed(boolean alertShouldBe) {
        logger.info("Alert window should " + (alertShouldBe ? "" : "not") + " be displayed.");
        Condition condition = Condition.alertPresence(alertShouldBe);
        return waitUntil(condition, 30, false);
    }

    public void alert_window_should_be_displayed_with_text(String alertMessage) {
        logger.info("Alert should be displayed with '" + alertMessage + "' message.");
        alert_window_should_be_displayed(true);
        assert getAlertMessage().trim().equals(alertMessage.trim());
    }

    public void I_clear_browser_cookies() {
        logger.info("Clear browser cookies.");
        clearBrowserCookies();
    }

    public void I_navigate_to_url(String url) {
        logger.info("Navigate to url: " + url);
        navigateTo(url);
    }

    public void I_navigate_back() {
        logger.info("Navigate to previous page.");
        navigateBack();
    }

    public void I_navigate_forward() {
        logger.info("Navigate forward to next page.");
        navigateForward();
    }

    public void I_refresh_current_web_page() {
        logger.info("Refresh current web page.");
        browserRefresh();
    }

    public void I_should_be_redirected_to(String expectedURL) {
        logger.info("Should be redirected to: " + expectedURL);
        assert getBrowserURL().equals(expectedURL);
    }

    public void I_enter_text_in_element(By elementBy, String textToEnter) {
        logger.info("Enter " + textToEnter + " in element: " + elementBy);
        CommanderElement commanderElement = find(elementBy);
        commanderElement.clearBox();
        commanderElement.sendKeys(textToEnter);
    }

    public void I_select_with_or_having_text_from_select_dropdown(String withOrHaving, By elementBy, String dropdownValue) {
        logger.info("Select dropdown " + withOrHaving + " text '" + dropdownValue + "' in element: " + elementBy);
        try {
            if (withOrHaving.trim().equalsIgnoreCase("with")) {
                String[] dropdownValues = {dropdownValue};
                find(elementBy).selectDropdownOptionsByTexts(dropdownValues);
            } else {
                find(elementBy).selectOptionsContainingTexts(dropdownValue);
            }
        } catch (Exception e) {
            logger.error("Failed to select dropdown " + withOrHaving + " text '" + dropdownValue + "' in element: " + elementBy + " ", e);
            e.printStackTrace();
        }
    }

    public boolean current_URL_should_be_contain(String operationBeOrContain, String expectedURL, boolean expectedToBeOrContain) {
        logger.info("URL should " + (expectedToBeOrContain ? "" : "not ") + operationBeOrContain + " " + expectedURL);
        Condition condition;
        if (operationBeOrContain.trim().equalsIgnoreCase("be")) {
            condition = Condition.urlIs(expectedURL, expectedToBeOrContain);
        } else if (operationBeOrContain.trim().equalsIgnoreCase("contain")) {
            condition = Condition.urlContains(expectedURL, expectedToBeOrContain);
        } else {
            logger.error("Invalid operation parameter. Valid values are: ['be','contain']. Actual value passed:" + operationBeOrContain);
            throw new CukeBrowseException("Invalid operation parameter. Valid values are: ['be','contain']. Actual value passed:" + operationBeOrContain);
        }
        return waitUntil(condition, 30, false);
    }

    public boolean page_title_should_be_contain(String operationBeOrContain, String expectedTitle, boolean expectedToBeOrContain) {
        logger.info("Page title should " + (expectedToBeOrContain ? "" : "not ") + operationBeOrContain + " " + expectedTitle);
        Condition condition;
        if (operationBeOrContain.trim().equalsIgnoreCase("be")) {
            condition = Condition.titleIs(expectedTitle, expectedToBeOrContain);
        } else if (operationBeOrContain.trim().equalsIgnoreCase("contain")) {
            condition = Condition.titleContains(expectedTitle, expectedToBeOrContain);
        } else {
            logger.error("Invalid operation parameter. Valid values are: ['be','contain']. Actual value passed:" + operationBeOrContain);
            throw new CukeBrowseException("Invalid operation parameter. Valid values are: ['be','contain']. Actual value passed:" + operationBeOrContain);
        }
        return waitUntil(condition, 30, false);
    }

    public boolean element_should_contain_match_have_text(By elementBy, String operationContainOrMatch, String expectedText, boolean expectedContainHaveOrMatch) {
        logger.info("Element " + elementBy + " should " + (expectedContainHaveOrMatch ? "" : "not ") + operationContainOrMatch + " " + expectedText);
        Condition condition;
        if (operationContainOrMatch.trim().equalsIgnoreCase("match")) {
            condition = Condition.exactText(elementBy, expectedText, expectedContainHaveOrMatch);
        } else if (operationContainOrMatch.trim().equalsIgnoreCase("contain")) {
            condition = Condition.containsCaseSensitiveText(elementBy, expectedText, expectedContainHaveOrMatch);
        } else {
            logger.error("Invalid operation parameter. Valid values are: ['match','contain']. Actual value passed:" + operationContainOrMatch);
            throw new CukeBrowseException("Invalid operation parameter. Valid values are: ['match','contain']. Actual value passed:" + operationContainOrMatch);
        }
        return waitUntil(condition, 30, false);
    }

    public boolean elements_should_contain_text(By elementBy, String expectedText, boolean expectedContain) {
        logger.info("Elements " + elementBy + " should " + (expectedContain ? "" : "not ") + " contain " + expectedText);
        Set<Boolean> flags = findAll(elementBy).getElements().stream().
                map(ce -> ce.getText().contains(expectedText)).
                collect(Collectors.toSet());
        if (expectedContain) {
            return !flags.contains(false);
        } else {
            return !flags.contains(true);
        }
    }

    public void I_click_on_element(By elementBy, ClickType ct) {
        logger.info(ct + " click on element " + elementBy);
        find(elementBy).clickOn(ct);
    }

    public void I_press_enter_on_element(By elementBy) {
        logger.info("Press enter on element " + elementBy);
        find(elementBy).pressEnter();
    }

    public void I_press_tab_on_element(By elementBy) {
        logger.info("Press tab on element " + elementBy);
        find(elementBy).pressEnter();
    }

    public void I_optionally_click_on_element(By elementBy, ClickType ct) {
        logger.info(ct + " optionally click on element " + elementBy);
        try {
            I_click_on_element(elementBy, ct);
        } catch (Exception e) {
            logger.error("Failed with I optionally " + ct + " on element: " + elementBy + ". Exception Details:" + Arrays.toString(e.getStackTrace()));
            throw new CukeBrowseException("Failed with I optionally " + ct + " on element: " + elementBy + ". Exception Details:" + Arrays.toString(e.getStackTrace()), e);
        }
    }

    public void I_click_on_link_with_text(String linkName, ClickType ct, boolean like) {
        logger.info(ct + " on click " + (like ? "exact link" : "partial link") + " on link name " + linkName);
        By elementBy;
        if (like) {
            elementBy = By.linkText(Helper.removeQuotes(linkName));
        } else {
            elementBy = By.partialLinkText(Helper.removeQuotes(linkName));
        }
        I_click_on_element(elementBy, ct);
    }

    public void I_deselect_option_with_text_from_select_dropdown(By dropdownElementBy, String dropdownValue) {
        logger.info("Deselect dropdown option with text " + dropdownValue + " from element " + dropdownElementBy);
        String[] dropdownValues = {dropdownValue};
        find(dropdownElementBy).deselectDropdownOptionsByTexts(dropdownValues);
    }

    public void I_deselect_following_options_with_text_from_select_dropdown(By dropdownElementBy, List<String> dropdownTexts) {
        logger.info("Deselect options with text " + dropdownTexts + " from element " + dropdownElementBy);
        find(dropdownElementBy).deselectDropdownOptionsByTexts(dropdownTexts.toArray(new String[0]));
    }

    public ClickType getClickType(String clickOption) {
        logger.info("Get click type where click option is " + clickOption);
        switch (clickOption) {
            case "click":
                return ClickType.DEFAULT;
            case "JS click":
                return ClickType.JSClick;
            case "JS double click":
                return ClickType.JSDoubleClick;
            case "JS right click":
                return ClickType.JSRightClick;
            case "right click":
                return ClickType.ActionRightClick;
            case "double click":
                return ClickType.ActionDoubleClick;
        }
        return null;
    }

    public void I_select_with_or_having_text_from_dropdown_and_option(String withOrHaving, By dropdownElementBy, By optionsElementBy, String dropdownValue) {
        logger.info("Select dropdown " + withOrHaving + " text '" + dropdownValue + "' in element: " + dropdownElementBy + " and options by " + optionsElementBy);
        try {
            if (withOrHaving.trim().equalsIgnoreCase("with")) {
                String[] dropdownValues = {dropdownValue};
                find(dropdownElementBy).selectDropdownOptionsByTexts(dropdownValues, optionsElementBy);
            } else {
                find(dropdownElementBy).selectOptionsContainingTexts(dropdownValue, optionsElementBy);
            }
        } catch (Exception e) {
            logger.error("Failed to select dropdown " + withOrHaving + " text '" + dropdownValue + "' in element: " + dropdownElementBy + " and options by " + optionsElementBy);
            throw new CukeBrowseException("Failed to select dropdown " + withOrHaving + " text '" + dropdownValue + "' in element: " + dropdownElementBy + " and options by " + optionsElementBy + ".", e);
        }
    }

    public void I_select_following_options_with_text_from_dropdown(By dropdownElementBy, List<String> dropdownTexts) {
        logger.info("Select dropdown options " + dropdownTexts + "' from element: " + dropdownElementBy);
        try {
            find(dropdownElementBy).selectDropdownOptionsByTexts(dropdownTexts.toArray(new String[0]));
        } catch (Exception e) {
            logger.error("Failed to select dropdown options " + dropdownTexts + " from element: " + dropdownElementBy);
            throw new CukeBrowseException("Failed to select dropdown options " + dropdownTexts + " from element: " + dropdownElementBy + ".", e);
        }
    }

    public void I_deselect_all_options_of_dropdown(By dropdownElementBy) {
        logger.info("Deselect all options of dropdown.");
        find(dropdownElementBy).deselectAllDropdownOptions();
    }

    public void I_drag_drop_element_to_other_element(By dragElementBy, By dropElementBy) {
        logger.info("Drag element: '" + dragElementBy + "' and drop to element: " + dropElementBy);
        CommanderElement dropCommanderElement = find(dropElementBy);
        find(dragElementBy).dragAndDropTo(dropCommanderElement);
    }

    public void I_drag_drop_element_to_offset(By dragElementBy, int xOffset, int yOffset) {
        logger.info("Drag element: '" + dragElementBy + "' and drop to x-offset: " + xOffset + " y-offset: " + yOffset);
        find(dragElementBy).dragAndDropTo(xOffset, yOffset);
    }

    public void I_mouse_over_on_element(By mouseoverElementBy, HoverType hoverType) {
        logger.info("Mouse over as " + hoverType + " on element: " + mouseoverElementBy);
        find(mouseoverElementBy).hover(hoverType);
    }

    public void I_scroll_element_in_view(By scrollElementBy) {
        logger.info("Scroll element: '" + scrollElementBy + "' in viewport.");
        scrollIntoView(scrollElementBy);
    }

    public void I_set_browser_zoom_percentage(int zoomPercentage) {
        logger.info("Set browser zoom percentage to " + zoomPercentage);
        setBrowserZoomPercentage(zoomPercentage);
    }

    public void I_reset_browser_zoom_level() {
        logger.info("I reset browser zoom level");
        resetBrowserZoomLevel();
    }

    public void I_switch_to_frame_by_name_or_id(String frameNameOrId) {
        logger.info("Switch to frame by name or id as " + frameNameOrId);
        switchToMainFrame();
        switchToFrameByNameOrId(frameNameOrId);
    }

    public void I_switch_to_frame_by_element(By frameElementBy) {
        logger.info("Switch to frame by element as " + frameElementBy);
        switchToMainFrame();
        switchToFrameByFrameElement(find(frameElementBy));
    }

    public void I_switch_to_frame_by_index(int frameIndex) {
        logger.info("Switch to frame by index as " + frameIndex);
        switchToMainFrame();
        switchToFrameByIndex(frameIndex);
    }

    public void I_switch_to_parent_frame() {
        logger.info("Switch to parent frame.");
        switchToMainFrame();
    }

    public void I_switch_to_window_by_title(String windowTitle) {
        logger.info("Switch to window by title: " + windowTitle);
        switchToWindowByTitle(windowTitle);
    }

    public void I_open_new_tab_and_switch_on_it() {
        logger.info("Open new tab and switch on it");
        openAndSwitchOnNewTab();
    }

    public void I_open_browser_new_window_and_switch_on_it() {
        logger.info("Open browser new window and switch on it");
        openAndSwitchOnNewWindow();
    }

    public void I_close_all_child_browser_windows() {
        logger.info("Close all child browser windows.");
        closeAllChildBrowserWindows();
    }

    public void I_close_current_browser_window() {
        logger.info("Close all current browser window.");
        closeCurrentBrowserWindow();
    }

    public void I_wait_for_milliseconds(long toMillis) {
        logger.info("Wait for " + toMillis + " milliseconds.");
        sleep(TimeUnit.MILLISECONDS.toSeconds(toMillis));
    }

    public void I_capture_and_attach_screenshot_of_page() {
        logger.info("Capture and attach screenshot of the page.");
        Scenario scenario = ServiceHooks.getCurrentScenario();
        WebDriverRunner webDriverRunner = getWebDriverRunner();
        if (webDriverRunner.hasWebDriverStarted()) {
            scenario.attach(screenshotOfPageAsBytes(),
                    "image/png",
                    scenario.getId() + Helper.getTimestamp());
        }
    }

    public void I_capture_and_attach_screenshot_of_element(By screenshotElementBy) {
        logger.info("Capture and attach screenshot of the element: " + screenshotElementBy);
        Scenario scenario = ServiceHooks.getCurrentScenario();
        WebDriverRunner webDriverRunner = getWebDriverRunner();
        if (webDriverRunner.hasWebDriverStarted()) {
            scenario.attach(screenshotOfElementAsBytes(screenshotElementBy),
                    "image/png",
                    scenario.getId() + Helper.getTimestamp());
        }
    }

    public boolean element_should_be_displayed(By displayElementBy, boolean shouldDisplay) {
        logger.info("Element: " + displayElementBy + " should " + (shouldDisplay ? "" : "not") + " display");
        Condition condition = shouldDisplay ? Condition.visible(displayElementBy) : Condition.invisible(displayElementBy);
        return waitUntil(condition, 30, false);
    }

    public boolean element_text_should_be_with_polling(By textElementBy, String expectedText, boolean shouldHave, long maxTimeInterval, TimeUnit maxTimeUnitMinuteSecondsMilliseconds, long pollingTimeInterval, TimeUnit pollingTimeUnitMinuteSecondsMilliseconds) {
        logger.info("Element: " + textElementBy + " should " + (shouldHave ? "" : "not") + " have text'" + expectedText + "' in " + maxTimeInterval + "(" + maxTimeUnitMinuteSecondsMilliseconds + ") and polling interval of " + pollingTimeInterval + "(" + pollingTimeUnitMinuteSecondsMilliseconds + ")");
        Condition condition = Condition.exactText(textElementBy, expectedText, shouldHave);
        maxTimeInterval = Helper.convertTimeInSeconds(maxTimeInterval, maxTimeUnitMinuteSecondsMilliseconds);
        pollingTimeInterval = Helper.convertTimeInSeconds(pollingTimeInterval, pollingTimeUnitMinuteSecondsMilliseconds);
        return waitUntil(condition, maxTimeInterval, pollingTimeInterval, false);
    }

    public boolean element_should_be_displayed_with_polling(By displayElementBy, boolean shouldDisplay, long maxTimeInterval, TimeUnit maxTimeUnitMinuteSecondsMilliseconds, long pollingTimeInterval, TimeUnit pollingTimeUnitMinuteSecondsMilliseconds) {
        logger.info("Element: " + displayElementBy + " should " + (shouldDisplay ? "" : "not") + " be display in " + maxTimeInterval + "(" + maxTimeUnitMinuteSecondsMilliseconds + ") and polling interval of " + pollingTimeInterval + "(" + pollingTimeUnitMinuteSecondsMilliseconds + ")");
        Condition condition = shouldDisplay ? Condition.visible(displayElementBy) : Condition.invisible(displayElementBy);
        maxTimeInterval = Helper.convertTimeInSeconds(maxTimeInterval, maxTimeUnitMinuteSecondsMilliseconds);
        pollingTimeInterval = Helper.convertTimeInSeconds(pollingTimeInterval, pollingTimeUnitMinuteSecondsMilliseconds);
        return waitUntil(condition, maxTimeInterval, pollingTimeInterval, false);
    }

    public boolean element_should_be_clickable(By clickableElementBy, boolean shouldClickable) {
        logger.info("Element: " + clickableElementBy + " should " + (shouldClickable ? "" : "not") + " be clickable.");
        Condition condition = shouldClickable ? Condition.visible(clickableElementBy) : Condition.invisible(clickableElementBy);
        return waitUntil(condition, 30, false);
    }

    public boolean element_should_be_clickable_with_polling(By clickableElementBy, boolean shouldDisplay, long maxTimeInterval, TimeUnit maxTimeUnitMinuteSecondsMilliseconds, long pollingTimeInterval, TimeUnit pollingTimeUnitMinuteSecondsMilliseconds) {
        logger.info("Element: " + clickableElementBy + " should " + (shouldDisplay ? "" : "not") + " be clickable in " + maxTimeInterval + "(" + maxTimeUnitMinuteSecondsMilliseconds + ") and polling interval of " + pollingTimeInterval + "(" + pollingTimeUnitMinuteSecondsMilliseconds + ")");
        Condition condition = Condition.clickable(clickableElementBy, shouldDisplay);
        maxTimeInterval = Helper.convertTimeInSeconds(maxTimeInterval, maxTimeUnitMinuteSecondsMilliseconds);
        pollingTimeInterval = Helper.convertTimeInSeconds(pollingTimeInterval, pollingTimeUnitMinuteSecondsMilliseconds);
        return waitUntil(condition, maxTimeInterval, pollingTimeInterval, false);
    }

    public boolean selector_should_have_exactly_lessthan_greaterthan_elements(By countElementBy, String precision, int expectedNoOfElements) {
        logger.info("Element: " + countElementBy + " should have '" + precision + "' than " + expectedNoOfElements);
        Condition condition;
        if (precision.trim().equalsIgnoreCase("greater than")) {
            condition = Condition.countOfElementsMoreThan(countElementBy, expectedNoOfElements);
            //assert findAll(clickableElementBy).getElements().size() >= expectedNoOfElements;
        } else if (precision.trim().equalsIgnoreCase("less than")) {
            condition = Condition.countOfElementsLessThan(countElementBy, expectedNoOfElements);
            //assert findAll(clickableElementBy).getElements().size() <= expectedNoOfElements;
        } else if (precision.trim().equalsIgnoreCase("exactly")) {
            condition = Condition.exactCountOfElements(countElementBy, expectedNoOfElements);
            //assert findAll(clickableElementBy).getElements().size() == expectedNoOfElements;
        } else {
            throw new CukeBrowseException("Invalid precision parameter. Valid values are: ['exactly','less than','greater than']");
        }
        return waitUntil(condition, 30, false);
    }

    public void style_of_element_should_be(By selectorElementBy, String styleName, String expectedStyleValue, boolean styleShouldBe) {
        logger.info("Element: " + selectorElementBy + " style '" + styleName + "' should " + (styleShouldBe ? "" : "not") + " be'" + expectedStyleValue + "'.");
        wait_for_page_to_get_ready();
        String actualCssValue = find(selectorElementBy).getCssValue(styleName);
        assert styleShouldBe == actualCssValue.equals(expectedStyleValue);
    }

    public boolean attribute_of_element_should_be(By selectorElementBy, String attributeName, String expectedAttributeValue, boolean attributeShouldBe) {
        logger.info("Element: " + selectorElementBy + " attribute '" + attributeName + "' should " + (attributeShouldBe ? "" : "not") + " be'" + expectedAttributeValue + "'.");
        wait_for_page_to_get_ready();
        Condition condition = Condition.exactAttribute(selectorElementBy, attributeName, expectedAttributeValue, attributeShouldBe);
        return waitUntil(condition, 30, false);
    }

    public void I_select_and_upload_file_under_element(By uploadSelectorElementBy, String targetFilePath) {
        logger.info("I select file: '" + targetFilePath + "' and upload it on element: " + uploadSelectorElementBy);
        wait_for_page_to_get_ready();
        find(uploadSelectorElementBy).uploadFile(Helper.resolveFileSafely(targetFilePath));
    }

    public void I_copy_in_browser_clipboard_and_paste_it_in_element(String clipboardText, By sendTextElementBy) {
        wait_for_page_to_get_ready();
        I_wait_for_milliseconds(500);
        executeJavaScript("navigator.clipboard.writeText(" + clipboardText + ");");
        WebElement sendTextElement = find(sendTextElementBy).getElement();
        sendTextElement.sendKeys(Keys.CONTROL, "a");
        sendTextElement.sendKeys(Keys.CONTROL, "v");
        executeJavaScript("navigator.clipboard.writeText(" + "" + ");");
    }

    public String get_downloaded_file_path(String filePattern, @NotNull String fileExt){
        logger.info("Get downloaded file path having file pattern as: " + filePattern + " with extension '" + fileExt + "'.");
        return getDownloadedFilePath(filePattern, fileExt);
    }

    public File get_downloaded_file_from_to_target_path(String filePattern, @NotNull String fileExt, @NotNull String targetFilePath) throws IOException {
        logger.info("Get downloaded file at targeted path: "+ targetFilePath +" having file pattern as: " + filePattern + " with extension '" + fileExt + "'.");
        return getDownloadedFileFrom(filePattern, fileExt, targetFilePath);
    }
}
