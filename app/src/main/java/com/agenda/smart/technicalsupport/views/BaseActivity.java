package com.agenda.smart.technicalsupport.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.agenda.smart.technicalsupport.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Abdelrahman on 9/23/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    protected BottomNavigationView navigationView;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        updateNavigationBarState1();


    }

    @Override
    protected void onStart() {
        super.onStart();

        updateNavigationBarState1();

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navigation_home:
                startActivity(new Intent(this, ChooseActivity.class));
                break;
            case R.id.navigation_chatting:
                startActivity(new Intent(this, UserProblemsActivity.class));

                break;
            case R.id.navigation_products:
                startActivity(new Intent(this, UserOrdersActivity.class));

                break;

            case R.id.navigation_help:
                startActivity(new Intent(this, ProblemsActivity.class));

                break;

            case R.id.navigation_contact_us:
                startActivity(new Intent(this, ContactUsActivity.class));

                break;


            default:
                return false;
        }
        finish();
        return true;
    }


    private void updateNavigationBarState1() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem1(actionId);
    }

    void selectBottomNavigationBarItem1(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.icon:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();


}