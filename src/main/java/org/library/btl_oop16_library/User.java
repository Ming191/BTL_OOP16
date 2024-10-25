package org.library.btl_oop16_library;

public class User {
    private int id;
    private String name;
    private String email;
    private String account;
    private String password;
    private boolean isAdmin;

    User(String account, String email) {
        this.account = account;
        this.email = email;
    }

    User(int id, String name, String account, String password, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.account = account;
        this.password = password;
        this.isAdmin = false;
    }

    User(int id, String name, String email, String account, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.account = account;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    User(String name, String account, String password, String email) {
        this.id = -1;
        this.name = name;
        this.email = email;
        this.account = account;
        this.password = password;
        this.isAdmin = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
}
