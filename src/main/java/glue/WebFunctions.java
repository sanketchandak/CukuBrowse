package glue;

import core.web.CommanderElement;
import core.web.Condition;
import core.web.browser.runner.WebDriverRunner;
import core.web.commands.ClickType;
import core.web.commands.HoverType;
import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Helper;
import utils.service.ServiceHooks;

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
        return ElementImporter.getElement(elementName);
    }

    public void wait_for_page_to_get_ready(){
        waitForPageToReady();
    }

    public void I_accept_alert() {
        confirmAlert();
    }

    public void I_dismiss_alert() {
        dismissAlert();
    }

    public void I_accept_alert_with_message(String expectedAlertMessage) {
        try {
            confirmAlertWithMessage(expectedAlertMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void I_dismiss_alert_with_message(String expectedAlertMessage) {
        try {
            dismissAlertWithMessage(expectedAlertMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean alert_window_should_be_displayed(boolean alertShouldBe) {
        Condition condition = Condition.AlertPresence(alertShouldBe);
        return waitUntil(condition, 30, false);
    }

    public void alert_window_should_be_displayed_with_text(String alertMessage) {
        alert_window_should_be_displayed(true);
        assert getAlertMessage().trim().equals(alertMessage.trim());
    }

    public void I_clear_browser_cookies() {
        clearBrowserCookies();
    }

    public void I_navigate_to_url(String url) {
        navigateTo(url);
    }

    public void I_navigate_back() {
        navigateBack();
    }

    public void I_navigate_forward() {
        navigateForward();
    }

    public void I_refresh_current_web_page() {
        browserRefresh();
    }

    public void I_should_be_redirected_to(String expectedURL) {
        assert getBrowserURL().equals(expectedURL);
    }

    public void I_enter_text_in_element(By elementBy, String textToEnter) {
        CommanderElement commanderElement = find(elementBy);
        commanderElement.clearBox();
        commanderElement.sendKeys(textToEnter);
    }

    public void I_select_with_or_having_text_from_select_dropdown(String withOrHaving, By elementBy, String dropdownValue) {
        try {
            if (withOrHaving.trim().equalsIgnoreCase("with")) {
                String[] dropdownValues = {dropdownValue};
                find(elementBy).selectDropdownOptionsByTexts(dropdownValues);
            } else {
                find(elementBy).selectOptionsContainingTexts(dropdownValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean current_URL_should_be_contain(String operationBeOrContain, String expectedURL, boolean expectedToBeOrContain) {
        Condition condition;
        if (operationBeOrContain.trim().equalsIgnoreCase("be")) {
            condition = Condition.urlIs(expectedURL, expectedToBeOrContain);
        } else if (operationBeOrContain.trim().equalsIgnoreCase("contain")) {
            condition = Condition.urlContains(expectedURL, expectedToBeOrContain);
        } else {
            throw new InvalidArgumentException("Invalid operation parameter. Valid values are: ['be','contain']. Actual value passed:" + operationBeOrContain);
        }
        return waitUntil(condition, 30, false);
    }

    public boolean page_title_should_be_contain(String operationBeOrContain, String expectedTitle, boolean expectedToBeOrContain) {
        /*String command = KEYWORDS_TO_FUNCTION.valueOf(operation).toString();
        try {
            Method[] allMethod = String.class.getDeclaredMethods();
            Optional<Method> firstMethod = Arrays.stream(allMethod).filter(method -> method.getName().equals(command)).findFirst();
            if (expectedContainHaveOrMatch) {
                assert firstMethod.isPresent() && (boolean) firstMethod.get().invoke(title(), expectedTitle);
            } else {
                assert firstMethod.isPresent() && !(boolean) firstMethod.get().invoke(title(), expectedTitle);
            }
            return true;
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }*/
        Condition condition;
        if (operationBeOrContain.trim().equalsIgnoreCase("be")) {
            condition = Condition.titleIs(expectedTitle, expectedToBeOrContain);
        } else if (operationBeOrContain.trim().equalsIgnoreCase("contain")) {
            condition = Condition.titleContains(expectedTitle, expectedToBeOrContain);
        } else {
            throw new InvalidArgumentException("Invalid operation parameter. Valid values are: ['be','contain']. Actual value passed:" + operationBeOrContain);
        }
        return waitUntil(condition, 30, false);
    }

    public boolean element_should_contain_match_have_text(By elementBy, String operationContainOrMatch, String expectedText, boolean expectedContainHaveOrMatch) {
        Condition condition;
        if (operationContainOrMatch.trim().equalsIgnoreCase("match")) {
            condition = Condition.exactText(elementBy, expectedText, expectedContainHaveOrMatch);
        } else if (operationContainOrMatch.trim().equalsIgnoreCase("contain")) {
            condition = Condition.containsCaseSensitiveText(elementBy, expectedText, expectedContainHaveOrMatch);
        } else {
            throw new InvalidArgumentException("Invalid operation parameter. Valid values are: ['match','contain']. Actual value passed:" + operationContainOrMatch);
        }
        return waitUntil(condition, 30, false);
    }

    public boolean elements_should_contain_text(By elementBy, String expectedText, boolean expectedContain) {
        Set<Boolean> flags = findAll(elementBy).getElements().stream().
                map(ce -> ce.getText().contains(expectedText)).
                collect(Collectors.toSet());
        if(expectedContain){
            return !flags.contains(false);
        } else {
            return !flags.contains(true);
        }
    }

    public void I_click_on_element(By elementBy, ClickType ct) {
        find(elementBy).clickOn(ct);
    }

    public void I_optionally_click_on_element(By elementBy, ClickType ct) {
        try {
            I_click_on_element(elementBy, ct);
        } catch (Exception e) {
            logger.info("Failed with I optionally " + ct + " on element: " + elementBy + ". Exception Details:" + Arrays.toString(e.getStackTrace()));
        }
    }

    public void I_click_on_link_with_text(String linkName, ClickType ct, boolean like) {
        By elementBy;
        if (like) {
            elementBy = By.linkText(Helper.removeQuotes(linkName));
        } else {
            elementBy = By.partialLinkText(Helper.removeQuotes(linkName));
        }
        I_click_on_element(elementBy, ct);
    }

    public void I_deselect_option_with_text_from_select_dropdown(By dropdownElementBy, String dropdownValue) {
        String[] dropdownValues = {dropdownValue};
        find(dropdownElementBy).deselectDropdownOptionsByTexts(dropdownValues);
    }

    public void I_deselect_following_options_with_text_from_select_dropdown(By dropdownElementBy, List<String> dropdownTexts) {
        find(dropdownElementBy).deselectDropdownOptionsByTexts((String[]) dropdownTexts.toArray());
    }

    public ClickType getClickType(String clickOption) {
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
        try {
            if (withOrHaving.trim().equalsIgnoreCase("with")) {
                String[] dropdownValues = {dropdownValue};
                find(dropdownElementBy).selectDropdownOptionsByTexts(dropdownValues, optionsElementBy);
            } else {
                find(dropdownElementBy).selectOptionsContainingTexts(dropdownValue, optionsElementBy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void I_select_following_options_with_text_from_dropdown(By dropdownElementBy, List<String> dropdownTexts) {
        try {
            find(dropdownElementBy).selectDropdownOptionsByTexts((String[]) dropdownTexts.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void I_deselect_all_options_of_dropdown(By dropdownElementBy) {
        find(dropdownElementBy).deselectAllDropdownOptions();
    }

    public void I_drag_drop_element_to_other_element(By dragElementBy, By dropElementBy) {
        CommanderElement dropCommanderElement = find(dropElementBy);
        find(dragElementBy).dragAndDropTo(dropCommanderElement);
    }

    public void I_drag_drop_element_to_offset(By dragElementBy, int xOffset, int yOffset) {
        find(dragElementBy).dragAndDropTo(xOffset, yOffset);
    }

    public void I_mouse_over_on_element(By mouseoverElementBy, HoverType hoverType) {
        find(mouseoverElementBy).hover(hoverType);
    }

    public void I_scroll_element_in_view(By scrollElementBy) {
        scrollIntoView(scrollElementBy);
    }

    public void I_set_browser_zoom_percentage(int zoomPercentage) {
        setBrowserZoomPercentage(zoomPercentage);
    }

    public void I_reset_browser_zoom_level() {
        resetBrowserZoomLevel();
    }

    public void I_switch_to_frame_by_name_or_id(String frameNameOrId) {
        switchToMainFrame();
        switchToFrameByNameOrId(frameNameOrId);
    }

    public void I_switch_to_frame_by_element(By frameElementBy) {
        switchToMainFrame();
        switchToFrameByFrameElement(find(frameElementBy));
    }

    public void I_switch_to_frame_by_index(int frameIndex) {
        switchToMainFrame();
        switchToFrameByIndex(frameIndex);
    }

    public void I_switch_to_parent_frame(){
        switchToMainFrame();
    }

    public void I_switch_to_window_by_title(String windowTitle) {
        switchToWindowByTitle(windowTitle);
    }

    public void I_close_all_child_browser_windows() {
        closeAllChildBrowserWindows();
    }

    public void I_close_current_browser_windows() {
        closeCurrentBrowserWindow();
    }

    public void I_wait_for_milliseconds(long toMillis) {
        sleep(toMillis);
    }

    public void I_capture_and_attach_screenshot_of_page() {
        Scenario scenario = ServiceHooks.getCurrentScenario();
        WebDriverRunner webDriverRunner = getWebDriverRunner();
        if (webDriverRunner.hasWebDriverStarted()) {
            scenario.attach(screenshotOfPageAsBytes(),
                    "image/png",
                    scenario.getId() + Helper.getTimestamp());
        }
    }

    public void I_capture_and_attach_screenshot_of_element(By screenshotElementBy) {
        Scenario scenario = ServiceHooks.getCurrentScenario();
        WebDriverRunner webDriverRunner = getWebDriverRunner();
        if (webDriverRunner.hasWebDriverStarted()) {
            scenario.attach(screenshotOfElementAsBytes(screenshotElementBy),
                    "image/png",
                    scenario.getId() + Helper.getTimestamp());
        }
    }

    public boolean element_should_be_displayed(By displayElementBy, boolean shouldDisplay) {
        Condition condition = shouldDisplay ? Condition.visible(displayElementBy) : Condition.invisible(displayElementBy);
        return waitUntil(condition, 30, false);
    }

    public boolean element_should_be_displayed_with_polling(By displayElementBy, boolean shouldDisplay, long maxTimeInterval, TimeUnit maxTimeUnitMinuteSecondsMilliseconds, long pollingTimeInterval, TimeUnit pollingTimeUnitMinuteSecondsMilliseconds) {
        Condition condition = shouldDisplay ? Condition.visible(displayElementBy) : Condition.invisible(displayElementBy);
        long maxConvertedTime, pollingConvertedIntervalTime;
        switch (maxTimeUnitMinuteSecondsMilliseconds) {
            case MILLISECONDS:
                maxConvertedTime = TimeUnit.MILLISECONDS.toSeconds(maxTimeInterval);
                break;
            case MINUTES:
                maxConvertedTime = TimeUnit.MINUTES.toSeconds(maxTimeInterval);
                break;
            case SECONDS:
                maxConvertedTime = maxTimeInterval;
                break;
            default:
                throw new InvalidArgumentException("Invalid time unit parameter. Valid values are: ['MILLISECONDS','MINUTES','SECONDS']");
        }

        switch (pollingTimeUnitMinuteSecondsMilliseconds) {
            case MILLISECONDS:
                pollingConvertedIntervalTime = TimeUnit.MILLISECONDS.toSeconds(pollingTimeInterval);
                break;
            case MINUTES:
                pollingConvertedIntervalTime = TimeUnit.MINUTES.toSeconds(pollingTimeInterval);
                break;
            case SECONDS:
                pollingConvertedIntervalTime = pollingTimeInterval;
                break;
            default:
                throw new InvalidArgumentException("Invalid time unit parameter. Valid values are: ['MILLISECONDS','MINUTES','SECONDS']");
        }

        return waitUntil(condition, maxConvertedTime, pollingConvertedIntervalTime, false);
    }

    public boolean element_should_be_clickable(By clickableElementBy, boolean shouldClickable) {
        Condition condition = shouldClickable ? Condition.visible(clickableElementBy) : Condition.invisible(clickableElementBy);
        return waitUntil(condition, 30, false);
    }

    public boolean element_should_be_clickable_with_polling(By clickableElementBy, boolean shouldDisplay, long maxTimeInterval, TimeUnit maxTimeUnitMinuteSecondsMilliseconds, long pollingTimeInterval, TimeUnit pollingTimeUnitMinuteSecondsMilliseconds) {
        Condition condition = Condition.clickable(clickableElementBy, shouldDisplay);
        long maxConvertedTime, pollingConvertedIntervalTime;
        switch (maxTimeUnitMinuteSecondsMilliseconds) {
            case MILLISECONDS:
                maxConvertedTime = TimeUnit.MILLISECONDS.toSeconds(maxTimeInterval);
                break;
            case MINUTES:
                maxConvertedTime = TimeUnit.MINUTES.toSeconds(maxTimeInterval);
                break;
            case SECONDS:
                maxConvertedTime = maxTimeInterval;
                break;
            default:
                throw new InvalidArgumentException("Invalid time unit parameter. Valid values are: ['MILLISECONDS','MINUTES','SECONDS']");
        }

        switch (pollingTimeUnitMinuteSecondsMilliseconds) {
            case MILLISECONDS:
                pollingConvertedIntervalTime = TimeUnit.MILLISECONDS.toSeconds(pollingTimeInterval);
                break;
            case MINUTES:
                pollingConvertedIntervalTime = TimeUnit.MINUTES.toSeconds(pollingTimeInterval);
                break;
            case SECONDS:
                pollingConvertedIntervalTime = pollingTimeInterval;
                break;
            default:
                throw new InvalidArgumentException("Invalid time unit parameter. Valid values are: ['MILLISECONDS','MINUTES','SECONDS']");
        }

        return waitUntil(condition, maxConvertedTime, pollingConvertedIntervalTime, false);
    }

    public boolean selector_should_have_exactly_lessthan_greaterthan_elements(By clickableElementBy, String precision, int expectedNoOfElements) {
        Condition condition;
        if (precision.trim().equalsIgnoreCase("greater than")) {
            condition = Condition.CountOfElementsMoreThan(clickableElementBy, expectedNoOfElements);
            //assert findAll(clickableElementBy).getElements().size() >= expectedNoOfElements;
        } else if (precision.trim().equalsIgnoreCase("less than")) {
            condition = Condition.CountOfElementsLessThan(clickableElementBy, expectedNoOfElements);
            //assert findAll(clickableElementBy).getElements().size() <= expectedNoOfElements;
        } else if (precision.trim().equalsIgnoreCase("exactly")) {
            condition = Condition.ExactCountOfElements(clickableElementBy, expectedNoOfElements);
            //assert findAll(clickableElementBy).getElements().size() == expectedNoOfElements;
        } else {
            throw new InvalidArgumentException("Invalid precision parameter. Valid values are: ['exactly','less than','greater than']");
        }
        return waitUntil(condition, 30, false);
    }

    public void style_of_element_should_be(By selectorElementBy, String styleName, String expectedStyleValue, boolean styleShouldBe) {
        wait_for_page_to_get_ready();
        String actualCssValue = find(selectorElementBy).getCssValue(styleName);
        assert styleShouldBe == actualCssValue.equals(expectedStyleValue);
    }

    public boolean attribute_of_element_should_be(By selectorElementBy, String attributeName, String expectedAttributeValue, boolean attributeShouldBe) {
        wait_for_page_to_get_ready();
        Condition condition = Condition.exactAttribute(selectorElementBy, attributeName, expectedAttributeValue, attributeShouldBe);
        return waitUntil(condition, 30, false);
    }
}
