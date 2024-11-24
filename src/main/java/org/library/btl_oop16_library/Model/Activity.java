package org.library.btl_oop16_library.Model;

import java.sql.Timestamp;

public class Activity {
    private int id;
    private String description;
    private Timestamp timestamp;

    public Activity(int id, String description, Timestamp timestamp) {
        this.id = id;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
