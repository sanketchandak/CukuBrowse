package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;
import static core.web.commands.FindAll.FindAll;
import static core.web.commands.GetInnerHtml.GetInnerHtml;

public class GetSelectedDropdownOptions {
    private static final Logger logger = LoggerFactory.getLogger(GetSelectedDropdownOptions.class);
    public static GetSelectedDropdownOptions GetSelectedDropdownOptions =
            ThreadLocal.withInitial(GetSelectedDropdownOptions::new).get();

    private GetSelectedDropdownOptions() {
        if (GetSelectedDropdownOptions != null) {
            logger.error("Use GetSelectedDropdownOption variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetSelectedDropdownOption variable to get the single instance of this class.");
        }
    }

    public List<WebElement> getSelectedDropdownOptions(By dropdownBy, By... dropdownChildOptionBy) {
        logger.info(String.format("Get Selected Dropdown Options: Get selected options of '%s' dropdown having option: '%s'", dropdownBy.toString(), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        return getSelectedDropdownOptions(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public List<WebElement> getSelectedDropdownOptions(WebElement dropdownElement, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        logger.info(String.format("Get Selected Dropdown Options: Get selected options of '%s' dropdown having option: '%s'", GetInnerHtml.getInnerHtml(dropdownElement), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        if ("select".equalsIgnoreCase(dropdownElement.getTagName())) {
            return new Select(dropdownElement).getAllSelectedOptions();
        } else {
            dropdownElement.click();
            if (dropdownChildOptionBy.length == 0) {
                throw new CukeBrowseException("Get Selected Dropdown Options: Element is not having 'Select' tag. 'dropdownChildOptionBy' argument value is required.");
            } else {
                return FindAll.findAll(dropdownElement, dropdownChildOptionBy[0]);
            }
        }
    }
}
