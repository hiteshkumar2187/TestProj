package testScripts;

import applicationPages.Controls;
import applicationPages.Login;
import applicationPages.TicketDetail;
import applicationPages.TicketsList;
import framework.annotation.DataProviderParams;
import framework.core.DriverFactory;
import framework.core.ExecutionConfig;
import framework.util.DataProviderUtil;
import framework.util.email.EmailMessage;
import framework.util.email.EmailUtil;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created by ctpl00694 on 28/6/16.
 */
public class EmailTest {

    @DataProviderParams({"fileName=demo.xls", "sheetName=demo", "tableName=ticket_creation_through_email"})
    @Test(dataProviderClass = DataProviderUtil.class, dataProvider = "ExcelDataProvider")
    public void ticketCreationThroughEmail(String from, String password, String to, String subject, String body){

        EmailUtil.sendEmail(from, to, null, null, subject, body);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebDriver driver = DriverFactory.getDriver();
        driver.get(ExecutionConfig.BASE_URL);
        Login loginPage = new Login();
        loginPage.login("superadmin@akosha.com", "t");

        driver.get("http://admin.onedirectdev.in/admin/showcontrols");
        Controls controlsPage = new Controls();
        controlsPage.emailID().waitUntilEditable();
        controlsPage.emailID().type("varun.helpchat@gmail.com");
        controlsPage.go().click();

        TicketsList ticketsListPage = new TicketsList();
        ticketsListPage.waitUntilLoads();
        ticketsListPage.waitUntilAllAJAXCallsFinish();

        ticketsListPage.assertContainsText(subject);

        ticketsListPage.ticketSubject(subject).click();

        TicketDetail ticketDetailPage = new TicketDetail();
        ticketDetailPage.waitUntilLoads();
        ticketDetailPage.waitUntilAllAJAXCallsFinish();

        ticketDetailPage.reply().click();

        ticketDetailPage.waitUntilFrameAppearsAndSwitchToIt("ui-tinymce-1_ifr");
        ticketDetailPage.pause(5);
        ticketDetailPage.textArea().type("Test Reply");
        driver.switchTo().defaultContent();
        ticketDetailPage.send().click();
        ticketDetailPage.waitUntilAllAJAXCallsFinish();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        EmailMessage emailMessage = EmailUtil.getEmail(from, password, subject);
        EmailUtil.assertEmailSubject(emailMessage, subject);

    }
}
