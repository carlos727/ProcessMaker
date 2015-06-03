package com.uninorte.processmaker;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataBranch implements Serializable {

    private String field_id;
    private String type;
    private String value;
    private String go_to_step;

    public DataBranch() {

    }

    public DataBranch(String field_id, String type, String value, String go_to_step) {
        this.field_id = field_id;
        this.type = type;
        this.value = value;
        this.go_to_step = go_to_step;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getField_id() {
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

    public void setGo_to_step(String go_to_step) {
        this.go_to_step = go_to_step;
    }

    public String getGo_to_step() {
        return go_to_step;
    }
}
