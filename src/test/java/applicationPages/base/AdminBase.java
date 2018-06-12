package applicationPages.base;

import applicationPages.common.Footer;
import applicationPages.common.Header;
import framework.base.BasePage;

public abstract class AdminBase extends BasePage {

    private Header header;
    private Footer footer;

    protected AdminBase(String pageName) {
        super(pageName);
        header = new Header(pageName);
        footer = new Footer(pageName);
    }

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

}
