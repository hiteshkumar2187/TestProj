package framework.element;

import framework.core.DriverFactory;
import framework.element.internal.IRadioButton;
import org.fest.assertions.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;

public class HTMRadioButton extends HTMWebElement implements IRadioButton {

    public HTMRadioButton(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
        setElementType("Radio HTMButton");
    }

    public void select() {
        Reporter.log("<b>HTMSelect " + getElementType() + "</b><br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        if (!isSelected()) {
            click();
        }
    }

    public boolean isSelected() {
        return getWrappedElement().isSelected();
    }

    public void assertSelected(){
        Reporter.log("<b>Assert that " + getElementType() + " is selected</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        Assertions.assertThat(isSelected()).isEqualTo(true);
    }

    public void assertDeSelected(){
        Reporter.log("<b>Assert that " + getElementType() + " is not selected</b> <br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        Assertions.assertThat(isSelected()).isEqualTo(false);
    }

    public void waitUntilSelected(){
        Reporter.log("<b>Wait until " + getElementType() + " is selected</b> <br> Page | element => " + getPageName() + " | " + getElementName());
        DriverFactory.getWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), true));
    }

    public void waitUntilDeSelected(){
        Reporter.log("<b>Wait until " + getElementType() + " is de-selected</b> <br> Page | element => " + getPageName() + " | " + getElementName());
        DriverFactory.getWait().until(ExpectedConditions.elementSelectionStateToBe(getBy(), false));
    }

}