package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;
import static core.web.commands.GetSelectedDropdownOptions.GetSelectedDropdownOptions;

public class GetSelectedDropdownOptionsText {
    private static final Logger logger = LoggerFactory.getLogger(GetSelectedDropdownOptionsText.class);
    public static GetSelectedDropdownOptionsText GetSelectedDropdownOptionsText =
            ThreadLocal.withInitial(GetSelectedDropdownOptionsText::new).get();

    private GetSelectedDropdownOptionsText() {
        if (GetSelectedDropdownOptionsText != null) {
            logger.error("Use GetSelectedDropdownOptionsText variable to get the single instance of this class.");
            throw new RuntimeException("Use GetSelectedDropdownOptionsText variable to get the single instance of this class.");
        }
    }

    public List<String> getSelectedDropdownOptionsText(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        logger.info(String.format("Get Selected Dropdown Options Text: Get selected options text of '%s' dropdown having option: '%s'", dropdownBy.toString(), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        return getSelectedDropdownOptionsText(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public List<String> getSelectedDropdownOptionsText(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        logger.info(String.format("Get Selected Dropdown Options Text: Get selected options text of '%s' dropdown having option: '%s'", GetInnerHtml.getInnerHtml(dropdownElement), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        List<WebElement> selectedOptions = GetSelectedDropdownOptions.getSelectedDropdownOptions(dropdownElement, dropdownChildOptionBy);
        return selectedOptions.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
