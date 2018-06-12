package framework.element;

import framework.element.internal.IAlert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class HTMAlert extends HTMWebElement implements IAlert {

    public HTMAlert(By by, String pageName, String elementName) {
        super(by, pageName, elementName);
        setElementType("HTMAlert");
    }

    public void accept() {
        Reporter.log("<b>Accept alert</b><br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        WebElement button = getWrappedElement().findElement(By.tagName("button"));

        try{
            button.click();
        }catch(WebDriverException e){
            if(e.getMessage().contains("unknown error: Element is not clickable at point")){
                Reporter.log("Retrying due to the error: Element is not clickable at point(X, Y)<br>");
                try { Thread.sleep(1000); } catch (InterruptedException e1) { e1.printStackTrace(); }
                button.click();
            }else{
                throw e;
            }
        }
    }

    public void close() {
        Reporter.log("<b>Close alert</b><br> Page | Element => " + getPageName() + " | " + getElementName() + "<br>");
        WebElement cross = getWrappedElement().findElement(By.cssSelector(".ui-icon.ui-icon-closethick"));
        try{
            cross.click();
        }catch(WebDriverException e){
            if(e.getMessage().contains("unknown error: Element is not clickable at point")){
                Reporter.log("Retrying due to the error: Element is not clickable at point(X, Y)<br>");
                try { Thread.sleep(1000); } catch (InterruptedException e1) { e1.printStackTrace(); }
                cross.click();
            }else{
                throw e;
            }
        }
    }

}