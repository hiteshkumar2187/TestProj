package framework.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import framework.core.ExecutionConfig;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.RandomStringUtils;

public class ReadExcelTableUtil {

    private static ReadExcelTableUtil excelUtil;

    private ReadExcelTableUtil(){
    }

    public static synchronized ReadExcelTableUtil getInstance() {
        if(excelUtil == null){
            excelUtil = new ReadExcelTableUtil();
        }
        return excelUtil;
    }

    public synchronized String[][] getTableData(String fileName, String sheetName, String tableName){
        String filePath = new File("").getAbsolutePath() + File.separator + ExecutionConfig.TEST_DATA_PATH +
                File.separator + fileName;

        Workbook workbook = null;
        Sheet sheet;
        String[][] tabArray=null;

        try {
            workbook = Workbook.getWorkbook(new File(filePath));
        } catch (IOException | BiffException e) {
            throw new RuntimeException(e);
        }

        sheet = workbook.getSheet(sheetName);
        if(sheet == null) {
            throw new RuntimeException("Worksheet not found: [Workbook | Sheet] => [" + fileName + " | " +
                    sheetName + "]");
        }

        int startRow,startCol, endRow, endCol, ci, cj;

        Cell tableStart = sheet.findCell(tableName);
        if(tableStart == null) {
            throw new RuntimeException("Data HTMTable not found: [Workbook | Sheet | HTMTable] => [" + fileName + " | " +
                    sheetName + " | " + tableName + "]");
        }

        startRow = tableStart.getRow();
        startCol = tableStart.getColumn();

        Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1, 100, 64000,  false);
        if(tableEnd == null) {
            throw new RuntimeException("Data HTMTable end marker not found: [Workbook | Sheet | HTMTable] => [" + fileName +
                    " | " + sheetName + " | " + tableName + "]");
        }

        endRow = tableEnd.getRow();
        endCol = tableEnd.getColumn();

        if((endRow - startRow) < 2 || (endCol - startCol) < 2){
            return null;
        }

        tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
        ci = 0;

        for (int i = startRow + 1; i < endRow; i++, ci++){
            cj = 0;
            for (int j = startCol + 1; j < endCol; j++, cj++){
                tabArray[ci][cj] = getFinalString(sheet.getCell(j,i).getContents());
            }
        }
        return(tabArray);
    }

    private String getFinalString(String inputString){

        String []preDefinedPatterns = {"TIMESTAMP", "RANDOM-A\\([0-9]+\\)", "RANDOM-C\\([0-9]+\\)", "RANDOM-N\\([0-9]+\\)", "MSG\\(.+?\\)"};
        for(int j = 0; j < preDefinedPatterns.length; j++){
            Pattern pattern = Pattern.compile(preDefinedPatterns[j], Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(inputString);
            String s3 = inputString;
            while(m.find()){
                String s1 = m.group();
                String s2 = null;
                int i = 0;

                if(pattern.toString().toUpperCase().contains("TIMESTAMP")){
                    s2 = new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
                    s3 = m.replaceAll(s2);
                }else if(pattern.toString().toUpperCase().contains("RANDOM-A")){
                    i = Integer.parseInt(s1.replaceAll("[\\D]", ""));
                    s2 = RandomStringUtils.randomAlphanumeric(i).toLowerCase();
                    s3 = m.replaceFirst(s2);
                }else if(pattern.toString().toUpperCase().contains("RANDOM-C")){
                    i = Integer.parseInt(s1.replaceAll("[\\D]", ""));
                    s2 = RandomStringUtils.randomAlphabetic(i).toLowerCase();
                    s3 = m.replaceFirst(s2);
                }else if(pattern.toString().toUpperCase().contains("RANDOM-N")){
                    i = Integer.parseInt(s1.replaceAll("[\\D]", ""));
                    s2 = RandomStringUtils.randomNumeric(i);
                    s3 = m.replaceFirst(s2);
                }else if(pattern.toString().toUpperCase().contains("MSG")){
                    s2 = s1.replaceFirst("MSG\\(", "").replaceFirst("\\)", "");
                    Properties messages = CommonTestUtil.loadPropertiesFile("messages.properties");
                    String message = messages.getProperty(s2);
                    s3 = m.replaceFirst(message);
                }
                m=pattern.matcher(s3);
            }
            inputString = s3;
        }
        return inputString;
    }
}