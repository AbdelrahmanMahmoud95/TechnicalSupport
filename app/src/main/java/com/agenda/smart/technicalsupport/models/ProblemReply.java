package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 9/4/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProblemReply {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issue")
    @Expose
    private Issue issue;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

}