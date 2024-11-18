package org.library.btl_oop16_library.Model;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class BookInfoCellFactory implements Callback<ListView<Book>, ListCell<Book>> {

    @Override
    public ListCell<Book> call(ListView<Book> param) {
        return new BookInfoCell();
    }
}
