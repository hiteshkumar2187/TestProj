package framework.util;

import framework.annotation.DataProviderParams;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DataProviderUtil {

    private static Map<String, String> resolveDataProviderParams(Method testMethod) {
        if (testMethod == null)
            throw new IllegalArgumentException("Test Method context cannot be null.");

        DataProviderParams args = testMethod.getAnnotation(DataProviderParams.class);
        if (args == null)
            throw new IllegalArgumentException("Test Method context has no DataProviderParams annotation.");
        if (args.value() == null || args.value().length == 0)
            throw new IllegalArgumentException("Test Method context has a malformed DataProviderParams annotation.");
        Map<String, String> arguments = new HashMap<String, String>();
        for (int i = 0; i < args.value().length; i++)
        {
            String[] parts = args.value()[i].split("=");
            arguments.put(parts[0].trim(), parts[1].trim());
        }
        return arguments;
    }

    @DataProvider(name="ExcelDataProvider")
    public static synchronized Object[][] excelDataProvider(Method testMethod) {
        Map<String, String> arguments = resolveDataProviderParams(testMethod);
        Object[][] retObjArr = ReadExcelTableUtil.getInstance().getTableData(arguments.get("fileName"), arguments
                .get("sheetName"), arguments.get("tableName"));

        if(retObjArr == null){
            throw new RuntimeException("Data HTMTable is either malformed or empty [Workbook | Sheet | HTMTable] => [" +
                    arguments.get("fileName") + " | " + arguments.get("sheetName") + " | " +
                    arguments.get("tableName") + "]");
        }

        return(retObjArr);
    }

    @DataProvider(name="ExcelDataProviderTranspose")
    public static synchronized Object[][] excelDataProviderTranspose(Method testMethod) {
        Object[][] retObjArr = excelDataProvider(testMethod);
        Object[][] transposeObjArray = new Object[retObjArr[0].length][retObjArr.length];
        for(int row = 0; row < retObjArr[0].length; row++){
            for(int col = 0; col < retObjArr.length; col++){
                transposeObjArray[row][col] = retObjArr[col][row];
            }
        }
        return(transposeObjArray);
    }
}
