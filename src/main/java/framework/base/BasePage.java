package framework.base;

import framework.core.ExecutionConfig;
import framework.core.DriverFactory;
import framework.element.HTMAlert;
import framework.util.CustomExpectedConditions;
import org.fest.assertions.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import java.util.concurrent.TimeUnit;

public abstract class BasePage {

    protected static final String PATTERN_TO_REPLACE = "PATTERN";
    protected String pageName;

    public HTMAlert alert() {
        return new HTMAlert(By.xpath("//div[@aria-labelledby='ui-dialog-title-alert']"), getPageName(), "HTMAlert");
    }

    public String getPageName() {
        return this.pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public BasePage(String pageName) {
        this.pageName = pageName;
    }

    protected By getDynamicBy(By by, String... args) {
        String dynamicLocatorString = getLocatorStringFromBy(by);
        if (args.length != 0) {
            dynamicLocatorString = searchAndReplaceDynamicPattern(dynamicLocatorString, args);
        }
        String dynamicLocatorStrategy = getLocatorStrategyFromBy(by);

        if (dynamicLocatorStrategy.equalsIgnoreCase("id")) {
            by = By.id(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("name")) {
            by = By.name(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("className")) {
            by = By.className(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("cssSelector")) {
            by = By.cssSelector(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("linkText")) {
            by = By.linkText(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("partialLinkText")) {
            by = By.partialLinkText(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("tagName")) {
            by = By.tagName(dynamicLocatorString);
        } else if (dynamicLocatorStrategy.equalsIgnoreCase("xpath")) {
            by = By.xpath(dynamicLocatorString);
        } else {
            // some issue with the locator which needs to be handled
        }
        return by;
    }

    private String getLocatorStringFromBy(By by) {
        String[] data = by.toString().trim().split(" ");
        if (data.length != 2) {
            // TODO some issue with the locator which needs to be handled
        }
        return data[1].trim();
    }

    private String getLocatorStrategyFromBy(By by) {
        String str = by.toString().trim();
        String subString = by.toString().trim().substring(0, str.indexOf(':'));
        String[] data = subString.trim().split("\\.");
        if (data.length != 2) {
            // TODO some issue with the locator which needs to be handled
        }
        return data[1].trim();
    }

    private String searchAndReplaceDynamicPattern(String dynamicLocatorString, String... args) {
        for (String str : args) {
            dynamicLocatorString = dynamicLocatorString.replaceFirst(PATTERN_TO_REPLACE, str);
        }
        return dynamicLocatorString;
    }

    public void assertContainsText(String text) {
        Reporter.log("<b>Assert that applicationPages contains text</b> <br> Page | Text => " + pageName + " | " + text + "<br>");
        Assertions.assertThat(DriverFactory.getDriver().findElement(By.tagName("body")).getText()).containsIgnoringCase(text);
    }

    public void assertDoesNotContainText(String text) {
        Reporter.log("<b>Assert that applicationPages doesn't contain text</b> <br> Page | Text => " + pageName + " | " + text + "<br>");
        Assertions.assertThat(DriverFactory.getDriver().findElement(By.tagName("body")).getText()).doesNotContain(text);
    }

    public void assertContainsTitle(String title) {
        Reporter.log("<b>Assert that applicationPages contains title</b> <br> Page | Text => " + pageName + " | " + title + "<br>");
        Assertions.assertThat(DriverFactory.getDriver().getTitle()).containsIgnoringCase(title);
    }

    public void waitUntilLoads() {
        Reporter.log("<b>Wait until applicationPages loads</b><br>");
        int timeToWaitTillDocumentStartsLoading = 5;
        try {
            // It is important to first wait till the document has started loading otherwise tests fail intermittently
            // when the execution is fast on some browser/environment. e.g. if clicking on a link results in a
            // applicationPages load & we have a waitForPageLoad after click then if the document hasn't started to load, it will
            // assume that the document is ready
            DriverFactory.setWaitTime(timeToWaitTillDocumentStartsLoading);
            DriverFactory.getWait().until(CustomExpectedConditions.documentIsLoading());
            DriverFactory.setWaitTime(ExecutionConfig.WAIT_TIME - timeToWaitTillDocumentStartsLoading);
            DriverFactory.getWait().until(CustomExpectedConditions.documentIsReady());
        } catch (Throwable e) {
            // Do nothing since we don't want to fail a test if the applicationPages hasn't loaded completely
        } finally {
            DriverFactory.resetWaitTime();
        }
    }

    public void waitUntilContainsTitle(final String title) {
        Reporter.log("<b>Wait until applicationPages contains title</b> <br> Title => " + title + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.titleContains(title));
    }

    public void waitUntilContainsText(final String text) {
        Reporter.log("<b>Wait until applicationPages contains text</b> <br> Text => " + text + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text));
    }

    public void waitUntilDoesNotContainText(final String text) {
        Reporter.log("<b>Wait until applicationPages doesn't contain text</b> <br> Text => " + text + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.not
                (ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text)));
    }

    public void waitUntilAllAJAXCallsFinish() {
        Reporter.log("<b>Wait until all ajax requests complete</b><br> Page => " + getPageName() + "<br>");
        WebDriverWait wait = DriverFactory.getWait();
        long timeOutInMilliSeconds = ExecutionConfig.WAIT_TIME * 1000;
        long startTime = System.currentTimeMillis();


        try{

            if(timeOutInMilliSeconds > 0) {
                wait.withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS).until(CustomExpectedConditions.jQueryAJAXCallsHaveCompleted());
            }
            pause(1);

            timeOutInMilliSeconds = timeOutInMilliSeconds - (System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
            if(timeOutInMilliSeconds > 0) {
                wait.withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS).until(CustomExpectedConditions.jQueryAJAXCallsHaveCompleted());
            }
            pause(1);

            timeOutInMilliSeconds = timeOutInMilliSeconds - (System.currentTimeMillis() - startTime);
            if(timeOutInMilliSeconds > 0) {
                wait.withTimeout(timeOutInMilliSeconds, TimeUnit.MILLISECONDS).until(CustomExpectedConditions.jQueryAJAXCallsHaveCompleted());
            }
        }catch (Throwable e){
            // Do nothing since we don't want to fail a test case in case of ongoing request
        }
    }

    public void waitUntilAngularProcessingFinish() {
        Reporter.log("<b>Wait until angular js has finished processing</b><br> Page => " + getPageName() + "<br>");
        DriverFactory.getWait().until(CustomExpectedConditions.angularHasFinishedProcessing());
    }

    public void waitUntilFrameAppearsAndSwitchToIt(String frameLocator) {
        Reporter.log("<b>Wait until frame appears and switch to it</b> <br> Frame Locator => " + frameLocator + "<br>");
        DriverFactory.getWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    public void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getElementsCount(By by) {
        return DriverFactory.getDriver().findElements(by).size();
    }

}