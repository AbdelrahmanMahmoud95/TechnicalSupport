package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.UserOrders;

/**
 * Created by Abdelrahman on 9/3/2018.
 */

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder> {
    Context context;
    UserOrders orders;

    public UserOrdersAdapter(Context context, UserOrders orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders, parent, false);
        UserOrdersAdapter.UserOrderViewHolder userOrderViewHolder = new UserOrdersAdapter.UserOrderViewHolder(view);
        return userOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position) {
        holder.orderName.setText(orders.getOrders().get(position).getProduct().getTitle());
        holder.orderDate.setText(orders.getOrders().get(position).getCreatedAt());
        holder.orderStatus.setText(orders.getOrders().get(position).getStatusName());
        holder.orderNumber.setText(orders.getOrders().get(position).getOrderNumber());

    }

    @Override
    public int getItemCount() {
        return orders.getOrders().size();
    }

    public class UserOrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderDate, orderStatus, orderNumber;


        public UserOrderViewHolder(View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            orderName = itemView.findViewById(R.id.order_name);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderNumber = itemView.findViewById(R.id.order_number);

        }
    }
}
