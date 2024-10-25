package org.library.btl_oop16_library;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    List<User> users;

    UserList() {
        users = new ArrayList<User>();
    }

    public void viewUsers() {
        for (User user : users) {
            System.out.println(user.getName());
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
