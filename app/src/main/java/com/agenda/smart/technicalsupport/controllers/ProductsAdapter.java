package com.agenda.smart.technicalsupport.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.models.Datum2;
import com.agenda.smart.technicalsupport.models.Product;
import com.agenda.smart.technicalsupport.models.Products;
import com.agenda.smart.technicalsupport.views.ProductDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Abdelrahman on 8/13/2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    Context context;
    Products products;

    public ProductsAdapter(Context context, Products products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        Picasso.with(context).load(String.valueOf(products.getProducts().getData().get(position).getImage())).into(holder.itemImage);
        holder.itemName.setText( products.getProducts().getData().get(position).getTitle());
        holder.itemPrice.setText(String.valueOf(products.getProducts().getData().get(position).getCost()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int product_id = products.getProducts().getData().get(position).getId();
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id", product_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.getProducts().getData().size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice;
        Button productDetails;
        RelativeLayout layout;

        public ProductViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            layout = itemView.findViewById(R.id.layout);

        }
    }

    public void addInRecycle(List<Datum2> datum) {
        for (Datum2 dt : datum) {
            products.getProducts().getData().add(dt);
        }
        notifyDataSetChanged();
    }

}
