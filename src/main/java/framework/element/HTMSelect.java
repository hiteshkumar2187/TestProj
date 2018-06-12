package framework.element;

import framework.core.DriverFactory;
import framework.element.internal.ISelect;
import framework.util.CustomExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;

public class HTMSelect extends HTMWebElement implements ISelect {

    public HTMSelect(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
        setElementType("Drop-down");
    }

    public boolean isMultiple() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement()).isMultiple();
    }

    public void deselectByIndex(int index) {
        Reporter.log("<b>De-select from " + getElementType() + " by index</b> <br> Page | Element | Index => " + getPageName() + " | " + getElementName() + " | " + index + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectByIndex(index);
    }

    public void selectByValue(String value) {
        Reporter.log("<b>HTMSelect from " + getElementType() + " by value</b> <br> Page | Element | Value => " + getPageName() + " | " + getElementName() + " | " + value + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).selectByValue(value);
    }

    public WebElement getFirstSelectedOption() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement()).getFirstSelectedOption();
    }

    public void selectByVisibleText(String text) {
        Reporter.log("<b>HTMSelect from " + getElementType() + " by visible text</b> <br> Page | Element | Text => " + getPageName() + " | " + getElementName() + " | " + text + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).selectByVisibleText(text);
    }

    public void deselectByValue(String value) {
        Reporter.log("<b>De-select from " + getElementType() + " by value</b> <br> Page | Element | Value => " + getPageName() + " | " + getElementName() + " | " + value + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectByValue(value);
    }

    public void deselectAll() {
        Reporter.log("<b>De-select all from " + getElementType() + "</b><br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectAll();
    }

    public List<WebElement> getAllSelectedOptions() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement()).getAllSelectedOptions();
    }

    public List<WebElement> getOptions() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement()).getOptions();
    }

    public void deselectByVisibleText(String text) {
        Reporter.log("<b>De-select from " + getElementType() + " by visible text</b> <br> Page | Element | Text => " + getPageName() + " | " + getElementName() + " | " + text + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).deselectByVisibleText(text);
    }

    public void selectByIndex(int index) {
        Reporter.log("<b>HTMSelect from " + getElementType() + " by index</b> <br> Page | Element | Index => " + getPageName() + " | " + getElementName() + " | " + index + "<br>");
        new org.openqa.selenium.support.ui.Select(getWrappedElement()).selectByIndex(index);
    }

    public void waitUntilSelected(){
        Reporter.log("<b>Wait until " + getElementType() + " is selected</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), true));
    }

    public void waitUntilDeSelected(){
        Reporter.log("<b>Wait until " + getElementType() + " is de-selected</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), false));
    }

    public void waitUntilOptionToBeSelectedByVisibeText(String optionText){
        Reporter.log("<b>Wait until " + getElementType() + " has option selected by visible text</b> <br> Page | Element | Option => " + getPageName() + " | " + getElementName() + " | " + optionText + "<br>");
        DriverFactory.getWait().until(CustomExpectedConditions.optionToBeSelectedInElement(new org.openqa.selenium.support.ui.Select(getWrappedElement()), optionText, true));
    }

    public void waitUntilOptionToBeSelectedByValue(String optionValue){
        Reporter.log("<b>Wait until " + getElementType() + " has option selected by value</b> <br> Page | Element | Option => " + getPageName() + " | " + getElementName() + " | " + optionValue + "<br>");
        DriverFactory.getWait().until(CustomExpectedConditions.optionToBeSelectedInElement(new org.openqa.selenium.support.ui.Select(getWrappedElement()), optionValue, false));
    }
}