package com.uninorte.processmaker;

import org.json.JSONObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataStep implements Serializable {

    private String procedure_id;
    private String step_number;
    private String content;

    public DataStep() {

    }

    public DataStep(String procedure_id, String step_number, String content) {
        this.procedure_id = procedure_id;
        this.step_number = step_number;
        this.content = content;
    }

    public void setProcedure_id(String procedure_id) {
        this.procedure_id = procedure_id;
    }

    public String getProcedure_id() {
        return procedure_id;
    }

    public void setStep_number(String step_number) {
        this.step_number = step_number;
    }

    public String getStep_number() {
        return step_number;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
