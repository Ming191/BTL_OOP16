module org.library.btl_oop16_library {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.library.btl_oop16_library to javafx.fxml;
    exports org.library.btl_oop16_library;
}