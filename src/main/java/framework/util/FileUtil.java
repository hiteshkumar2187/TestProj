package framework.util;

import org.testng.Reporter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;

public class FileUtil {

    public static final String UNIX_FILE_SEPARATOR = "/";
    public static final String WINDOWS_FILE_SEPARATOR = "\\";

    public static String changeSeparatorIfRequired(String path) {

        String changedPath = path;

        if (File.separator.equals(WINDOWS_FILE_SEPARATOR)) {
            changedPath = path.replaceAll(UNIX_FILE_SEPARATOR, Matcher.quoteReplacement(WINDOWS_FILE_SEPARATOR));
        }

        return changedPath;

    }

    public static File getResourceAsFile(String resourceName) {
        return new File(Thread.currentThread().getContextClassLoader().getResource(stripLeadingSlash(resourceName)).getFile().replaceAll("%20"," "));
    }

    private static String stripLeadingSlash(String resourceName) {
        return (resourceName.startsWith("/")) ? resourceName.substring(1) : resourceName;
    }


    /**
     * Copies a file to the provided destination.
     * @param srcFilePath
     * @param destFilePath
     *
     */
    public static void copyFile(String srcFilePath, String destFilePath){
        Reporter.log("<b>Copy file</b><br>");
        Reporter.log("Src file => " + srcFilePath + ", Dest file => " + destFilePath);
        try {
            Files.copy(Paths.get(getAbsoluteFilePath(srcFilePath)), Paths.get(getAbsoluteFilePath(destFilePath)), StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a file's absolute path
     * @param filePath relative file path
     * @return absolute file path
     *
     */
    public static String getAbsoluteFilePath(String filePath){
        filePath = filePath.replace('/', File.separatorChar);
        File dst = new File("");
        String targetAbsolutePath = dst.getAbsolutePath();
        String absolutePath = null;
        if(filePath.startsWith(targetAbsolutePath)){
            absolutePath = filePath;
        }else{
            File file = new File("");
            absolutePath = file.getAbsolutePath()+ filePath;
        }
        return absolutePath;
    }
}