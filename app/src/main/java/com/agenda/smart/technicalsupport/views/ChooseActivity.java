package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.OurCustomersAdapter;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.SliderImage;
import com.agenda.smart.technicalsupport.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ChooseActivity extends BaseActivity implements View.OnClickListener {

    TextView userData, productLayout, userProblemsLayout, logout, name, ourCustomer;
    SharedPreferences dataSaver;
    ImageView exit;
    String api_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        name = findViewById(R.id.user_name);
        ourCustomer = findViewById(R.id.our_customer);
        logout = findViewById(R.id.logout);
        exit = findViewById(R.id.exit);
        productLayout = findViewById(R.id.product_layout);
        userProblemsLayout = findViewById(R.id.user_problem);
        userData = findViewById(R.id.edit_data);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        productLayout.setOnClickListener(this);
        userData.setOnClickListener(this);
        logout.setOnClickListener(this);
        userProblemsLayout.setOnClickListener(this);
        ourCustomer.setOnClickListener(this);
        exit.setOnClickListener(this);
        getUserName();
        ImageView img = (ImageView)findViewById(R.id.image);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://info.support-smartagenda.com/"));
                startActivity(intent);
            }


        });
    }




    public void getUserName() {
        Service.Fetcher.getInstance().getProfile(api_token).enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {

                    UserProfile profile = response.body();

                    name.setText(profile.getUser().getName());

                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e("TAG ", "onFailure");
            }

        });

    }


    @Override
    int getContentViewId() {
        return R.layout.activity_choose;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    public void logOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ChooseActivity.this, LoginActivity.class);
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


    @Override
    public void onClick(View view) {


        if (view == productLayout) {
            Intent intent = new Intent(ChooseActivity.this, ProductsActivity.class);
            startActivity(intent);
        }


        if (view == userData) {
            Intent intent = new Intent(ChooseActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }


        if (view == userProblemsLayout) {
            Intent intent = new Intent(ChooseActivity.this, UserProblemsActivity.class);
            startActivity(intent);
        }
        if (view == ourCustomer) {
            Intent intent = new Intent(ChooseActivity.this, OurCustomersActivity.class);
            startActivity(intent);
        }

        if (view == exit) {
            logOut();
        }

        if (view == logout) {
            Intent intent = new Intent(ChooseActivity.this, ContactUsActivity.class);
            startActivity(intent);
        }
    }
}
