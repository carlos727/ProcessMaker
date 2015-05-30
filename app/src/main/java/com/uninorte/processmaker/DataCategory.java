package com.uninorte.processmaker;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataCategory implements Serializable {

    private String group_id;
    private String name;

    public DataCategory() {

    }

    public DataCategory(String group_id, String name) {
        this.group_id = group_id;
        this.name = name;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}