package framework.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DriverFactory {

    private static List<WebDriverThread> webDriverThreadPool =
            Collections.synchronizedList(new ArrayList<WebDriverThread>());

    private static ThreadLocal<WebDriverThread> driverThread = new ThreadLocal<WebDriverThread>(){
        @Override
        protected WebDriverThread initialValue(){
            WebDriverThread webDriverThread = new WebDriverThread();
            webDriverThreadPool.add(webDriverThread);
            return webDriverThread;
        }
    };

    private static ThreadLocal<WebDriverWait> waitThread = new ThreadLocal<WebDriverWait>(){
        @Override
        protected WebDriverWait initialValue(){
            WebDriverWait wait = new WebDriverWait(getDriver(), ExecutionConfig.WAIT_TIME);
            return wait;
        }
    };

    public static WebDriver getDriver(){
        return driverThread.get().getDriver();
    }

    public static WebDriverWait getWait(){
        return waitThread.get();
    }

    public static void setWaitTime(int seconds){
        waitThread.set(new WebDriverWait(getDriver(), seconds));
    }

    public static void resetWaitTime(){
        waitThread.set(new WebDriverWait(getDriver(), ExecutionConfig.WAIT_TIME));
    }

    public static void closeDriverObjects(){
        for(WebDriverThread webDriverThread: webDriverThreadPool){
            webDriverThread.quitDriver();
        }
    }
}