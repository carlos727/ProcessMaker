package com.uninorte.processmaker;

import org.json.JSONArray;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Branch implements Serializable {

    private JSONArray branch;
    private String go_to_step;

    public Branch() {

    }

    public Branch(JSONArray branch, String go_to_step) {
        this.branch = branch;
        this.go_to_step = go_to_step;
    }

    public void setBranch(JSONArray branch) {
        this.branch = branch;
    }

    public JSONArray getBranch() {
        return branch;
    }

    public void setGo_to_step(String go_to_step) {
        this.go_to_step = go_to_step;
    }

    public String getGo_to_step() {
        return go_to_step;
    }
}

