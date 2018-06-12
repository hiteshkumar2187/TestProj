package framework.core.config;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.Arrays;
import java.util.HashMap;

public enum DriverType implements DriverSetup {

    FIREFOX {
        public DesiredCapabilities getDesiredCapabilities(){
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            return capabilities;
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities){
            return new FirefoxDriver(capabilities);
        }
    },

    CHROME {
        public DesiredCapabilities getDesiredCapabilities(){
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
            HashMap<String, String> chromePreferences = new HashMap<String, String>();
            chromePreferences.put("profile.password_manager_enabled", "false");
            capabilities.setCapability("chrome.prefs", chromePreferences);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--test-type");
            options.addArguments("--allow-running-insecure-content");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return capabilities;
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities){
            ChromeDriverManager.setup();
            return new ChromeDriver(capabilities);
        }
    },

    IE {
        public DesiredCapabilities getDesiredCapabilities(){
            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            capabilities.setCapability("requireWindowFocus", true);
            return capabilities;
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities){
            InternetExplorerDriverManager.setup();
            return new InternetExplorerDriver(capabilities);
        }
    }
}
