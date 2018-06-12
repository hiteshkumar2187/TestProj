package framework.util;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {

    private static PropertyUtil prop;
    private Properties properties;

    private PropertyUtil(){
        properties = new Properties();
    }

    public static synchronized PropertyUtil getInstance() {
        if(prop == null){
            prop = new PropertyUtil();
        }
        return prop;
    }

    public void load(String fileName) throws IOException{
        InputStream input = null;
        input = getClass().getClassLoader().getResourceAsStream(fileName);
        properties.load(input);
    }

    public void load(File file) throws IOException{
        InputStream input = new FileInputStream(file);
        properties.load(input);
    }

    public void clear() {
        properties.clear();
    }

    public String getValue(String key) {
        return properties.getProperty(key).trim();
    }

    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }
}
