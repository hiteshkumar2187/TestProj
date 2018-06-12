package applicationPages.common;

import framework.base.BasePage;
import framework.element.HTMLink;
import org.openqa.selenium.By;

public class Header extends BasePage{

    public Header(String pageName) {
        super(pageName);
    }


    /**
     * Navigates to menu items by clicking on all the provided links in the specified order
     * @param menuLinks array of link texts (menu links to click) as seen on the UI
     *
     */
    public void navigateTo(String ...menuLinks){
        for(String link: menuLinks){
            HTMLink menuElement = new HTMLink(By.linkText(link), getPageName(), link + " Menu HTMLink");
            menuElement.waitUntilClickable();
            menuElement.click();
        }
        waitUntilAllAJAXCallsFinish();
    }
}
