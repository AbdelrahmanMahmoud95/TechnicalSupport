package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 8/13/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details2 {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("product")
    @Expose
    private ProductDetails product;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductDetails getProduct() {
        return product;
    }

    public void setProduct(ProductDetails product) {
        this.product = product;
    }

}
