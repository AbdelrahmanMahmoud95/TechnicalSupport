package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 8/12/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("problem")
    @Expose
    private ProblemDetails problem;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProblemDetails getProblem() {
        return problem;
    }

    public void setProblem(ProblemDetails problem) {
        this.problem = problem;
    }

}