package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 9/5/2018.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommerceType {

    @SerializedName("business_types")
    @Expose
    private List<BusinessType> businessTypes = null;

    public List<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<BusinessType> businessTypes) {
        this.businessTypes = businessTypes;
    }

}
