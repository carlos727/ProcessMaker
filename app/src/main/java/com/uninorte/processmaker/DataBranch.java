package com.uninorte.processmaker;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataBranch implements Serializable {

    private int field_id;
    private String type;
    private String value;

    public DataBranch() {

    }

    public DataBranch(int field_id, String type, String value) {
        this.field_id = field_id;
        this.type = type;
        this.value = value;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getField_id() {
        return field_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
