package com.agenda.smart.technicalsupport.models;

import java.util.List;

import com.agenda.smart.technicalsupport.models.Slider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderImage {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("clients")
    @Expose
    private List<Slider> clients = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Slider> getClients() {
        return clients;
    }

    public void setClients(List<Slider> clients) {
        this.clients = clients;
    }

}