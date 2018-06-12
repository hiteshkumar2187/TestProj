package framework.element;

import framework.core.DriverFactory;
import framework.element.internal.ITextInput;
import org.fest.assertions.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;

public class HTMTextInput extends HTMWebElement implements ITextInput {

    public HTMTextInput(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
        setElementType("Input Box");
    }

    public void clearAndType(String text) {
        Reporter.log("<b>Clear and type into " + getElementType() + "</b><br> Page | Element | Text => " + getPageName() + " | " + getElementName() + " | " + text + "<br>");
        getWrappedElement().clear();
        getWrappedElement().sendKeys(text);
    }

    public void type(String text) {
        Reporter.log("<b>Type into " + getElementType() + "</b><br> Page | Element | Text => " + getPageName() + " | " + getElementName() + " | " + text + "<br>");
        getWrappedElement().sendKeys(text);
    }

    @Override
    public String getText() {
        return getWrappedElement().getAttribute("value");
    }

    @Override
    public void assertText(String text){
        Reporter.log("<b>Assert that " + getElementType() + " has text</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        Assertions.assertThat(getText()).isEqualToIgnoringCase(text);
    }

    @Override
    public void assertContainsText(String text){
        Reporter.log("<b>Assert that " + getElementType() + " contains text</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        Assertions.assertThat(getText()).containsIgnoringCase(text);
    }

    @Override
    public void assertDoesNotContainText(String text){
        Reporter.log("<b>Assert that " + getElementType() + " doesn't contain text</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        Assertions.assertThat(getText()).doesNotContain(text);
    }

    public void waitUntilContainsText(String text){
        Reporter.log("<b>Wait until " + getElementType() + " contains text </b><br> Page | Element | Text => " + getPageName() + " | " + getElementName() + " | " + text + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.textToBePresentInElementValue(getBy(), text));
    }

    public void waitUntilDoesNotContainText(String text){
        Reporter.log("<b>Wait until " + getElementType() + " doesn't contain text </b><br> Page | Element | Text => " + getPageName() + " | " + getElementName() + " | " + text + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(getBy(), text)));
    }

}