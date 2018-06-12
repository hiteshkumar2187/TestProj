package applicationPages;

import framework.base.BasePage;
import framework.element.HTMButton;
import framework.element.HTMLink;
import framework.element.HTMTextInput;
import org.openqa.selenium.By;

/**
 * Created by ctpl00694 on 30/6/16.
 */
public class TicketDetail extends BasePage {
    public TicketDetail() {
        super("Ticket Detail");
    }

    public HTMLink reply(){
        return new HTMLink(By.linkText("Reply"), getPageName(), "Reply");
    }

    public HTMTextInput textArea(){
        return new HTMTextInput(By.id("tinymce"), getPageName(), "Text Area");
    }

    public HTMButton send(){
        return new HTMButton(By.xpath("//div[@class='btn-group send-button-group dropup']/button"), getPageName(), "Send");
    }
}
