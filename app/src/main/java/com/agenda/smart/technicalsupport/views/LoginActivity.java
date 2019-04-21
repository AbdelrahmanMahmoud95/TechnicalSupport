package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.GeneralResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    LinearLayout newUserLinear;
    TextView forgetPasswordLinear;
    EditText userName, password;
    SharedPreferences dataSaver;
    String api_token;
    String user_name;
    String new_password;
    String verification_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        newUserLinear = findViewById(R.id.new_user);
        forgetPasswordLinear = findViewById(R.id.forget_password);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        login.setOnClickListener(this);
        forgetPasswordLinear.setOnClickListener(this);
        newUserLinear.setOnClickListener(this);

        if (!api_token.equals("")) {
            Intent intent = new Intent(LoginActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {

        if (view == newUserLinear) {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
        if (view == login) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري التحميل...");
            progressDialog.show();
            String phone = userName.getText().toString();
            String pass = password.getText().toString();
            Service.Fetcher.getInstance().userLogin(phone, pass).enqueue(new Callback<GeneralResponse>() {

                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (response.isSuccessful()) {

                        GeneralResponse generalResponse = response.body();
                        int status = generalResponse.getStatus();

                        if (status == 1) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

                            Log.e("TAG", "isSuccessful ");
                            progressDialog.dismiss();
                            Toast.makeText(getApplication(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ChooseActivity.class);
                            startActivity(intent);
                            finish();


                            dataSaver.edit()
                                    .putString(AppKeys.TOKEN_KEY, generalResponse.getApiToken())
                                    .apply();

                        } else {
                            Log.e("TAG", "notSuccessful ");
                            progressDialog.dismiss();
                            String message = "";
                            for (int i = 0; i < response.body().getMessages().size(); i++) {
                                message = "";
                                message += response.body().getMessages().get(i);
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Log.e("TAG", "notSuccessful ");

                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    Log.e("TAG", "onFailure " + t.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

                }
            });

        }
        if (view == forgetPasswordLinear) {

            final android.app.AlertDialog builder = new android.app.AlertDialog.Builder(this).create();
            final View v = LayoutInflater.from(this).inflate(R.layout.order_dialog, null);
            final EditText userName = v.findViewById(R.id.order_details);
            userName.setHint("ادخل اسم المستخدم");
            Log.e("TAG", "user_name " + user_name);
            Button submit = v.findViewById(R.id.submit);
            submit.setText("تم");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user_name = userName.getText().toString();
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("جاري التحميل...");
                    progressDialog.show();
                    Log.e("TAG", "user_name " + user_name);
                    Service.Fetcher.getInstance().resetPassword(user_name).enqueue(new Callback<GeneralResponse>() {

                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();

                                if (status == 1) {
                                    builder.dismiss();
                                    Log.e("TAG", "isSuccessful ");
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "تم ارسال رقم التحقق على البريد الالكتروني", Toast.LENGTH_SHORT).show();
                                    verificationNumber();
                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message = "";
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Log.e("TAG", "notSuccessful ");
                                Toast.makeText(LoginActivity.this, "notSuccessful", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            Log.e("TAG", "onFailure " + t.getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();


                        }
                    });
                }
            });
            builder.setView(v);
            // dialog.setCancelable(false);
            builder.show();
        }
    }

    public void newPassword() {
        final android.app.AlertDialog builder = new android.app.AlertDialog.Builder(this).create();

        final View v = LayoutInflater.from(this).inflate(R.layout.order_dialog, null);
        final EditText newPassword = v.findViewById(R.id.order_details);
        newPassword.setHint("ادخل اكتب كلمة السر الجديده");

        Button submit = v.findViewById(R.id.submit);
        submit.setText("تم");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_password = newPassword.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();

                Service.Fetcher.getInstance().newPassword(verification_number, new_password).enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();

                            if (status == 1) {
                                builder.dismiss();
                                Log.e("TAG", "isSuccessful ");
                                progressDialog.dismiss();
                                Toast.makeText(getApplication(), "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, ChooseActivity.class);
                                startActivity(intent);
                                finish();


                                dataSaver.edit()
                                        .putString(AppKeys.TOKEN_KEY, generalResponse.getApiToken())
                                        .apply();
                            } else {
                                Log.e("TAG", "notSuccessful ");
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message = "";
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            Log.e("TAG", "notSuccessful ");
                            Toast.makeText(LoginActivity.this, "notSuccessful", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        Log.e("TAG", "onFailure " + t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();


                    }
                });
            }
        });
        builder.setView(v);
        // dialog.setCancelable(false);
        builder.show();
    }

    public void verificationNumber() {
        final android.app.AlertDialog builder = new android.app.AlertDialog.Builder(this).create();

        final View v = LayoutInflater.from(this).inflate(R.layout.order_dialog, null);
        final EditText verificationNumber = v.findViewById(R.id.order_details);
        verificationNumber.setHint("ادخل كود التحقق");

        Button submit = v.findViewById(R.id.submit);
        submit.setText("تم");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verification_number = verificationNumber.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();

                Service.Fetcher.getInstance().verifyCode(verification_number).enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();

                            if (status == 1) {
                                builder.dismiss();
                                Log.e("TAG", "isSuccessful ");
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "الكود صحيح", Toast.LENGTH_SHORT).show();
                                newPassword();
                            } else {
                                Log.e("TAG", "notSuccessful ");
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message = "";
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            Log.e("TAG", "notSuccessful ");
                            Toast.makeText(LoginActivity.this, "notSuccessful", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        Log.e("TAG", "onFailure " + t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();


                    }
                });
            }
        });
        builder.setView(v);
        // dialog.setCancelable(false);
        builder.show();
    }

//    @Override
//    public void onBackPressed() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//
//        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        final AlertDialog dialog = builder.create();
//
//        dialog.setTitle("إغلاق التطبيق");
//        dialog.setMessage("هل انت متأكد انك تريد الخروج من التطبيق؟");
//
//        dialog.show();
//    }
}
