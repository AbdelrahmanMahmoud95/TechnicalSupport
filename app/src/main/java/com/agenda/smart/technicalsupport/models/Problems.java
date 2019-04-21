package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 8/12/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Problems {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("problems")
    @Expose
    private Problem problems;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Problem getProblems() {
        return problems;
    }

    public void setProblems(Problem problems) {
        this.problems = problems;
    }

}