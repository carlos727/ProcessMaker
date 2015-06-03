package com.uninorte.processmaker;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataField implements Serializable {

    private String id;
    private String caption;
    private int type;
    private String[] possible_values;

    public DataField() {

    }

    public DataField(String id, String caption, int type, String[] possible_values) {
        this.id = id;
        this.caption = caption;
        this.type = type;
        this.possible_values = possible_values;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setPossible_values(String[] possible_values) {
        this.possible_values = possible_values;
    }

    public String[] getPossible_values() {
        return possible_values;
    }
}
