package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.Arrays;

import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;

public class DeselectOptionByTextOrIndex {
    private static final Logger logger = LoggerFactory.getLogger(DeselectOptionByTextOrIndex.class);
    public static DeselectOptionByTextOrIndex DeselectOptionByTextOrIndex =
            ThreadLocal.withInitial(DeselectOptionByTextOrIndex::new).get();
    private final Find find;

    private DeselectOptionByTextOrIndex() {
        find = Find;
        if (DeselectOptionByTextOrIndex != null) {
            logger.error("Use DeselectOptionByTextOrIndex variable to get the single instance of this class.");
            throw new CukeBrowseException("Use DeselectOptionByTextOrIndex variable to get the single instance of this class.");
        }
    }

    public void deselectOptionsByIndexes(By selectDropdownBy, int[] indexes) {
        deselectOptionsByIndexes(find.find(selectDropdownBy), indexes);
        logger.info(String.format("Deselect Options By Indexes: Deselect Options in dropdown '%s' by indexes: '%s' ", selectDropdownBy.toString(), Arrays.toString(indexes)));
    }

    public void deselectOptionsByIndexes(WebElement selectDropdownElement, int[] indexes) {
        if ("select".equalsIgnoreCase(selectDropdownElement.getTagName())) {
            Select select = new Select(selectDropdownElement);
            for (int index : indexes) {
                try {
                    select.deselectByIndex(index);
                } catch (NoSuchElementException e) {
                    logger.error(String.format("Deselect Options By Indexes: /option[index:%s] is not present. ", index), e);
                    throw new CukeBrowseException("Deselect Options By Indexes: /option[index:" + index + "] is not present. ", e);
                }
            }
            if (!Thread.currentThread().getStackTrace()[2].getMethodName().equalsIgnoreCase("deselectOptionsByIndexes")) {
                logger.info(String.format("Deselect Options By Indexes: Deselect Options in dropdown '%s' by indexes: '%s' ", GetInnerHtml.getInnerHtml(selectDropdownElement), Arrays.toString(indexes)));
            }
        } else {
            logger.error(String.format("Deselect Options By Indexes: '%s' Element don't have 'select' tag. This can only work with 'select' tag.", GetInnerHtml.getInnerHtml(selectDropdownElement)));
            throw new CukeBrowseException(String.format("Deselect Options By Indexes: '%s' Element don't have 'select' tag. This can only work with 'select' tag.", GetInnerHtml.getInnerHtml(selectDropdownElement)));
        }
    }

    public void deselectOptionsByTexts(By selectDropdownBy, String[] texts) {
        deselectOptionsByTexts(find.find(selectDropdownBy), texts);
        logger.info(String.format("Deselect Options By Texts: Deselect Options in dropdown '%s' by indexes: '%s' ", selectDropdownBy.toString(), Arrays.toString(texts)));
    }

    public void deselectOptionsByTexts(WebElement selectDropdownElement, String[] texts) {
        if ("select".equalsIgnoreCase(selectDropdownElement.getTagName())) {
            Select select = new Select(selectDropdownElement);
            for (String text : texts) {
                try {
                    select.deselectByVisibleText(text);
                } catch (NoSuchElementException e) {
                    logger.error(String.format("Deselect Options By Texts: /option[index:%s] is not present. ", text), e);
                    throw new CukeBrowseException("Deselect Options By Texts: /option[text:" + text + "] is not present. ", e);
                }
            }
            logger.info(String.format("Deselect Options By Texts: Deselect Options in dropdown '%s' by indexes: '%s' ", GetInnerHtml.getInnerHtml(selectDropdownElement), Arrays.toString(texts)));
        } else {
            logger.error(String.format("Deselect Options By Texts: '%s' Element don't have 'select' tag. This can only work with 'select' tag.", GetInnerHtml.getInnerHtml(selectDropdownElement)));
            throw new CukeBrowseException(String.format("Deselect Options By Texts: '%s' Element don't have 'select' tag. This can only work with 'select' tag.", GetInnerHtml.getInnerHtml(selectDropdownElement)));
        }
    }
}
