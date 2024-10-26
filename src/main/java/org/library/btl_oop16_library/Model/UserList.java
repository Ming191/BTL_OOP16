package org.library.btl_oop16_library.Model;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    public List<User> users;

    public UserList() {
        users = new ArrayList<User>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
