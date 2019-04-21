package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.ExpandAdapter;
import com.agenda.smart.technicalsupport.controllers.OurCustomersAdapter;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.ExpandModel;
import com.agenda.smart.technicalsupport.models.SliderImage;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class OurCustomersActivity extends AppCompatActivity {
    ExpandAdapter expandAdapter;
    ExpandModel expandModel;
    ExpandableListView expandableListView;
    SharedPreferences dataSaver;
    String api_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_customers);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        expandableListView =  findViewById(R.id.expand_list);

        getClients();
    }

    public void getClients() {
        Service.Fetcher.getInstance().getClients(api_token).enqueue(new Callback<ExpandModel>() {
            @Override
            public void onResponse(Call<ExpandModel> call, Response<ExpandModel> response) {
                if (response.isSuccessful()) {
                    expandModel = response.body();
                    expandAdapter = new ExpandAdapter(OurCustomersActivity.this, expandModel);
                    expandableListView.setAdapter(expandAdapter);
                    expandAdapter.getGroupCount();
                    for (int i = 0; i < 1; i++) {
                        expandableListView.expandGroup(i);
                    }

                } else {

                    Toast.makeText(OurCustomersActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExpandModel> call, Throwable t) {

            }
        });


    }
}
