package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 9/4/2018.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProblems {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issues")
    @Expose
    private List<Issue> issues = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

}