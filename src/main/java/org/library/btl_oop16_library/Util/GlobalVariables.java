package org.library.btl_oop16_library.Util;

public class GlobalVariables {
    public static boolean isLightTheme = true;
    public static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String LOADING_IMG = String.valueOf(GlobalVariables.class.getResource("/img/loading.gif"));
    public static final String DEFAULT_IMG = String.valueOf(GlobalVariables.class.getResource("/img/defBookCover.png"));
    public static final String LIGHT_THEME = String.valueOf(GlobalVariables.class.getResource("/css/nord-light.css"));
    public static final String DARK_THEME = String.valueOf(GlobalVariables.class.getResource("/css/nord-dark.css"));
    public static final String MAINMENU_PATH = "/org/library/btl_oop16_library/view/MainMenu.fxml";
}
