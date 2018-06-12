package applicationPages;

import framework.base.BasePage;
import framework.element.HTMButton;
import framework.element.HTMTextInput;
import org.openqa.selenium.By;

/**
 * Created by ctpl00694 on 30/6/16.
 */
public class Controls extends BasePage {
    public Controls() {
        super("Controls");
    }

    public HTMTextInput emailID(){
        return new HTMTextInput(By.id("username"), getPageName(), "Email ID");
    }

    public HTMButton go(){
        return new HTMButton(By.cssSelector(".right_nav.small_btn"), getPageName(), "Go");
    }


}
