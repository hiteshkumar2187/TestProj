package testScripts;

import applicationPages.Login;
import framework.annotation.DataProviderParams;
import framework.base.BaseTest;
import framework.core.DriverFactory;
import framework.core.ExecutionConfig;
import framework.util.DataProviderUtil;
import org.fest.assertions.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created by ctpl00694 on 21/6/16.
 */
public class Sample1 extends BaseTest {

    @DataProviderParams({"fileName=demo.xls", "sheetName=demo", "tableName=login_fail_wrong_details"})
    @Test(description = "Validate error messages on invalid login  credentials", dataProviderClass = DataProviderUtil.class, dataProvider = "ExcelDataProvider")
    public void ODAUT100(String email, String password, String errorMessage){
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().deleteAllCookies();
        driver.get(ExecutionConfig.BASE_URL);
        Login loginPage = new Login();
        loginPage.waitUntilLoads();
        loginPage.txtEmail().waitUntilEditable();
        loginPage.txtEmail().type(email);
        loginPage.txtPassword().type(password);
        loginPage.btnLogin().click();
        loginPage.waitUntilAllAJAXCallsFinish();
        loginPage.assertContainsText(errorMessage);
    }
}
