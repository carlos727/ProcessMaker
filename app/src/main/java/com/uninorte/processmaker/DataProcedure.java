package com.uninorte.processmaker;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataProcedure implements Serializable {

    private String procedure_id;
    private String group_id;
    private String name;
    private String description;

    public DataProcedure() {

    }

    public DataProcedure(String procedure_id, String group_id, String name, String description) {
        this.procedure_id = procedure_id;
        this.group_id = group_id;
        this.name = name;
        this.description = description;
    }

    public void setProcedure_id(String procedure_id) {
        this.procedure_id = procedure_id;
    }

    public String getProcedure_id() {
        return procedure_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
