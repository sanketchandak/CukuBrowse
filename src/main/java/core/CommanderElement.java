package core;

import core.commands.ClickType;
import core.commands.HoverType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;
import java.util.Map;

import static core.commands.Clear.Clear;
import static core.commands.Click.Click;
import static core.commands.DragAndDrop.DragAndDrop;
import static core.commands.Find.Find;
import static core.commands.FindAll.FindAll;
import static core.commands.GetAttribute.GetAttribute;
import static core.commands.GetCssValue.GetCssValue;
import static core.commands.GetInnerHtml.GetInnerHtml;
import static core.commands.GetInnerText.GetInnerText;
import static core.commands.GetLastChild.GetLastChild;
import static core.commands.GetParent.GetParent;
import static core.commands.GetPreceding.GetPreceding;
import static core.commands.GetAncestor.GetAncestor;
import static core.commands.GetSibling.GetSibling;
import static core.commands.GetText.GetText;
import static core.commands.Hover.Hover;
import static core.commands.IsDisplayed.IsDisplayed;
import static core.commands.IsImage.IsImage;
import static core.commands.PressControlA.PressControlA;
import static core.commands.PressControlC.PressControlC;
import static core.commands.PressEnter.PressEnter;
import static core.commands.PressEscape.PressEscape;
import static core.commands.PressTab.PressTab;
import static core.commands.ScrollIntoView.ScrollIntoView;
import static core.commands.SendKeys.SendKeys;
import static core.commands.Submit.Submit;
import static core.commands.UploadFile.UploadFile;
import static core.commands.DeselectAllOptions.DeselectAllOptions;
import static core.commands.DeselectOptionByTextOrIndex.DeselectOptionByTextOrIndex;
import static core.commands.GetSelectedDropdownAttribute.GetSelectedDropdownAttribute;
import static core.commands.GetSelectedDropdownOption.GetSelectedDropdownOption;
import static core.commands.GetSelectedDropdownOptions.GetSelectedDropdownOptions;
import static core.commands.GetSelectedDropdownOptionsText.GetSelectedDropdownOptionsText;
import static core.commands.GetSelectedDropdownOptionText.GetSelectedDropdownOptionText;
import static core.commands.SelectOptionByTextOrIndex.SelectOptionByTextOrIndex;
import static core.commands.SelectOptionContainingText.SelectOptionContainingText;

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
        setElement(Find.find(element, elementBy, index));
        return this;
    }

    public CommanderElements findAll(By elementBy) {
        return new CommanderElements(FindAll.findAll(element, elementBy));
    }

    public CommanderElement getParent() {
        setElement(GetParent.getParent(element));
        return this;
    }

    public CommanderElement getLastChild() {
        setElement(GetLastChild.getLastChild(element));
        return this;
    }

    public CommanderElement getPreceding(int siblingIndex) {
        setElement(GetPreceding.getPreceding(element, siblingIndex));
        return this;
    }

    public CommanderElement getAncestor(String ancestorPath) {
        setElement(GetAncestor.getAncestor(element, ancestorPath));
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
        return GetText.getText(element);
    }

    public void hover(HoverType hoverType) {
        Hover.hover(element, hoverType);
    }

    public boolean isDisplayed() {
        return IsDisplayed.isDisplayed(element);
    }

    public boolean isImage() {
        return IsImage.isImage(element);
    }

    public void pressControlA() {
        PressControlA.pressControlA(element);
    }

    public void pressControlC() {
        PressControlC.pressControlC(element);
    }

    public void pressEnter() {
        PressEnter.pressEnter(element);
    }

    public void pressEscape() {
        PressEscape.pressEscape(element);
    }

    public void pressTab() {
        PressTab.pressTab(element);
    }

    public void scrollIntoView() {
        ScrollIntoView.scrollIntoView(element);
    }

    public void sendKeys(String textToSend) {
        SendKeys.sendKeys(element, textToSend);
    }

    public void submit() {
        Submit.submit(element);
    }

    public void uploadFile(File fileToUpload) {
        UploadFile.uploadFile(element, fileToUpload);
    }

    public void dragAndDropTo(@org.jetbrains.annotations.NotNull CommanderElement destinationCommanderElement) {
        DragAndDrop.dragAndDropTo(element, destinationCommanderElement.getElement());
    }

    public void dragAndDropTo(int xOffset, int yOffset) {
        DragAndDrop.dragAndDropTo(element, xOffset, yOffset);
    }

    public String getAttribute(String attribute) {
        return GetAttribute.getAttribute(element, attribute);
    }

    public Map<String, String> getAttributes(List<String> attributes) {
        return GetAttribute.getAttributes(element, attributes);
    }

    public String getCssValue(String cssPropertyName) {
        return GetCssValue.getCssValue(element, cssPropertyName);
    }

    public Map<String, String> getCssValues(List<String> cssPropertyNames) {
        return GetCssValue.getCssValues(element, cssPropertyNames);
    }

    public String getInnerHtml() {
        return GetInnerHtml.getInnerHtml(element);
    }

    public String getInnerText() {
        return GetInnerText.getInnerText(element);
    }

    public void deselectAllDropdownOptions() {
        DeselectAllOptions.deselectAllOptions(element);
    }

    public void deselectDropdownOptionsByIndexes(int[] indexes) {
        DeselectOptionByTextOrIndex.deselectOptionsByIndexes(element, indexes);
    }

    public void deselectDropdownOptionsByTexts(String[] texts) {
        DeselectOptionByTextOrIndex.deselectOptionsByTexts(element, texts);
    }

    public List<String> getSelectedDropdownAttribute(String attribute, By... dropdownChildOptionBy) throws Exception {
        return GetSelectedDropdownAttribute.getSelectedDropdownAttribute(element, attribute, dropdownChildOptionBy);
    }

    public CommanderElement getSelectedDropdownOption(By... dropdownChildOptionBy) throws Exception {
        setElement(GetSelectedDropdownOption.getSelectedDropdownOption(element, dropdownChildOptionBy));
        return this;
    }

    public CommanderElements getSelectedDropdownOptions(By... dropdownChildOptionBy) throws Exception {
        return new CommanderElements(GetSelectedDropdownOptions.getSelectedDropdownOptions(element, dropdownChildOptionBy));
    }

    public List<String> getSelectedDropdownOptionsText(By... dropdownChildOptionBy) throws Exception {
        return GetSelectedDropdownOptionsText.getSelectedDropdownOptionsText(element, dropdownChildOptionBy);
    }

    public String getSelectedDropdownOptionText(By... dropdownChildOptionBy) throws Exception {
        return GetSelectedDropdownOptionText.getSelectedDropdownOptionText(element, dropdownChildOptionBy);
    }

    public void selectDropdownOptionsByIndexes(int[] indexes, By... elementDropdownOptionsBy) {
        SelectOptionByTextOrIndex.selectOptionsByIndexes(element, indexes, elementDropdownOptionsBy);
    }

    public void selectDropdownOptionsByTexts(String[] texts, By... elementDropdownOptionsBy) throws Exception {
        SelectOptionByTextOrIndex.selectOptionsByTexts(element, texts, elementDropdownOptionsBy);
    }

    public void selectOptionsContainingTexts(String text, By... elementDropdownOptionsBy) throws Exception {
        SelectOptionContainingText.selectOptionsContainingTexts(element, text, elementDropdownOptionsBy);
    }

}
