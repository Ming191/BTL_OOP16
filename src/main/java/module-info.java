module org.library.btl_oop16_library {
    requires org.xerial.sqlitejdbc;
    requires org.json;
    requires java.net.http;
    requires jdk.compiler;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.apache.poi.ooxml;
    requires atlantafx.base;
    requires jakarta.mail;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;
    requires MaterialFX;
    requires eu.iamgio.animated;

    opens org.library.btl_oop16_library.U_Controllers to javafx.fxml;
    exports org.library.btl_oop16_library.U_Controllers;
    opens org.library.btl_oop16_library to javafx.fxml;
    exports org.library.btl_oop16_library;
    exports org.library.btl_oop16_library.Controller;
    opens org.library.btl_oop16_library.Controller to javafx.fxml;
    exports org.library.btl_oop16_library.Model;
    opens org.library.btl_oop16_library.Model to javafx.fxml;
    exports org.library.btl_oop16_library.Util;
    opens org.library.btl_oop16_library.Util to javafx.fxml;
    exports org.library.btl_oop16_library.Services;
    opens org.library.btl_oop16_library.Services to javafx.fxml;
}