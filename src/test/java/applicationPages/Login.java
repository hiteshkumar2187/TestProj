package applicationPages;

import framework.base.BasePage;
import framework.element.HTMButton;
import framework.element.HTMTextInput;
import org.openqa.selenium.By;

/**
 * Created by ctpl00694 on 8/6/16.
 */
public class Login extends BasePage {

    public Login() {
        super("Login");
    }

    public HTMTextInput txtEmail(){
        return new HTMTextInput(By.id("inputEmail3"), getPageName(), "Email");
    }

    public HTMTextInput txtPassword  (){
        return new HTMTextInput(By.id("inputPassword3"), getPageName(), "Password");
    }

    public HTMButton btnLogin(){
        return new HTMButton(By.id("signbtn"), getPageName(), "Sign in");
    }

    public void login(String userName, String password){
        txtEmail().waitUntilEditable();
        txtEmail().type(userName);
        txtPassword().type(password);
        btnLogin().click();
        pause(10);
    }

}
