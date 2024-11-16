package org.library.btl_oop16_library.Model;

public class Account {
    private int id;
    private String userName;
    private String password;
    private boolean isAdmin = false;

    public Account(String userName, String password, Boolean isAdmin) {
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Account(int id,String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
