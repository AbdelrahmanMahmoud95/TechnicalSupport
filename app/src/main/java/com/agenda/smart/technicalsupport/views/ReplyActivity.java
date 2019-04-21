package com.agenda.smart.technicalsupport.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.Details;
import com.agenda.smart.technicalsupport.models.ProblemReply;
import com.agenda.smart.technicalsupport.models.UserProblems;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ReplyActivity extends AppCompatActivity {
    TextView problemDetails;
    VideoView problemVideo;
    SharedPreferences dataSaver;
    String api_token;
    int problem_id;
    ProblemReply problems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        problemDetails = findViewById(R.id.problem_details);
        problemVideo = findViewById(R.id.problem_video);
        Intent intent = getIntent();
        problem_id = intent.getIntExtra("problem_id", 0);
        getReply();
    }


    public void getReply() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getReply(problem_id, api_token).enqueue(new Callback<ProblemReply>() {

            @Override
            public void onResponse(Call<ProblemReply> call, Response<ProblemReply> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    problems = response.body();
                    String reply = problems.getIssue().getReply().getDetails();
                    if (reply != null) {
                        problemDetails.setText(reply);

                    }

                    MediaController mc = new MediaController(ReplyActivity.this);
                    problemVideo.setMediaController(mc);
                    String path = problems.getIssue().getReply().getVideo();
                    if (path != null) {
                        Uri uri = Uri.parse(path);
                        problemVideo.setVideoURI(uri);
                        problemVideo.start();
                    }


                }
            }

            @Override
            public void onFailure(Call<ProblemReply> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(ReplyActivity.this, "حدث خطا", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
