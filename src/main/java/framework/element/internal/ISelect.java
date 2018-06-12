package framework.element.internal;

import org.openqa.selenium.WebElement;
import java.util.List;

public interface ISelect extends IWebElement {

    boolean isMultiple();

    void deselectByIndex(int index);

    void selectByValue(String value);

    WebElement getFirstSelectedOption();

    void selectByVisibleText(String text);

    void deselectByValue(String value);

    void deselectAll();

    List<WebElement> getAllSelectedOptions();

    List<WebElement> getOptions();

    void deselectByVisibleText(String text);

    void selectByIndex(int index);

}
