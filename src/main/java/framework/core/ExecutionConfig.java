package framework.core;

import framework.util.FileUtil;
import framework.util.PropertyUtil;
import java.io.File;

public class ExecutionConfig {

    public static final String BROWSER;
    public static final int WAIT_TIME;
    public static final String BASE_URL;
    public static final String DB_URL;
    public static final String DB_USERNAME;
    public static final String DB_NAME;
    public static final String DB_PASSWORD;
    public static final String DB_DECRYPTION_KEY;
    public static final String TEST_DATA_PATH;
    public static final String LOGIN_PASSWORD;
    public static final String TEMP_DATA_PATH;
    public static final String MAX_RETRY_COUNT;
    public static final String SMTP_HOSTNAME;
    public static final String SMTP_PORT;
    public static final String SMTP_USERNAME;
    public static final String SMTP_PASSWORD;

    static{
        try {
            PropertyUtil.getInstance().load(new File("config.properties"));
            BROWSER = PropertyUtil.getInstance().getValue("BROWSER");
            WAIT_TIME = Integer.parseInt(PropertyUtil.getInstance().getValue("WAIT_TIME"));
            BASE_URL = PropertyUtil.getInstance().getValue("HOSTNAME");
            DB_NAME = System.getProperty("dbName");
            DB_USERNAME = System.getProperty("dbUserName");
            DB_PASSWORD = System.getProperty("dbPassword");
            DB_URL = "jdbc:mysql://" + System.getProperty("dbHost") + "/" + DB_NAME;
            DB_DECRYPTION_KEY = System.getProperty("dbDecryptionKey");
            LOGIN_PASSWORD = System.getProperty("loginPassword");
            TEST_DATA_PATH = PropertyUtil.getInstance().getValue("TEST_DATA_PATH");
            TEMP_DATA_PATH = FileUtil.changeSeparatorIfRequired(System.getProperty("tempDataDirectory"));
            MAX_RETRY_COUNT = PropertyUtil.getInstance().getValue("RETRY_COUNT");

            SMTP_HOSTNAME = PropertyUtil.getInstance().getValue("SMTP_HOSTNAME");
            SMTP_PORT = PropertyUtil.getInstance().getValue("SMTP_PORT");
            SMTP_USERNAME = PropertyUtil.getInstance().getValue("SMTP_USERNAME");
            SMTP_PASSWORD = PropertyUtil.getInstance().getValue("SMTP_PASSWORD");

            File file = new File(TEMP_DATA_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            System.setProperty("org.uncommons.reportng.escape-output", "false");
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Something wrong !!! Check configurations.", e);
        }
    }
}