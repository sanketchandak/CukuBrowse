package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static core.commands.Find.Find;

public class DeselectOptionByTextOrIndex {
    public static DeselectOptionByTextOrIndex DeselectOptionByTextOrIndex =
            ThreadLocal.withInitial(DeselectOptionByTextOrIndex::new).get();
    private final Find find;

    private DeselectOptionByTextOrIndex() {
        find = Find;
        if (DeselectOptionByTextOrIndex != null) {
            throw new RuntimeException("Use DeselectOptionByTextOrIndex variable to get the single instance of this class.");
        }
    }

    public void deselectOptionsByIndexes(By selectDropdownElement, int[] indexes) {
        deselectOptionsByIndexes(find.find(selectDropdownElement),
                indexes);
    }

    public void deselectOptionsByIndexes(WebElement selectDropdownElement, int[] indexes) {
        if ("select".equalsIgnoreCase(selectDropdownElement.getTagName())) {
            Select select = new Select(selectDropdownElement);
            for (int index : indexes) {
                try {
                    select.deselectByIndex(index);
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("/option[index:" + index + "] is not present");
                }
            }
        } else {
            throw new NoSuchElementException("Element don't have 'select' tag. This can only work with 'select' tag.");
        }
    }

    public void deselectOptionsByTexts(By selectDropdownBy, String[] texts) {
        deselectOptionsByTexts(find.find(selectDropdownBy), texts);
    }

    public void deselectOptionsByTexts(WebElement selectDropdownElement, String[] texts) {
        if ("select".equalsIgnoreCase(selectDropdownElement.getTagName())) {
            Select select = new Select(selectDropdownElement);
            for (String text : texts) {
                try {
                    select.deselectByVisibleText(text);
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("/option[text:" + text + "] is not present");
                }
            }
        } else {
            throw new NoSuchElementException("Element don't have 'select' tag. This can only work with 'select' tag.");
        }
    }
}
