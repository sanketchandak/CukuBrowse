package core.web;

import core.web.commands.ClickType;
import core.web.commands.HoverType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;
import java.util.Map;

import static core.web.commands.Clear.Clear;
import static core.web.commands.Click.Click;
import static core.web.commands.DragAndDrop.DragAndDrop;
import static core.web.commands.Find.Find;
import static core.web.commands.FindAll.FindAll;
import static core.web.commands.GetAttribute.GetAttribute;
import static core.web.commands.GetCssValue.GetCssValue;
import static core.web.commands.GetInnerHtml.GetInnerHtml;
import static core.web.commands.GetInnerText.GetInnerText;
import static core.web.commands.GetLastChild.GetLastChild;
import static core.web.commands.GetParent.GetParent;
import static core.web.commands.GetPreceding.GetPreceding;
import static core.web.commands.GetAncestor.GetAncestor;
import static core.web.commands.GetSibling.GetSibling;
import static core.web.commands.GetText.GetText;
import static core.web.commands.Hover.Hover;
import static core.web.commands.IsDisplayed.IsDisplayed;
import static core.web.commands.IsImage.IsImage;
import static core.web.commands.PressControlA.PressControlA;
import static core.web.commands.PressControlC.PressControlC;
import static core.web.commands.PressEnter.PressEnter;
import static core.web.commands.PressEscape.PressEscape;
import static core.web.commands.PressTab.PressTab;
import static core.web.commands.ScrollIntoView.ScrollIntoView;
import static core.web.commands.SendKeys.SendKeys;
import static core.web.commands.Submit.Submit;
import static core.web.commands.UploadFile.UploadFile;
import static core.web.commands.DeselectAllOptions.DeselectAllOptions;
import static core.web.commands.DeselectOptionByTextOrIndex.DeselectOptionByTextOrIndex;
import static core.web.commands.GetSelectedDropdownAttribute.GetSelectedDropdownAttribute;
import static core.web.commands.GetSelectedDropdownOption.GetSelectedDropdownOption;
import static core.web.commands.GetSelectedDropdownOptions.GetSelectedDropdownOptions;
import static core.web.commands.GetSelectedDropdownOptionsText.GetSelectedDropdownOptionsText;
import static core.web.commands.GetSelectedDropdownOptionText.GetSelectedDropdownOptionText;
import static core.web.commands.SelectOptionByTextOrIndex.SelectOptionByTextOrIndex;
import static core.web.commands.SelectOptionContainingText.SelectOptionContainingText;

public class CommanderElement {

    private WebElement element;

    CommanderElement(WebElement element) {
        setElement(element);
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public CommanderElement find(By elementBy, int... index) {
        setElement(Find.findElement(element, elementBy, index));
        return this;
    }

    public CommanderElements findAll(By elementBy) {
        return new CommanderElements(FindAll.find(element, elementBy));
    }

    public CommanderElement getParent() {
        setElement(GetParent.get(element));
        return this;
    }

    public CommanderElement getLastChild() {
        setElement(GetLastChild.get(element));
        return this;
    }

    public CommanderElement getPreceding(int siblingIndex) {
        setElement(GetPreceding.get(element, siblingIndex));
        return this;
    }

    public CommanderElement getAncestor(String ancestorPath) {
        setElement(GetAncestor.get(element, ancestorPath));
        return this;
    }

    public CommanderElement getFollowingSibling(int siblingIndex) {
        setElement(GetSibling.getFollowingSibling(element, siblingIndex));
        return this;
    }

    public CommanderElement getPrecedingSibling(int siblingIndex) {
        setElement(GetSibling.getPrecedingSibling(element, siblingIndex));
        return this;
    }

    public void clearBox() {
        Clear.clearBox(element);
    }

    public void clickOn(ClickType clickType) {
        Click.clickOn(element, clickType);
    }

    public String getText() {
        return GetText.get(element);
    }

    public void hover(HoverType hoverType) {
        Hover.hover(element, hoverType);
    }

    public boolean isDisplayed() {
        return IsDisplayed.displayed(element);
    }

    public boolean isImage() {
        return IsImage.image(element);
    }

    public void pressControlA() {
        PressControlA.press(element);
    }

    public void pressControlC() {
        PressControlC.press(element);
    }

    public void pressEnter() {
        PressEnter.press(element);
    }

    public void pressEscape() {
        PressEscape.press(element);
    }

    public void pressTab() {
        PressTab.press(element);
    }

    public void scrollIntoView() {
        ScrollIntoView.scroll(element);
    }

    public void sendKeys(String textToSend) {
        SendKeys.send(element, textToSend);
    }

    public void submit() {
        Submit.submit(element);
    }

    public void uploadFile(File fileToUpload) {
        UploadFile.upload(element, fileToUpload);
    }

    public void dragAndDropTo(@org.jetbrains.annotations.NotNull CommanderElement destinationCommanderElement) {
        DragAndDrop.dragAndDropTo(element, destinationCommanderElement.getElement());
    }

    public void dragAndDropTo(int xOffset, int yOffset) {
        DragAndDrop.dragAndDropTo(element, xOffset, yOffset);
    }

    public String getAttribute(String attribute) {
        return GetAttribute.get(element, attribute);
    }

    public Map<String, String> getAttributes(List<String> attributes) {
        return GetAttribute.get(element, attributes);
    }

    public String getCssValue(String cssPropertyName) {
        return GetCssValue.get(element, cssPropertyName);
    }

    public Map<String, String> getCssValues(List<String> cssPropertyNames) {
        return GetCssValue.get(element, cssPropertyNames);
    }

    public String getInnerHtml() {
        return GetInnerHtml.get(element);
    }

    public String getInnerText() {
        return GetInnerText.get(element);
    }

    public void deselectAllDropdownOptions() {
        DeselectAllOptions.deselectAll(element);
    }

    public void deselectDropdownOptionsByIndexes(int[] indexes) {
        DeselectOptionByTextOrIndex.deselectOptionsByIndexes(element, indexes);
    }

    public void deselectDropdownOptionsByTexts(String[] texts) {
        DeselectOptionByTextOrIndex.deselectOptionsByTexts(element, texts);
    }

    public List<String> getSelectedDropdownAttribute(String attribute, By... dropdownChildOptionBy) {
        return GetSelectedDropdownAttribute.get(element, attribute, dropdownChildOptionBy);
    }

    public CommanderElement getSelectedDropdownOption(By... dropdownChildOptionBy) {
        setElement(GetSelectedDropdownOption.get(element, dropdownChildOptionBy));
        return this;
    }

    public CommanderElements getSelectedDropdownOptions(By... dropdownChildOptionBy) {
        return new CommanderElements(GetSelectedDropdownOptions.get(element, dropdownChildOptionBy));
    }

    public List<String> getSelectedDropdownOptionsText(By... dropdownChildOptionBy) {
        return GetSelectedDropdownOptionsText.get(element, dropdownChildOptionBy);
    }

    public String getSelectedDropdownOptionText(By... dropdownChildOptionBy) {
        return GetSelectedDropdownOptionText.get(element, dropdownChildOptionBy);
    }

    public void selectDropdownOptionsByIndexes(int[] indexes, By... elementDropdownOptionsBy) {
        SelectOptionByTextOrIndex.selectOptionsByIndexes(element, indexes, elementDropdownOptionsBy);
    }

    public void selectDropdownOptionsByTexts(String[] texts, By... elementDropdownOptionsBy) {
        SelectOptionByTextOrIndex.selectOptionsByTexts(element, texts, elementDropdownOptionsBy);
    }

    public void selectOptionsContainingTexts(String text, By... elementDropdownOptionsBy) {
        SelectOptionContainingText.selectOptionsContainingTexts(element, text, elementDropdownOptionsBy);
    }
}
