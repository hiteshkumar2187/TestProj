package framework.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;


public class JavascriptUtil {

    /*
    * @dr - WebDriver to operate on
    * @postGet - Valid values are "POST" or "GET"
    * @url - url of server
    * @header - A property object which contains all header variable as name value pairs
    * @jsBody - wellformed js text
     */
    public static String runAsyncCall_deprecate(WebDriver dr, String postGet, String url,  Properties header, Properties jsBody) {

        String jsString = "var callback = arguments[arguments.length - 1];" +
                            "var xhr = new XMLHttpRequest();" ;
        jsString = jsString +   "xhr.open('"+postGet+"', '" + url + "', false);";
         /*Iterate header props add each property to header*/
        if(header != null){

            for(String key : header.stringPropertyNames()) {
                jsString = jsString + "xhr.setRequestHeader('" + key + "', '"+header.getProperty(key)+"');";
            }
        }else {
            Reporter.log("JS Header is null ");
        }

        jsString += "xhr.onreadystatechange = function() {" +
                "  if (xhr.readyState == 4) {" +
                "    callback(xhr.responseText);" +
                "  }" +
                "};";
        if (jsBody == null) {
            Reporter.log("JS Body was null.");
            jsString += "xhr.send();";
        }else{
            String myString ="";
            String sep ="";
            String encString ="";
            for(String key : jsBody.stringPropertyNames()) {
                try {
                    encString = URLEncoder.encode(jsBody.getProperty(key), "UTF-8");
                }catch (Exception e){
                    Reporter.log("JS Encoding failed ");
                    return ("");
                }
                myString = myString + sep+ key + "="+ encString;
                sep="&";
            }

            jsString += "    xhr.send('" + myString + "');";
        }
        Reporter.log("JS call "+jsString);
        Object retObj = ((JavascriptExecutor) dr).executeAsyncScript(jsString);
        return (String) retObj;
    }
    /*
    * @dr - WebDriver to operate on
    * @postGet - Valid values are "POST" or "GET"
    * @url - url of server
    * @header - An Arraylist object which contains all header variable as name value pairs
    * @jsBody - An Arraylist object which contains all jsbody variable as name value pairs
     */
    public static String runAsyncCallPairObject(WebDriver dr, String postGet, String url,
                                                ArrayList<Map.Entry<String,String>>  header,
                                                ArrayList<Map.Entry<String,String>> jsBody) {

        String jsString = "var callback = arguments[arguments.length - 1];" +
                "var xhr = new XMLHttpRequest();" ;
        jsString = jsString +   "xhr.open('"+postGet+"', '" + url + "', false);";
         /*Iterate header props add each property to header*/
        if(header != null){

            for(Map.Entry<String,String> headerElem: header) {
                jsString = jsString + "xhr.setRequestHeader('" + headerElem.getKey() + "', '"+headerElem.getValue()+"');";
            }
        }else {
            Reporter.log("JS Header is null ");
        }

        jsString += "xhr.onreadystatechange = function() {" +
                "  if (xhr.readyState == 4) {" +
                "    callback(xhr.responseText);" +
                "  }" +
                "};";
        if (jsBody == null) {
            Reporter.log("JS Body was null.");
            jsString += "xhr.send();";
        }else{
            String myString ="";
            String sep ="";
            String encString ="";


            for(Map.Entry<String,String> jsBodyElem : jsBody) {
                try {
                    encString = URLEncoder.encode(jsBodyElem.getValue(), "UTF-8");
                }catch (Exception e){
                    Reporter.log("JS Encoding failed ");
                    return ("");
                }
                myString = myString + sep+ jsBodyElem.getKey() + "="+ encString;
                sep="&";
            }

            jsString += "    xhr.send('" + myString + "');";
        }
        Reporter.log("JS call "+jsString);
        Object retObj = ((JavascriptExecutor) dr).executeAsyncScript(jsString);
        return (String) retObj;
    }
}