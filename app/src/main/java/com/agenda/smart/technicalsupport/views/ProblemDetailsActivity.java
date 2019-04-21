package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.Details;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ProblemDetailsActivity extends BaseActivity {
    TextView problemName;
    TextView problemDetails;
    VideoView problemVideo;
    SharedPreferences dataSaver;
    LinearLayout addProblem;
    String api_token;
    int problem_id;
    SimpleRatingBar ratingBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_details);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        ratingBar = findViewById(R.id.post_rating);
        ratingBar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ProblemDetailsActivity.this, "شكرا لتقيمك", Toast.LENGTH_LONG).show();
            }
        });
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        problemName = findViewById(R.id.problem_name);
        addProblem = findViewById(R.id.add_problem);
        addProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProblemDetailsActivity.this, AddProblemActivity.class);
                startActivity(intent);
            }
        });
        problemVideo = findViewById(R.id.problem_video);
        Intent intent = getIntent();
        problem_id = intent.getIntExtra("problem_id", 0);
        getProblemDetails();

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_problem_details;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_help;
    }

    public void getProblemDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getProblemsDetails(problem_id, api_token).enqueue(new Callback<Details>() {

            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Details details = response.body();

                    problemName.setText(details.getProblem().getTitle());
                    MediaController mc = new MediaController(ProblemDetailsActivity.this);
                    problemVideo.setMediaController(mc);
                    String path = details.getProblem().getVideo();
                    Uri uri = Uri.parse(path);
                    problemVideo.setVideoURI(uri);
                    problemVideo.start();

                }
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(ProblemDetailsActivity.this, "حدث خطا", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void logOut(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ProblemDetailsActivity.this, LoginActivity.class);
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
