package utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "LoginData")
    public String[][] getDataLogin() throws IOException {

        String path = ".\\testData\\Opencart_LoginData.xlsx";

        ExcelUtility xlutil = new ExcelUtility(path);

        int lastRow = xlutil.getRowCount("Sheet1"); // last row index
        int totalCols = xlutil.getCellCount("Sheet1", 0);

        List<String[]> dataList = new ArrayList<>();

        for (int i = 1; i <= lastRow; i++) {

            String username = xlutil.getCellData("Sheet1", i, 0);
            String password = xlutil.getCellData("Sheet1", i, 1);
            String result   = xlutil.getCellData("Sheet1", i, 2);

        
            if ((username == null || username.trim().isEmpty()) &&
                (password == null || password.trim().isEmpty()) &&
                (result == null || result.trim().isEmpty())) {
                continue;
            }

      
            dataList.add(new String[]{
                username == null ? "" : username,
                password == null ? "" : password,
                result   == null ? "" : result
            });
        }

        // Convert List → 2D Array
        String[][] dataArr = new String[dataList.size()][totalCols];

        for (int i = 0; i < dataList.size(); i++) {
            dataArr[i] = dataList.get(i);
        }

        System.out.println("✅ Total rows fetched from Excel: " + dataArr.length);

        return dataArr;
    }
}
