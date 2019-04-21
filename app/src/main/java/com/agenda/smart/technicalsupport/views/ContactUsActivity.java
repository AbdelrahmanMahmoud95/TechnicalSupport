package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.ContactUs;
import com.agenda.smart.technicalsupport.models.Details2;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ContactUsActivity extends BaseActivity {
    TextView phoneNumber, email;
    SharedPreferences dataSaver;
    String api_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
      //  phoneNumber = findViewById(R.id.phone_number);
//        email = findViewById(R.id.email);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
       // getContact();

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_contact_us;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_contact_us;
    }
//
//    public void getContact() {
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("جاري التحميل...");
//        progressDialog.show();
//        Log.e("TAG", "isSuccessful");
//
//        Service.Fetcher.getInstance().getContact(api_token).enqueue(new Callback<ContactUs>() {
//
//            @Override
//            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
//                if (response.isSuccessful()) {
//                    progressDialog.dismiss();
//                    ContactUs contactUs = response.body();
//
//                    phoneNumber.setText(" رقم الهاتف " + contactUs.getSetting().getPhone().toString());
//                    email.setText(" البريد الالكتروني " + contactUs.getSetting().getEmail());
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ContactUs> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.e("TAG ", "onFailure");
//                Toast.makeText(ContactUsActivity.this, "حدث خطا", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//    }
}
