package com.agenda.smart.technicalsupport.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.agenda.smart.technicalsupport.R;
import com.squareup.picasso.Picasso;

public class ProductImageActivity extends AppCompatActivity {
    String imagePath;
    ImageView productImage;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        productImage = findViewById(R.id.product_image);
        Picasso.with(ProductImageActivity.this).load(imagePath).into(productImage);
    }
}
