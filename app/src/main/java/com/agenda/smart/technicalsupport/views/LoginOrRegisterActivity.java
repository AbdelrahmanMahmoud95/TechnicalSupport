package com.agenda.smart.technicalsupport.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout login, newUser;
    SharedPreferences dataSaver;
    String api_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        login = findViewById(R.id.login);
        newUser = findViewById(R.id.new_user);
        login.setOnClickListener(this);
        newUser.setOnClickListener(this);
        if (api_token != "") {
            Intent intent = new Intent(LoginOrRegisterActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == login) {

            Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
            startActivity(intent);


        }

        if (view == newUser) {
            Intent intent = new Intent(LoginOrRegisterActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    }
}
