package applicationPages;

import framework.base.BasePage;
import framework.element.HTMLink;
import org.openqa.selenium.By;

/**
 * Created by ctpl00694 on 30/6/16.
 */
public class TicketsList extends BasePage {
    public TicketsList() {
        super("Tickets List");
    }

    public HTMLink ticketSubject(String subject){
        return new HTMLink(By.linkText(subject), getPageName(), "Subject Link");
    }
}
