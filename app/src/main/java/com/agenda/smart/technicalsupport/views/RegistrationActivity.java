package com.agenda.smart.technicalsupport.views;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agenda.smart.technicalsupport.ChooseDialog;
import com.agenda.smart.technicalsupport.R;
import com.agenda.smart.technicalsupport.controllers.AppKeys;
import com.agenda.smart.technicalsupport.controllers.Service;
import com.agenda.smart.technicalsupport.models.Cities;
import com.agenda.smart.technicalsupport.models.CommerceType;
import com.agenda.smart.technicalsupport.models.ContactUs;
import com.agenda.smart.technicalsupport.models.GeneralResponse;
import com.agenda.smart.technicalsupport.models.UserProfile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener {
    EditText name, password, phone, email, address, userName, businessName;
    LinearLayout selectCityLinear, selectCommerceTypeLinear, selectCommerceLogo, newUser;

    ImageView registerLogo;
    SharedPreferences dataSaver;
    String api_token;
    ImageView addUser;
    TextView cityName, businessTypeName, textLogo, makeAccount, newUserText;
    int city_id, business_type_id;
    String city_name, business_type_name;
    String selectedImagePath = "";
    final int PICK_IMAGE = 123;
    boolean isCityDialog = false;
    boolean isBusinessType = false;
    private static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final int External_Permission_Request_code = 0505;
    private boolean mExternalPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        api_token = dataSaver.getString(AppKeys.TOKEN_KEY, "");
        name = findViewById(R.id.name);
        cityName = findViewById(R.id.city);
        registerLogo = findViewById(R.id.register_user_photo);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        newUser = findViewById(R.id.new_user);
        textLogo = findViewById(R.id.image);

        newUser = findViewById(R.id.new_user);
        address = findViewById(R.id.address);
        userName = findViewById(R.id.user_name);
        selectCityLinear = findViewById(R.id.city_linear);
        selectCommerceTypeLinear = findViewById(R.id.commerce_type_linear);
        selectCommerceLogo = findViewById(R.id.commerce_logo_linear);
        businessTypeName = findViewById(R.id.commerce_type);
        makeAccount = findViewById(R.id.make_account);
        businessName = findViewById(R.id.commerce_name);
        newUserText = findViewById(R.id.new_user_text);
        newUser.setOnClickListener(this);
        selectCityLinear.setOnClickListener(this);
        selectCommerceTypeLinear.setOnClickListener(this);
        selectCommerceLogo.setOnClickListener(this);
        getExternalPermission();

        if (!api_token.equals("")) {
            makeAccount.setText("تعديل البيانات");
            newUserText.setText("تعديل البيانات");
            getUserData();
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

            } else {
                ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
        }

    }

    public void openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    public static String getRealPath(Uri contentURI, Activity context) {
        // Intent data ;
        //    Log.e(TAG,"data "+data);

        String wholeID = DocumentsContract.getDocumentId(contentURI);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        @SuppressWarnings("deprecation")
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            String filePath = cursor.getString(columnIndex);
            return filePath;
        }
        cursor.close();


        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null && resultCode != RESULT_CANCELED) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                registerLogo.setImageBitmap(bitmap);
                registerLogo.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            selectedImagePath = getRealPath(uri, RegistrationActivity.this);
            Toast.makeText(RegistrationActivity.this, "تم اختيار الصورة", Toast.LENGTH_LONG).show();
            textLogo.setText("تم اختيار الصورة");

        }
    }


    @Override
    public void onClick(View view) {
        if (view == selectCommerceLogo) {
            openGallery();
        }

        if (view == selectCityLinear) {
            dataSaver.edit()
                    .putInt(AppKeys.CITY_ID, -1)
                    .apply();
            city_id = 0;
            city_name = "";
            isCityDialog = true;
            final ChooseDialog dialog = new ChooseDialog(this, "اختر المدينة");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getCities().enqueue(new Callback<Cities>() {
                @Override
                public void onResponse(Call<Cities> call, Response<Cities> response) {
                    if (response.isSuccessful()) {
                        Cities cities = response.body();
                        dialog.showCitiesList(cities.getCities());


                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cities> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (view == selectCommerceTypeLinear) {
            dataSaver.edit()
                    .putInt(AppKeys.BUSINESS_TYPE_ID, -1)
                    .apply();
            business_type_id = 0;
            business_type_name = "";
            isBusinessType = true;
            final ChooseDialog dialog = new ChooseDialog(this, "اختر نوع النشاط التجاري");
            dialog.setOnDismissListener(this);
            dialog.show();
            Service.Fetcher.getInstance().getBusinessType().enqueue(new Callback<CommerceType>() {
                @Override
                public void onResponse(Call<CommerceType> call, Response<CommerceType> response) {
                    if (response.isSuccessful()) {
                        CommerceType type = response.body();
                        dialog.showBusinessTypeList(type.getBusinessTypes());


                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, AppKeys.SOME_THING_WENT_WRONG_KEY, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommerceType> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (view == newUser) {
            if (api_token.equals("")) {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                File file = new File(selectedImagePath);
                Log.e("image path", "selectedImagePath " + selectedImagePath);
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("logo", file.getName(), imageFile);
                RequestBody nam = RequestBody.create(MediaType.parse("multipart/form-data"), name.getText().toString());
                RequestBody usernam = RequestBody.create(MediaType.parse("multipart/form-data"), userName.getText().toString());
                RequestBody businesName = RequestBody.create(MediaType.parse("multipart/form-data"), businessName.getText().toString());
                RequestBody phoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), phone.getText().toString());
                RequestBody usermail = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());
                RequestBody add = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), city_id + "");
                RequestBody businessid = RequestBody.create(MediaType.parse("multipart/form-data"), business_type_id + "");
                RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
                if (selectedImagePath != null || !selectedImagePath.equals("")) {

                    Service.Fetcher.getInstance().userRegister(imagePart, nam, phoneNumber, pass,
                            usermail, add, businessid, businesName, usernam, city).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegistrationActivity.this, ChooseActivity.class);
                                    startActivity(intent);


                                    dataSaver.edit()
                                            .putString(AppKeys.TOKEN_KEY, generalResponse.getApiToken())
                                            .apply();
                                    finish();
                                    Toast.makeText(RegistrationActivity.this, "تم إنشاء الحساب بنجاح", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message = "";
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "notSuccess", Toast.LENGTH_LONG).show();

                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                            //    Toast.makeText(RegistrationActivity.this, "error", Toast.LENGTH_LONG).show();

                        }
                    });


                }

                if (selectedImagePath == null || selectedImagePath.equals("")) {
                    imagePart = null;


                    Service.Fetcher.getInstance().userRegister(imagePart, nam, phoneNumber, pass,
                            usermail, add, businessid, businesName, usernam, city).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegistrationActivity.this, ChooseActivity.class);
                                    startActivity(intent);


                                    dataSaver.edit()
                                            .putString(AppKeys.TOKEN_KEY, generalResponse.getApiToken())
                                            .apply();
                                    finish();
                                    Toast.makeText(RegistrationActivity.this, "تم إنشاء الحساب بنجاح", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message = "";
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "notSuccess", Toast.LENGTH_LONG).show();

                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                            //     Toast.makeText(RegistrationActivity.this, "error", Toast.LENGTH_LONG).show();

                        }
                    });
                }

            } else {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("جاري التحميل...");
                progressDialog.show();
                File file = new File(selectedImagePath);
                Log.e("image path", "selectedImagePath " + selectedImagePath);
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("logo", file.getName(), imageFile);
                RequestBody nam = RequestBody.create(MediaType.parse("multipart/form-data"), name.getText().toString());
                RequestBody usernam = RequestBody.create(MediaType.parse("multipart/form-data"), userName.getText().toString());
                RequestBody businesName = RequestBody.create(MediaType.parse("multipart/form-data"), businessName.getText().toString());
                RequestBody phoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), phone.getText().toString());
                RequestBody usermail = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());
                RequestBody add = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), city_id + "");
                RequestBody businessid = RequestBody.create(MediaType.parse("multipart/form-data"), business_type_id + "");
                RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
                RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), api_token);

                if (selectedImagePath != null || !selectedImagePath.equals("")) {

                    Service.Fetcher.getInstance().updateUserData(imagePart, token, nam, phoneNumber, pass,
                            usermail, add, businessid, businesName, usernam, city).enqueue(new Callback<GeneralResponse>() {


                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();


                                    dataSaver.edit()
                                            .putString(AppKeys.TOKEN_KEY, generalResponse.getApiToken())
                                            .apply();
                                    finish();
                                    Toast.makeText(RegistrationActivity.this, "تم تعديل بياناتك بنجاح", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message = "";
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                            //  Toast.makeText(RegistrationActivity.this, "error", Toast.LENGTH_LONG).show();

                        }
                    });


                }

                if (selectedImagePath == null || selectedImagePath.equals("")) {
                    imagePart = null;


                    Service.Fetcher.getInstance().updateUserData(imagePart, token, nam, phoneNumber, pass,
                            usermail, add, businessid, businesName, usernam, city).enqueue(new Callback<GeneralResponse>() {

                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {


                            if (response.isSuccessful()) {

                                GeneralResponse generalResponse = response.body();
                                int status = generalResponse.getStatus();
                                if (status == 1) {
                                    Log.e("TAG", "isSuccessful ");

                                    progressDialog.dismiss();


                                    dataSaver.edit()
                                            .putString(AppKeys.TOKEN_KEY, generalResponse.getApiToken())
                                            .apply();
                                    finish();
                                    Toast.makeText(RegistrationActivity.this, "تم تعديل بياناتك بنجاح", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.e("TAG", "notSuccessful ");
                                    progressDialog.dismiss();
                                    String message = "";
                                    for (int i = 0; i < response.body().getMessages().size(); i++) {
                                        message = "";
                                        message += response.body().getMessages().get(i);
                                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure " + t.getMessage());

                            //   Toast.makeText(RegistrationActivity.this, "error", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }


        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (isCityDialog) {
            city_name = dataSaver.getString(AppKeys.CITY_NAME, "");
            city_id = dataSaver.getInt(AppKeys.CITY_ID, -1);
            if (city_id != -1) {
                cityName.setText(city_name);
            }

        }
        if (isBusinessType) {
            business_type_name = dataSaver.getString(AppKeys.BUSINESS_TYPE_NAME, "");
            business_type_id = dataSaver.getInt(AppKeys.BUSINESS_TYPE_ID, -1);
            if (business_type_id != -1) {
                businessTypeName.setText(business_type_name);
            }

        }
    }

    public void getUserData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Log.e("TAG", "isSuccessful");

        Service.Fetcher.getInstance().getProfile(api_token).enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    UserProfile profile = response.body();

                    name.setText(profile.getUser().getName());
                    Log.e("TAG", "profile.getUser().getEmail() " + profile.getUser().getEmail());
                    email.setText(profile.getUser().getEmail());
                    phone.setText(profile.getUser().getPhone());
                    userName.setText(profile.getUser().getUserName());
                    address.setText(profile.getUser().getAddress());
                    Log.e("TAG", "profile.getUser().getCity().getName() " + profile.getUser().getCity().getName());

                    cityName.setText(profile.getUser().getCity().getName());
                    businessName.setText(profile.getUser().getBusinessName());
                    businessTypeName.setText(profile.getUser().getBusiness().getName());

                    if (profile.getUser().getLogo() != null) {

                        Picasso.with(RegistrationActivity.this).load(String.valueOf(profile.getUser().getLogo())).into(registerLogo);
                        registerLogo.setVisibility(View.VISIBLE);
                    }
                    city_id = profile.getUser().getCity().getId();
                    business_type_id = profile.getUser().getBusinessId();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG ", "onFailure");
                Toast.makeText(RegistrationActivity.this, "حدث خطا", Toast.LENGTH_SHORT).show();
            }

        });

    }

}

