package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

import static core.commands.Find.Find;
import static core.commands.FindAll.FindAll;
import static core.commands.GetInnerHtml.GetInnerHtml;

public class GetSelectedDropdownOption {
    private static final Logger logger = LoggerFactory.getLogger(GetSelectedDropdownOption.class);
    public static GetSelectedDropdownOption GetSelectedDropdownOption =
            ThreadLocal.withInitial(GetSelectedDropdownOption::new).get();

    private GetSelectedDropdownOption() {
        if (GetSelectedDropdownOption != null) {
            logger.error("Use GetSelectedDropdownOption variable to get the single instance of this class.");
            throw new RuntimeException("Use GetSelectedDropdownOption variable to get the single instance of this class.");
        }
    }

    public WebElement getSelectedDropdownOption(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        logger.info(String.format("Get Selected Dropdown Option: Get selected option of '%s' dropdown having option: '%s'", dropdownBy.toString(), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        return getSelectedDropdownOption(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public WebElement getSelectedDropdownOption(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        assert dropdownChildOptionBy != null;
        logger.info(String.format("Get Selected Dropdown Option: Get selected option of '%s' dropdown having option: '%s'", GetInnerHtml.getInnerHtml(dropdownElement), (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : ""));
        if("select".equalsIgnoreCase(dropdownElement.getTagName())){
            return new Select(dropdownElement).getFirstSelectedOption();
        } else {
            dropdownElement.click();
            if(dropdownChildOptionBy.length == 0){
                throw new Exception("Get Selected Dropdown Option: Element is not having 'Select' tag. 'dropdownChildOptionBy' argument value is required.");
            } else {
                return Find.find(dropdownElement, dropdownChildOptionBy[0]);
            }
        }
    }
}
