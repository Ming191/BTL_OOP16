module org.library.btl_oop16_library {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires org.json;
    requires java.net.http;
    requires jdk.compiler;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.apache.poi.ooxml;
    requires jakarta.mail;


    opens org.library.btl_oop16_library to javafx.fxml;
    exports org.library.btl_oop16_library;
    exports org.library.btl_oop16_library.Controller;
    opens org.library.btl_oop16_library.Controller to javafx.fxml;
    exports org.library.btl_oop16_library.Model;
    opens org.library.btl_oop16_library.Model to javafx.fxml;
    exports org.library.btl_oop16_library.Util;
    opens org.library.btl_oop16_library.Util to javafx.fxml;
}