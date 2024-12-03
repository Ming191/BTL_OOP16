package org.library.btl_oop16_library.model;


public class User {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String userName;
    private String password;
    private String role;

    public User() {}

    public User(int id,
                String name,
                String email,
                String phone,
                String address,
                String userName,
                String password,
                String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phone;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public User(String name,
                String email,
                String phone,
                String address,
                String userName,
                String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phone;
        this.address = address;
        this.userName = userName;
        this.password = password;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
