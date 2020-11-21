package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static core.commands.Find.Find;

public class DeselectAllOptions {
    public static DeselectAllOptions DeselectAllOptions =
            ThreadLocal.withInitial(DeselectAllOptions::new).get();
    private final Find find;

    private DeselectAllOptions() {
        find = Find;
        if (DeselectAllOptions != null) {
            throw new RuntimeException("Use DeselectAllOptions variable to get the single instance of this class.");
        }
    }

    public void deselectAllOptions(By selectDropdownBy) {
        deselectAllOptions(find.find(selectDropdownBy));
    }

    public void deselectAllOptions(WebElement selectDropdownElement) {
        if ("select".equalsIgnoreCase(selectDropdownElement.getTagName())) {
            Select select = new Select(selectDropdownElement);
            select.deselectAll();
        } else {
            throw new NoSuchElementException("Element don't have 'select' tag. This can only work with 'select' tag.");
        }
    }

}