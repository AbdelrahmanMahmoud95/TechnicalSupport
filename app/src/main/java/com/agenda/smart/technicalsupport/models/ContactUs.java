package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 9/3/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUs {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("setting")
    @Expose
    private Setting setting;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

}