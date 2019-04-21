package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.ProductsAdapter;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.Products;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ProductsActivity extends BaseActivity {
    ProductsAdapter productsAdapter;
    RecyclerView productRecyclerView;
    Products products;
    GridLayoutManager layoutManager;
    SharedPreferences dataSaver;
    String api_token;
    int pageNumber = 1;
    boolean isLoading = true;
    int pastVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    int viewThreshold = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        dataSaver = getDefaultSharedPreferences(ProductsActivity.this);
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        productRecyclerView = findViewById(R.id.product_recycler);
        layoutManager = new GridLayoutManager(this, 1);
        productRecyclerView.setLayoutManager(layoutManager);
        getProducts();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_products;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_products;
    }

    public void getProducts() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "pageNumber " + pageNumber);

        Service.Fetcher.getInstance().getProducts(api_token, pageNumber).enqueue(new Callback<Products>() {

            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Log.e("TAG", "api_token " + api_token);

                if (response.isSuccessful()) {
                    Log.e("TAG", "isSuccessful");

                    progressDialog.dismiss();
                    products = response.body();
                    productsAdapter = new ProductsAdapter(ProductsActivity.this, products);
                    productRecyclerView.setAdapter(productsAdapter);

                } else {
                    Log.e("TAG", "notSuccessful");

                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
            }

        });
        Log.e("TAG", "productRecyclerView.addOnScrollListener");
        productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Log.e("TAG", "dy " + dy);

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItem + viewThreshold)) {
                        Log.e("TAG", "performPagination()");

                        pageNumber++;
                        performPagination();
                        isLoading = true;
                        Log.e("TAG", "pageNumber " + pageNumber);

                    }
                }
            }
        });
    }

    public void performPagination() {
        //progressBar.setVisibility(View.VISIBLE);
        Service.Fetcher.getInstance().getProducts(api_token, pageNumber).enqueue(new Callback<Products>() {

            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Log.e("TAG", "api_token " + api_token);

                if (response.isSuccessful()) {
                    Log.e("TAG", "isSuccessful");

                    products = response.body();
                    productsAdapter.addInRecycle(products.getProducts().getData());

                } else {
                    Log.e("TAG", "notSuccessful");

                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Log.e("TAG ", "onFailure");
            }

        });
    }

    public void logOut(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ProductsActivity.this, LoginActivity.class);
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
        final AlertDialog dialog = builder.create();

        dialog.setTitle("تسجيل الخروج");
        dialog.setMessage("هل انت متأكد انك تريد تسجيل الخروج؟");

        dialog.show();
    }


}
