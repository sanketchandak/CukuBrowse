package glue;

import core.web.commands.ClickType;
import core.web.commands.HoverType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSteps {

    WebFunctions getWeb() {
        return WebFunctions.WebFunctions;
    }

    @When("^I wait for page to get ready$")
    public void when_I_wait_for_page_to_get_ready() {
        getWeb().wait_for_page_to_get_ready();
    }

    @When("^I clear browser cookies$")
    public void when_I_clear_browser_cookies() {
        getWeb().I_clear_browser_cookies();
    }

    @When("^I navigate to (.*)$")
    public void when_I_navigate_to(String url) {
        getWeb().I_navigate_to_url(url);
    }

    @When("^I navigate back$")
    public void when_I_navigate_back() {
        getWeb().I_navigate_back();
    }

    @When("^I navigate forward$")
    public void when_I_navigate_forward() {
        getWeb().I_navigate_forward();
    }

    @When("^I refresh current web page$")
    public void when_I_refresh_current_web_page() {
        getWeb().I_refresh_current_web_page();
    }

    @Then("^I should be redirected to (.*)$")
    public void when_I_should_be_redirected_to(String expectedURL) {
        getWeb().I_should_be_redirected_to(expectedURL);
    }

    @Then("^current URL should( not)? (be|contain) (.*)$")
    public void then_current_URL_should(String not, String operation, String expectedURL) {
        getWeb().current_URL_should_be_contain(operation, expectedURL, not == null);
    }

    @Then("^page title should( not)? (be|contain) (.*)$")
    public void then_page_title_should(String not, String operation, String expectedTitle) {
        getWeb().page_title_should_be_contain(operation, expectedTitle, not == null);
    }

    @Then("^page should( not)? (contain|match) text (.*)$")
    public void then_page_text_should(String not, String operation, String expectedText) {
        getWeb().element_should_contain_match_have_text(By.tagName("body"), operation, expectedText, not == null);
    }

    @Then("^element (.+) should( not)? (contain|match) text (.*)$")
    public void then_element_text_should(String elementName, String not, String operation, String expectedText) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().element_should_contain_match_have_text(elementBy, operation, expectedText, not == null);
    }

    @Then("^elements (.+) should( not)? contain text (.*)$")
    public void then_elements_text_should(String elementName, String not, String expectedText) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().elements_should_contain_text(elementBy, expectedText, not == null);
    }

    @When("^I press enter on element (.+)$")
    public void when_I_press_enter_on_element(String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().I_press_enter_on_element(elementBy);
    }

    @When("^I press tab on element (.+)$")
    public void when_I_press_tab_on_element(String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().I_press_tab_on_element(elementBy);
    }

    @When("^I (click|JS click|JS double click|JS right click|right click|double click) on element (.+)$")
    public void when_I_click_on_element(String clickOption, String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        ClickType ct = getWeb().getClickType(clickOption);
        getWeb().I_click_on_element(elementBy, ct);
    }

    @When("^I optionally (click|JS click|JS double click|JS right click|right click|double click) on element (.+)$")
    public void when_I_optionally_click_on_element(String clickOption, String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        ClickType ct = getWeb().getClickType(clickOption);
        getWeb().I_optionally_click_on_element(elementBy, ct);
    }

    @When("^I (click|JS click|JS double click|JS right click|right click|double click) on link with text( like)? (.*)$")
    public void when_I_click_on_link_with_text(String clickOption, String like, String linkName) {
        ClickType ct = getWeb().getClickType(clickOption);
        getWeb().I_click_on_link_with_text(linkName, ct, like == null);
    }

    @Then("^I enter (.*) text in element (.+)$")
    public void then_I_enter_text_in_element(String textToEnter, String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().I_enter_text_in_element(elementBy, textToEnter);
    }

    @Then("^I select option (with|having) text (.*) from 'select' dropdown element (.+)$")
    public void then_I_select_from_dropdown_element(String withOrHaving, String dropdownValue, String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().I_select_with_or_having_text_from_select_dropdown(withOrHaving, elementBy, dropdownValue);
    }

    @Then("^I deselect option with text (.*) from 'select' dropdown element (.+)$")
    public void then_I_deselect_from_dropdown_element(String dropdownValue, String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().I_deselect_option_with_text_from_select_dropdown(elementBy, dropdownValue);
    }

    @Then("^I select option (with|having) text (.*) from dropdown element (.+) and options element (.+)$")
    public void then_I_select_from_dropdown_element_and_Option(String withOrHaving, String dropdownValue, String dropdownElementName, String optionsElementName) {
        By dropdownElementBy = getWeb().normalize_element(dropdownElementName);
        By optionsElementBy = getWeb().normalize_element(optionsElementName);
        getWeb().I_select_with_or_having_text_from_dropdown_and_option(withOrHaving, dropdownElementBy, optionsElementBy, dropdownValue);
    }

    @Then("^I deselect all options of dropdown element (.+)$")
    public void then_I_deselect_all_options_of_dropdown_element(String elementName) {
        By elementBy = getWeb().normalize_element(elementName);
        getWeb().I_deselect_all_options_of_dropdown(elementBy);
    }

    @Then("^I select all following options with text from dropdown element (.+)$")
    public void then_I_select_following_options_with_text_from_dropdown(String dropdownElementName, List<String> dropdownTexts) {
        By dropdownElementBy = getWeb().normalize_element(dropdownElementName);
        getWeb().I_select_following_options_with_text_from_dropdown(dropdownElementBy, dropdownTexts);
    }

    @Then("^I deselect all following options with text from dropdown element (.+)$")
    public void then_I_deselect_following_options_with_text_from_dropdown(String dropdownElementName, List<String> dropdownTexts) {
        By dropdownElementBy = getWeb().normalize_element(dropdownElementName);
        getWeb().I_deselect_following_options_with_text_from_select_dropdown(dropdownElementBy, dropdownTexts);
    }

    @And("^I (accept|dismiss) alert$")
    public void and_I_accept_alert(String acceptOrDismiss) {
        if (acceptOrDismiss.equalsIgnoreCase("accept")) {
            getWeb().I_accept_alert();
        } else {
            getWeb().I_dismiss_alert();
        }
    }

    @And("^I (accept|dismiss) alert with message (.*)$")
    public void and_I_accept_alert_with_message(String acceptOrDismiss, String expectedAlertMessage) {
        if (acceptOrDismiss.equalsIgnoreCase("accept")) {
            getWeb().I_accept_alert_with_message(expectedAlertMessage);
        } else {
            getWeb().I_dismiss_alert_with_message(expectedAlertMessage);
        }
    }

    @And("^alert window should( not)? be displayed$")
    public void and_alert_window_should_be_displayed(String not) {
        getWeb().alert_window_should_be_displayed(not == null);
    }

    @And("^alert window should be displayed with text (.*)$")
    public void and_alert_window_should_be_displayed_with_text(String alertMessage) {
        getWeb().alert_window_should_be_displayed_with_text(alertMessage);
    }

    @And("^I drag element (.+) and drop to element (.+)$")
    public void and_I_drag_drop_element_to_other_element(String dragElementName, String dropElementName) {
        By dragElementBy = getWeb().normalize_element(dragElementName);
        By dropElementBy = getWeb().normalize_element(dropElementName);
        getWeb().I_drag_drop_element_to_other_element(dragElementBy, dropElementBy);
    }

    @And("^I drag element (.+) and drop to xOffset (\\d+) and yOffset (\\d+)$")
    public void and_I_drag_drop_element_to_offset(String dragElementName, int xOffset, int yOffset) {
        By dragElementBy = getWeb().normalize_element(dragElementName);
        getWeb().I_drag_drop_element_to_offset(dragElementBy, xOffset, yOffset);
    }

    @And("^I perform (javascript hover|javascript out|hover) on element (.+)$")
    public void and_I_mouse_over_on_element(String wayToHover, String mouseoverElementName) {
        By mouseoverElementBy = getWeb().normalize_element(mouseoverElementName);
        HoverType hoverType = null;
        if (wayToHover.trim().equalsIgnoreCase("javascript hover")) {
            hoverType = HoverType.JSHover;
        } else if (wayToHover.trim().equalsIgnoreCase("javascript out")) {
            hoverType = HoverType.JSOut;
        } else if (wayToHover.trim().equalsIgnoreCase("hover")) {
            hoverType = HoverType.ActionHover;
        }
        getWeb().I_mouse_over_on_element(mouseoverElementBy, hoverType);
    }

    @And("^I scroll to get element (.+) visible on screen$")
    public void and_I_scroll_to_element(String scrollElementName) {
        By scrollElementBy = getWeb().normalize_element(scrollElementName);
        getWeb().I_scroll_element_in_view(scrollElementBy);
    }

    @And("^I set browser zoom percentage (\\d+)$")
    public void and_I_set_browser_zoom_percentage(int zoomPercentage) {
        getWeb().I_set_browser_zoom_percentage(zoomPercentage);
    }

    @And("^I reset browser zoom level$")
    public void and_I_reset_browser_zoom_level() {
        getWeb().I_reset_browser_zoom_level();
    }

    @And("^I switch to frame by (name|id|element) (.+)$")
    public void and_I_switch_to_frame_by_name_id_element(String nameIdElementType, String frameNameOrIdOrElement) {
        if (nameIdElementType.trim().equalsIgnoreCase("element")) {
            By frameNameOrIdOrElementBy = getWeb().normalize_element(frameNameOrIdOrElement);
            getWeb().I_switch_to_frame_by_element(frameNameOrIdOrElementBy);
        } else {
            getWeb().I_switch_to_frame_by_name_or_id(frameNameOrIdOrElement);
        }
    }

    @And("^I switch to frame by index (\\d+)$")
    public void and_I_switch_to_frame_by_index(int frameIndex) {
        getWeb().I_switch_to_frame_by_index(frameIndex);
    }

    @And("^I switch to parent frame$")
    public void and_I_switch_to_parent_frame() {
        getWeb().I_switch_to_parent_frame();
    }

    @And("^I switch to window by title (.+)$")
    public void and_I_switch_to_window_by_title(String windowTitle) {
        getWeb().I_switch_to_window_by_title(windowTitle);
    }

    @And("^I close all child browser windows$")
    public void and_I_close_all_child_browser_windows() {
        getWeb().I_close_all_child_browser_windows();
    }

    @And("^I close current browser windows$")
    public void and_I_close_current_browser_window() {
        getWeb().I_close_current_browser_window();
    }

    @Then("^I wait for (\\d+) (minutes|seconds|milliseconds)$")
    public void then_I_wait_for_minutes_seconds_milliseconds(long waitInterval, String timeUnits) {
        if (timeUnits.trim().equalsIgnoreCase("minutes")) {
            getWeb().I_wait_for_milliseconds(TimeUnit.MINUTES.toMillis(waitInterval));
        } else if (timeUnits.trim().equalsIgnoreCase("seconds")) {
            getWeb().I_wait_for_milliseconds(TimeUnit.SECONDS.toMillis(waitInterval));
        } else {
            getWeb().I_wait_for_milliseconds(waitInterval);
        }
    }

    @Then("^I capture and attach screenshot of page$")
    public void then_I_capture_and_attach_screenshot_of_page() {
        getWeb().I_capture_and_attach_screenshot_of_page();
    }

    @Then("^I capture and attach screenshot of element (.+)$")
    public void then_I_capture_and_attach_screenshot_of_element(String screenshotElementName) {
        By screenshotElementBy = getWeb().normalize_element(screenshotElementName);
        getWeb().I_capture_and_attach_screenshot_of_element(screenshotElementBy);
    }

    @Then("^element (.+) should( not)? be displayed$")
    public void then_element_should_be_displayed(String displayElementName, String not) {
        By displayElementBy = getWeb().normalize_element(displayElementName);
        getWeb().element_should_be_displayed(displayElementBy, not == null);
    }

    @Then("^I wait (\\d+) (minutes|seconds|milliseconds) with polling interval of (\\d+) (minutes|seconds|milliseconds) for element (.+) text( not)? to be (.+)$")
    public void then_element_text_should_be_with_polling(long maxTimeInterval, String max_TimeUnit, long pollingTimeInterval, String polling_TimeUnit, String textElementName, String not, String expectedText) {
        By textElementBy = getWeb().normalize_element(textElementName);
        TimeUnit maxTimeUnit, pollingTimeUnit;
        switch (max_TimeUnit.trim().toUpperCase()) {
            case "SECONDS":
                maxTimeUnit = TimeUnit.SECONDS;
                break;
            case "MILLISECONDS":
                maxTimeUnit = TimeUnit.MILLISECONDS;
                break;
            case "MINUTES":
                maxTimeUnit = TimeUnit.MINUTES;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + max_TimeUnit.trim().toUpperCase());
        }
        switch (polling_TimeUnit.trim().toUpperCase()) {
            case "SECONDS":
                pollingTimeUnit = TimeUnit.SECONDS;
                break;
            case "MILLISECONDS":
                pollingTimeUnit = TimeUnit.MILLISECONDS;
                break;
            case "MINUTES":
                pollingTimeUnit = TimeUnit.MINUTES;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + polling_TimeUnit.trim().toUpperCase());
        }
        getWeb().element_text_should_be_with_polling(textElementBy, expectedText, not == null, maxTimeInterval, maxTimeUnit, pollingTimeInterval, pollingTimeUnit);
    }

    @Then("^I wait (\\d+) (minutes|seconds|milliseconds) with polling interval of (\\d+) (minutes|seconds|milliseconds) for element (.+) to( not)? display$")
    public void then_element_should_be_displayed_with_polling(long maxTimeInterval, String max_TimeUnit, long pollingTimeInterval, String polling_TimeUnit, String displayElementName, String not) {
        By displayElementBy = getWeb().normalize_element(displayElementName);
        TimeUnit maxTimeUnit, pollingTimeUnit;
        switch (max_TimeUnit.trim().toUpperCase()) {
            case "SECONDS":
                maxTimeUnit = TimeUnit.SECONDS;
                break;
            case "MILLISECONDS":
                maxTimeUnit = TimeUnit.MILLISECONDS;
                break;
            case "MINUTES":
                maxTimeUnit = TimeUnit.MINUTES;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + max_TimeUnit.trim().toUpperCase());
        }
        switch (polling_TimeUnit.trim().toUpperCase()) {
            case "SECONDS":
                pollingTimeUnit = TimeUnit.SECONDS;
                break;
            case "MILLISECONDS":
                pollingTimeUnit = TimeUnit.MILLISECONDS;
                break;
            case "MINUTES":
                pollingTimeUnit = TimeUnit.MINUTES;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + polling_TimeUnit.trim().toUpperCase());
        }
        getWeb().element_should_be_displayed_with_polling(displayElementBy, not == null, maxTimeInterval, maxTimeUnit, pollingTimeInterval, pollingTimeUnit);
    }

    @Then("^element (.+) should( not)? be clickable$")
    public void then_element_should_be_clickable(String clickableElementName, String not) {
        By clickableElementBy = getWeb().normalize_element(clickableElementName);
        getWeb().element_should_be_clickable(clickableElementBy, not == null);
    }

    @Then("^I wait (\\d+) (minutes|seconds|milliseconds) with polling interval of (\\d+) (minutes|seconds|milliseconds) for element (.+) to( not)? clickable$")
    public void then_element_should_be_clickable_with_polling(long maxTimeInterval, String max_TimeUnit, long pollingTimeInterval, String polling_TimeUnit, String clickableElementName, String not) {
        By clickableElementBy = getWeb().normalize_element(clickableElementName);
        TimeUnit maxTimeUnit, pollingTimeUnit;
        switch (max_TimeUnit.trim().toUpperCase()) {
            case "SECONDS":
                maxTimeUnit = TimeUnit.SECONDS;
                break;
            case "MILLISECONDS":
                maxTimeUnit = TimeUnit.MILLISECONDS;
                break;
            case "MINUTES":
                maxTimeUnit = TimeUnit.MINUTES;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + max_TimeUnit.trim().toUpperCase());
        }
        switch (polling_TimeUnit.trim().toUpperCase()) {
            case "SECONDS":
                pollingTimeUnit = TimeUnit.SECONDS;
                break;
            case "MILLISECONDS":
                pollingTimeUnit = TimeUnit.MILLISECONDS;
                break;
            case "MINUTES":
                pollingTimeUnit = TimeUnit.MINUTES;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + polling_TimeUnit.trim().toUpperCase());
        }
        getWeb().element_should_be_clickable_with_polling(clickableElementBy, not == null, maxTimeInterval, maxTimeUnit, pollingTimeInterval, pollingTimeUnit);
    }

    @And("^selector (.*) should return (exactly|less than|greater than) (\\d+) elements$")
    public void and_selector_should_have_exactly_lessthan_greaterthan_elements(String selectorName, String precision, int expectedNoOfElements) {
        By clickableElementBy = getWeb().normalize_element(selectorName);
        getWeb().selector_should_have_exactly_lessthan_greaterthan_elements(clickableElementBy, precision, expectedNoOfElements);
    }

    @And("^style (.+) of element (.+) should( not)? be (.+)$")
    public void and_style_of_element_should_be(String styleName, String selectorName, String not, String expectedStyleValue) {
        By selectorElementBy = getWeb().normalize_element(selectorName);
        getWeb().style_of_element_should_be(selectorElementBy, styleName, expectedStyleValue, not == null);
    }

    @And("^attribute (.+) of element (.+) should( not)? be (.+)$")
    public void and_attribute_of_element_should_be(String attributeName, String selectorName, String not, String expectedAttributeValue) {
        By selectorElementBy = getWeb().normalize_element(selectorName);
        getWeb().attribute_of_element_should_be(selectorElementBy, attributeName, expectedAttributeValue, not == null);
    }

    @Then("^I select and upload (.+) file under element (.+)$")
    public void and_I_select_and_upload_file_under_element(String targetFilePath, String uploadSelectorName) {
        By uploadSelectorElementBy = getWeb().normalize_element(uploadSelectorName);
        getWeb().I_select_and_upload_file_under_element(uploadSelectorElementBy, targetFilePath);
    }

    @Then("^I copy text (.+) in browser clipboard and paste it in element (.+)$")
    public void and_I_copy_in_browser_clipboard_and_paste_it_in_element(String clipboardText, String sendTextElement) {
        By sendTextElementBy = getWeb().normalize_element(sendTextElement);
        getWeb().I_copy_in_browser_clipboard_and_paste_it_in_element(clipboardText, sendTextElementBy);
    }
}
