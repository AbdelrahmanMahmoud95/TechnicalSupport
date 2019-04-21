package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.controllers.UserOrdersAdapter;
import com.agenda.smart.technicalsupport.models.UserOrders;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class UserOrdersActivity extends BaseActivity {
    UserOrdersAdapter userOrdersAdapter;
    RecyclerView orderRecyclerView;
    UserOrders orders;
    GridLayoutManager layoutManager;
    SharedPreferences dataSaver;
    TextView needHelp;
    LinearLayout addProblem;
    String api_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        orderRecyclerView = findViewById(R.id.recycler_orders);
        needHelp = findViewById(R.id.need_help);
        addProblem = findViewById(R.id.add_problem);
        addProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserOrdersActivity.this,ProductsActivity.class);
                startActivity(intent);
            }
        });
        dataSaver = getDefaultSharedPreferences(UserOrdersActivity.this);
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        layoutManager = new GridLayoutManager(this, 1);
        orderRecyclerView.setLayoutManager(layoutManager);
        getOrders();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_user_orders;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_products;
    }

    public void getOrders() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");
        Service.Fetcher.getInstance().getUserOrders(api_token).enqueue(new Callback<UserOrders>() {

            @Override
            public void onResponse(Call<UserOrders> call, Response<UserOrders> response) {
                Log.e("TAG", "api_token " + api_token);

                if (response.isSuccessful()) {
                    Log.e("TAG", "isSuccessful");
                    progressDialog.dismiss();

                    orders = response.body();
                  if (orders.getOrders().size()==1) {
                      needHelp.setVisibility(View.GONE);
                      addProblem.setVisibility(View.GONE);
                  }
                    Log.e("TAG", "orders " + orders);

                    userOrdersAdapter = new UserOrdersAdapter(UserOrdersActivity.this, orders);
                    orderRecyclerView.setAdapter(userOrdersAdapter);

                } else {
                    progressDialog.dismiss();
                    Log.e("TAG", "notSuccessful");

                }
            }

            @Override
            public void onFailure(Call<UserOrders> call, Throwable t) {
                Log.e("TAG ", "onFailure");
                progressDialog.dismiss();
                Toast.makeText(UserOrdersActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
