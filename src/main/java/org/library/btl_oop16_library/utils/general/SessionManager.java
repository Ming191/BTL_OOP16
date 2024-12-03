package org.library.btl_oop16_library.utils.general;

import org.library.btl_oop16_library.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void clearSession() {
        this.currentUser = null;
    }

}

