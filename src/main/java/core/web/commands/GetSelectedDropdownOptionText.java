package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;
import static core.web.commands.GetSelectedDropdownOption.GetSelectedDropdownOption;

public class GetSelectedDropdownOptionText {
    private static final Logger logger = LoggerFactory.getLogger(GetSelectedDropdownOptionText.class);
    public static GetSelectedDropdownOptionText GetSelectedDropdownOptionText =
            ThreadLocal.withInitial(GetSelectedDropdownOptionText::new).get();

    private GetSelectedDropdownOptionText() {
        if (GetSelectedDropdownOptionText != null) {
            logger.error("Use GetSelectedDropdownOptionText variable to get the single instance of this class.");
            throw new RuntimeException("Use GetSelectedDropdownOptionText variable to get the single instance of this class.");
        }
    }

    public String getSelectedDropdownOptionText(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        String selectedDropdownOptionText = getSelectedDropdownOptionText(Find.find(dropdownBy), dropdownChildOptionBy);
        logger.info(String.format("Get Selected Dropdown Option Text: '%s' dropdown having child options: '%s' having text as '%s'",
                dropdownBy.toString(),
                (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : "",
                selectedDropdownOptionText
        ));
        return selectedDropdownOptionText;
    }

    public String getSelectedDropdownOptionText(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        String selectedDropdownOptionText = GetSelectedDropdownOption.getSelectedDropdownOption(dropdownElement, dropdownChildOptionBy).getText();
        logger.info(String.format("Get Selected Dropdown Option Text: '%s' dropdown having child options: '%s' having text as '%s'",
                GetInnerHtml.getInnerHtml(dropdownElement),
                (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : "",
                selectedDropdownOptionText
        ));
        return selectedDropdownOptionText;
    }
}
