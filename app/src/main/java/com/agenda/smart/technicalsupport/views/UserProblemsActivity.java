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
import com.agenda.smart.technicalsupport.controllers.UserProblemsAdapter;
import com.agenda.smart.technicalsupport.models.UserOrders;
import com.agenda.smart.technicalsupport.models.UserProblems;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class UserProblemsActivity extends BaseActivity {
    UserProblemsAdapter userProblemsAdapter;
    RecyclerView problemRecyclerView;
    UserProblems problems;
    GridLayoutManager layoutManager;
    SharedPreferences dataSaver;
    TextView needHelp;
    LinearLayout addProblem;
    String api_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_problems);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        needHelp = findViewById(R.id.need_help);
        addProblem = findViewById(R.id.add_problem);
        addProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProblemsActivity.this, AddProblemActivity.class);
                startActivity(intent);
            }
        });
        problemRecyclerView = findViewById(R.id.recycler_problems);
        dataSaver = getDefaultSharedPreferences(UserProblemsActivity.this);
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        layoutManager = new GridLayoutManager(this, 1);
        problemRecyclerView.setLayoutManager(layoutManager);
        getProblems();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_user_problems;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_chatting;
    }

    public void getProblems() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");
        Service.Fetcher.getInstance().getUserProblems(api_token).enqueue(new Callback<UserProblems>() {

            @Override
            public void onResponse(Call<UserProblems> call, Response<UserProblems> response) {
                Log.e("TAG", "api_token " + api_token);

                if (response.isSuccessful()) {
                    Log.e("TAG", "isSuccessful");
                    progressDialog.dismiss();
                    problems = response.body();

                    if (problems.getIssues().size() >= 1) {
                        needHelp.setVisibility(View.GONE);
                        //addProblem.setVisibility(View.GONE);
                    }
                    userProblemsAdapter = new UserProblemsAdapter(UserProblemsActivity.this, problems);
                    problemRecyclerView.setAdapter(userProblemsAdapter);
                } else {
                    progressDialog.dismiss();
                    Log.e("TAG", "notSuccessful");

                }
            }

            @Override
            public void onFailure(Call<UserProblems> call, Throwable t) {
                Log.e("TAG ", "onFailure");
                progressDialog.dismiss();
                Toast.makeText(UserProblemsActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
