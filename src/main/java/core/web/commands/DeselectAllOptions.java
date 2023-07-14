package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;

public class DeselectAllOptions {
    private static final Logger logger = LoggerFactory.getLogger(DeselectAllOptions.class);
    public static DeselectAllOptions DeselectAllOptions =
            ThreadLocal.withInitial(DeselectAllOptions::new).get();
    private final Find find;

    private DeselectAllOptions() {
        find = Find;
        if (DeselectAllOptions != null) {
            logger.error("Use DeselectAllOptions variable to get the single instance of this class.");
            throw new CukeBrowseException("Use DeselectAllOptions variable to get the single instance of this class.");
        }
    }

    public void deselectAll(By selectDropdownBy) {
        deselectAll(find.findElement(selectDropdownBy));
        logger.info(String.format("Deselect All Options: Deselect All Options in dropdown: '%s' ", selectDropdownBy.toString()));
    }

    public void deselectAll(WebElement selectDropdownElement) {
        if ("select".equalsIgnoreCase(selectDropdownElement.getTagName())) {
            Select select = new Select(selectDropdownElement);
            select.deselectAll();
            logger.info(String.format("Deselect All Options: Deselect All Options in dropdown: '%s' ", GetInnerHtml.get(selectDropdownElement)));
        } else {
            logger.error(String.format("Deselect All Options: '%s' Element don't have 'select' tag. This can only work with 'select' tag.", GetInnerHtml.get(selectDropdownElement)));
            throw new CukeBrowseException(String.format("Deselect All Options: '%s' Element don't have 'select' tag. This can only work with 'select' tag.", GetInnerHtml.get(selectDropdownElement)));
        }
    }

}