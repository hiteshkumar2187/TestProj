package framework.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomExpectedConditions {

    private final static Logger log = Logger.getLogger(CustomExpectedConditions.class.getName());

    public static ExpectedCondition<Boolean> attributeValueToBeContainedInElement(
            final By locator, final String attributeName, final String expectedAttributeValue) {

        return new ExpectedCondition<Boolean> () {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementAttribute = findElement(locator, driver).getAttribute(attributeName);
                    return elementAttribute.contains(expectedAttributeValue);
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> attributeToBeContainedInElement(
            final By locator, final String attributeName) {

        return new ExpectedCondition<Boolean> () {
            public Boolean apply(WebDriver driver) {
                try {
                    String elementAttributeValue = findElement(locator, driver).getAttribute(attributeName);
                    return elementAttributeValue != null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> optionToBeSelectedInElement(
            final Select selectElement, final String optionText, boolean byVisibleText) {

        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement firstSelectedOption = selectElement.getFirstSelectedOption();
                    if(byVisibleText){
                        return firstSelectedOption.getText().equalsIgnoreCase(optionText);
                    }else{
                        return firstSelectedOption.getAttribute("value").equalsIgnoreCase(optionText);
                    }
                } catch (StaleElementReferenceException e) {
                    return null;
                } catch (NoSuchElementException e){
                    return false;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> angularHasFinishedProcessing() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return Boolean.valueOf(((JavascriptExecutor)
                        driver).executeScript("return (window.angular !== undefined) && " +
                        "(angular.element(document).injector() !== undefined) && " +
                        "(angular.element(document).injector().get('$http').pendingRequests.length === 0)").toString());
            }
        };
    }

    public static ExpectedCondition<Boolean> jQueryAJAXCallsHaveCompleted() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor)
                        driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    public static ExpectedCondition<Boolean> documentIsReady(){
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript( "return document.readyState").equals("complete");
            }
        };
    }

    public static ExpectedCondition<Boolean> documentIsLoading(){
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript( "return document.readyState").equals("loading");
            }
        };
    }

    public static ExpectedCondition<Boolean> documentIsInteractive(){
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript( "return document.readyState").equals("interactive");
            }
        };
    }

    private static WebElement findElement(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (WebDriverException e) {
            log.log(Level.WARNING,
                    String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }

    private static List<WebElement> findElements(By by, WebDriver driver) {
        try {
            return driver.findElements(by);
        } catch (WebDriverException e) {
            log.log(Level.WARNING,
                    String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }
}
