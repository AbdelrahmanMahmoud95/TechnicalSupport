package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.Category;
import com.agenda.smart.technicalsupport.models.Client;
import com.agenda.smart.technicalsupport.models.ExpandModel;
import com.agenda.smart.technicalsupport.models.UserOrders;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Abdelrahman on 10/10/2018.
 */

public class ClientRecyclerAdapter extends RecyclerView.Adapter<ClientRecyclerAdapter.ClientRecyclerViewHolder> {

    Context context;
    List<Client> client;

    public ClientRecyclerAdapter(Context context, List<Client> client) {
        this.context = context;
        this.client = client;
    }

    @NonNull
    @Override
    public ClientRecyclerAdapter.ClientRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false);
        ClientRecyclerAdapter.ClientRecyclerViewHolder clientRecyclerViewHolder = new ClientRecyclerAdapter.ClientRecyclerViewHolder(view);
        return clientRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientRecyclerAdapter.ClientRecyclerViewHolder holder, int position) {
        Picasso.with(context).load(String.valueOf(client.get(position).getImage())).into(holder.clientImages);

    }

    @Override
    public int getItemCount() {
        return client.size();
    }

    public class ClientRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView clientImages;

        public ClientRecyclerViewHolder(View itemView) {
            super(itemView);
            clientImages = itemView.findViewById(R.id.client_images);

        }
    }
}
