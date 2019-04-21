package com.agenda.smart.technicalsupport.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.BottomNavigationViewHelper;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.GeneralResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.support.v4.graphics.TypefaceCompatUtil.getTempFile;

public class AddProblemActivity extends BaseActivity implements View.OnClickListener {
    ImageView problemImage;
    LinearLayout openCamera;
    LinearLayout addProblem;
    EditText problemDetails;
    SharedPreferences dataSaver;
    String api_token;
    Bitmap capture;
    String _path;
    String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    String imageOut = outputDir + File.separator;
    Uri photoURI;
    File file;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    ContentValues values;


    private static final int CAMERA_IMAGE = 345;
    private static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;

    private static final int External_Permission_Request_code = 0505;
    private boolean mExternalPermissionGranted = false;
    String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        problemDetails = findViewById(R.id.problem_details);
        openCamera = findViewById(R.id.add_photo_linear);
        problemImage = findViewById(R.id.problem_image);
        addProblem = findViewById(R.id.add_problem);
        openCamera.setOnClickListener(this);
        addProblem.setOnClickListener(this);
        getExternalPermission();
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_add_problem;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_chatting;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.agenda.smart.technicalsupport",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void getExternalPermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mExternalPermissionGranted = true;

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mExternalPermissionGranted = true;

                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    mExternalPermissionGranted = true;

                } else {
                    ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
                }
            } else {
                ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            problemImage.setImageURI(Uri.fromFile(imgFile));
            problemImage.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onClick(View view) {
        if (view == openCamera) {

            dispatchTakePictureIntent();
        }

        if (view == addProblem) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري تحميل المشكلة...");
            progressDialog.show();
            progressDialog.setCancelable(false);

// memory limit in server file max size
            if (mCurrentPhotoPath == null) {
                Log.e("TAG", "mCurrentPhotoPath " + mCurrentPhotoPath);
                progressDialog.dismiss();
                Toast.makeText(AddProblemActivity.this, "برجاء اختيار الصورة", Toast.LENGTH_LONG).show();

            } else {
                Log.e("TAG", "mCurrentPhotoPath " + mCurrentPhotoPath);
                File file = new File(mCurrentPhotoPath);
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageFile);
                RequestBody apiToken = RequestBody.create(MediaType.parse("multipart/form-data"), api_token);
                RequestBody details = RequestBody.create(MediaType.parse("multipart/form-data"), problemDetails.getText().toString());


                Service.Fetcher.getInstance().postProblem(imagePart, apiToken, details).enqueue(new Callback<GeneralResponse>() {

                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.isSuccessful()) {

                            GeneralResponse generalResponse = response.body();
                            int status = generalResponse.getStatus();
                            if (status == 1) {
                                Log.e("TAG", "isSuccessful ");
                                problemDetails.setText("");
                                problemImage.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Toast.makeText(AddProblemActivity.this, "تم اضافة المشكلة بنجاح", Toast.LENGTH_LONG).show();


                            } else {
                                progressDialog.dismiss();
                                String message = "";
                                for (int i = 0; i < response.body().getMessages().size(); i++) {
                                    message += response.body().getMessages().get(i);
                                    Toast.makeText(AddProblemActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            Log.e("TAG", "notSuccessful ");
                            Toast.makeText(AddProblemActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                        }
                    }


                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                        progressDialog.dismiss();
                        Log.e("TAG", "onFailure " + t.getMessage());

                        Toast.makeText(AddProblemActivity.this, "حدث خطأ", Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }

    public void logOut(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AddProblemActivity.this, LoginActivity.class);
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
