package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;

public class GetSelectedDropdownOption {
    private static final Logger logger = LoggerFactory.getLogger(GetSelectedDropdownOption.class);
    public static GetSelectedDropdownOption GetSelectedDropdownOption =
            ThreadLocal.withInitial(GetSelectedDropdownOption::new).get();

    private GetSelectedDropdownOption() {
        if (GetSelectedDropdownOption != null) {
            logger.error("Use GetSelectedDropdownOption variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetSelectedDropdownOption variable to get the single instance of this class.");
        }
    }

    public WebElement get(By dropdownBy, By... dropdownChildOptionBy) {
        logger.info(String.format("Get Selected Dropdown Option: Get selected option of '%s' dropdown having option: '%s'", dropdownBy.toString(), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        return get(Find.findElement(dropdownBy), dropdownChildOptionBy);
    }

    public WebElement get(WebElement dropdownElement, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        logger.info(String.format("Get Selected Dropdown Option: Get selected option of '%s' dropdown having option: '%s'", GetInnerHtml.get(dropdownElement), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        if ("select".equalsIgnoreCase(dropdownElement.getTagName())) {
            return new Select(dropdownElement).getFirstSelectedOption();
        } else {
            dropdownElement.click();
            if (dropdownChildOptionBy.length == 0) {
                throw new CukeBrowseException("Get Selected Dropdown Option: Element is not having 'Select' tag. 'dropdownChildOptionBy' argument value is required.");
            } else {
                return Find.findElement(dropdownElement, dropdownChildOptionBy[0]);
            }
        }
    }
}
