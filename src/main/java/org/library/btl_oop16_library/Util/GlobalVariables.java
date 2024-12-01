package org.library.btl_oop16_library.Util;

public class GlobalVariables {
    public static boolean isLightTheme = true;
    public static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String LOADING_IMG = String.valueOf(GlobalVariables.class.getResource("/img/loading.gif"));
    public static final String DEFAULT_IMG = String.valueOf(GlobalVariables.class.getResource("/img/defBookCover.png"));
    public static final String LIGHT_THEME = String.valueOf(GlobalVariables.class.getResource("/css/nord-light.css"));
    public static final String DARK_THEME = String.valueOf(GlobalVariables.class.getResource("/css/nord-dark.css"));
    public static final String MAINMENU_PATH = "/org/library/btl_oop16_library/view/MainMenu.fxml";
    public static final String SETTINGS_PATH = "/org/library/btl_oop16_library/view/Settings.fxml";
    public static final String CHANGE_PASSWORD_PATH = "/org/library/btl_oop16_library/view/ChangePasswordView.fxml";
    public static final String UPDATE_INFO_PATH = "/org/library/btl_oop16_library/view/UpdateInforForUser.fxml";
    public static final String COMMENT_ITEM_PATH = "/org/library/btl_oop16_library/view/CommentItem.fxml";
    public static final String COMMENT_BOX_PATH = "/org/library/btl_oop16_library/view/CommentBox.fxml";
    public static final String ICON_PATH = "/img/logo.png";
    public static final String PREORDER_DIALOG = "/org/library/btl_oop16_library/view/PreorderDialog.fxml";
}
