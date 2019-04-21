package com.agenda.smart.technicalsupport.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.Details;
import com.agenda.smart.technicalsupport.models.Details2;
import com.agenda.smart.technicalsupport.models.GeneralResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {
    TextView productName, productDetails, productCost;
    ImageView productImage;
    Button order;
    SharedPreferences dataSaver;
    String api_token;
    int product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        productName = findViewById(R.id.item_name);
        productDetails = findViewById(R.id.item_details);
        productCost = findViewById(R.id.item_price);
        productImage = findViewById(R.id.item_image);
        order = findViewById(R.id.order);
        Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id", 0);
        order.setOnClickListener(this);
        getProductDetails();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_problem_details;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_products;
    }

    public void getProductDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getProductDetails(product_id, api_token).enqueue(new Callback<Details2>() {

            @Override
            public void onResponse(Call<Details2> call, Response<Details2> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    final Details2 details = response.body();

                    productName.setText(details.getProduct().getTitle());
                    productDetails.setText(" الوصف: " + details.getProduct().getDetails());
                    productCost.setText(String.valueOf(details.getProduct().getCost()));
                    Picasso.with(ProductDetailsActivity.this).load(String.valueOf(details.getProduct()
                            .getImage())).into(productImage);
                    productImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String imagePath = details.getProduct().getImage();
                            Intent intent = new Intent(ProductDetailsActivity.this, ProductImageActivity.class);
                            intent.putExtra("imagePath", imagePath);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Details2> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(ProductDetailsActivity.this, "حدث خطا", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onClick(View view) {
        if (view == order) {
            final AlertDialog builder = new AlertDialog.Builder(this).create();

            final View v = LayoutInflater.from(this).inflate(R.layout.order_dialog, null);

            Button submit = v.findViewById(R.id.submit);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText details = v.findViewById(R.id.order_details);

                    String orderDetails = details.getText().toString();

                    final ProgressDialog progressDialog = new ProgressDialog(ProductDetailsActivity.this);
                    progressDialog.setMessage("جاري الطلب...");
                    progressDialog.show();

                    Service.Fetcher.getInstance().postOrder(product_id, api_token, orderDetails).enqueue(new Callback<GeneralResponse>() {

                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();

                                if (status == 1) {
                                    builder.dismiss();
                                    Log.e("TAG", "isSuccessful ");
                                    progressDialog.dismiss();
                                    Toast.makeText(ProductDetailsActivity.this, "تم ارسال الطلب", Toast.LENGTH_SHORT).show();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message = "";
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Log.e("TAG", "notSuccessful ");
                                Toast.makeText(ProductDetailsActivity.this, "notSuccessful", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            Log.e("TAG", "onFailure " + t.getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();


                        }
                    });
                }
            });
            builder.setView(v);
            // dialog.setCancelable(false);
            builder.show();
        }
    }


    public void logOut(View view) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                dataSaver.edit()
                        .putString(AppKeys.TOKEN_KEY, "")
                        .apply();
            }
        });

        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final android.support.v7.app.AlertDialog dialog = builder.create();

        dialog.setTitle("تسجيل الخروج");
        dialog.setMessage("هل انت متأكد انك تريد تسجيل الخروج؟");

        dialog.show();
    }


}


