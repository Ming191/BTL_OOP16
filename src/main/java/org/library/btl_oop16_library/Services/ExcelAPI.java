package org.library.btl_oop16_library.Services;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.BookDBConnector;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;
import org.library.btl_oop16_library.Util.DBConnector;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.io.File;
import java.sql.SQLException;

public class ExcelAPI {
    public static void importExcel(DBConnector db) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(excelFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            db.importFromExcel(filePath);
        } else {
            System.out.println("No file selected.");
        }
    }
}
