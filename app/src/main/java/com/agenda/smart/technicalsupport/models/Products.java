package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 8/13/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Products {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("products")
    @Expose
    private Product products;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getProducts() {
        return products;
    }

    public void setProducts(Product products) {
        this.products = products;
    }

}