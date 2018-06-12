package framework.element.internal;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

public interface IWebElement extends WebElement, WrapsElement, Locatable {

    boolean elementWired();

}