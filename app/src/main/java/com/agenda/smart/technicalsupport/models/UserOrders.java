package com.agenda.smart.technicalsupport.models;

/**
 * Created by Abdelrahman on 9/3/2018.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrders {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}