package com.agenda.smart.technicalsupport.controllers;

import com.agenda.smart.technicalsupport.models.Cities;
import com.agenda.smart.technicalsupport.models.CommerceType;
import com.agenda.smart.technicalsupport.models.Details;
import com.agenda.smart.technicalsupport.models.Details2;
import com.agenda.smart.technicalsupport.models.ExpandModel;
import com.agenda.smart.technicalsupport.models.GeneralResponse;
import com.agenda.smart.technicalsupport.models.ProblemReply;
import com.agenda.smart.technicalsupport.models.Problems;
import com.agenda.smart.technicalsupport.models.Products;
import com.agenda.smart.technicalsupport.models.UserOrders;
import com.agenda.smart.technicalsupport.models.UserProblems;
import com.agenda.smart.technicalsupport.models.UserProfile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Abdelrahman on 8/9/2018.
 */

public interface Service {

    @FormUrlEncoded
    @POST("users/login")
    Call<GeneralResponse> userLogin(@Field("user_name") String phone,
                                    @Field("password") String password);

    @Multipart
    @POST("users/register")
    Call<GeneralResponse> userRegister(@Part MultipartBody.Part logo,
                                       @Part("name") RequestBody name,
                                       @Part("phone") RequestBody phone,
                                       @Part("password") RequestBody password,
                                       @Part("email") RequestBody email,
                                       @Part("address") RequestBody address,
                                       @Part("business_id") RequestBody business_id,
                                       @Part("business_name") RequestBody business_name,
                                       @Part("user_name") RequestBody user_name,
                                       @Part("city_id") RequestBody city_id);

    @Multipart
    @POST("users/profile")
    Call<GeneralResponse> updateUserData(@Part MultipartBody.Part logo,
                                         @Part("api_token") RequestBody api_token,
                                         @Part("name") RequestBody name,
                                         @Part("phone") RequestBody phone,
                                         @Part("password") RequestBody password,
                                         @Part("email") RequestBody email,
                                         @Part("address") RequestBody address,
                                         @Part("business_id") RequestBody business_id,
                                         @Part("business_name") RequestBody business_name,
                                         @Part("user_name") RequestBody user_name,
                                         @Part("city_id") RequestBody city_id);

    @Multipart
    @POST("users/problem/store")
    Call<GeneralResponse> postProblem(@Part MultipartBody.Part image,
                                      @Part("api_token") RequestBody api_token,
                                      @Part("details") RequestBody details);

    @FormUrlEncoded
    @POST("users/product/{id}/order")
    Call<GeneralResponse> postOrder(@Path("id") int id, @Field("api_token") String api_token,
                                    @Field("details") String details
    );

    @FormUrlEncoded
    @POST("users/password-reset/mail")
    Call<GeneralResponse> resetPassword(@Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("users/password-reset/verify-code")
    Call<GeneralResponse> verifyCode(@Field("code") String code);

    @FormUrlEncoded
    @POST("users/password-reset/reset")
    Call<GeneralResponse> newPassword(@Field("code") String code,
                                      @Field("password") String password);

    @GET("users/clients")
    Call<ExpandModel> getClients(@Query("api_token") String api_token);

    @GET("users/problems")
    Call<Problems> getProblems(@Query("api_token") String api_token, @Query("page") int page);

    @GET("users/products")
    Call<Products> getProducts(@Query("api_token") String api_token, @Query("page") int page);

    @GET("users/problem/{id}")
    Call<Details> getProblemsDetails(@Path("id") int id, @Query("api_token") String api_token);

    @GET("users/product/{id}")
    Call<Details2> getProductDetails(@Path("id") int id, @Query("api_token") String api_token);

    @GET("users/profile")
    Call<UserProfile> getProfile(@Query("api_token") String api_token);

    @GET("users/orders")
    Call<UserOrders> getUserOrders(@Query("api_token") String api_token);

    @GET("users/issues")
    Call<UserProblems> getUserProblems(@Query("api_token") String api_token);

    @GET("users/issue/{id}")
    Call<ProblemReply> getReply(@Path("id") int id, @Query("api_token") String api_token);

    @GET("cities")
    Call<Cities> getCities();

    @GET("business-types")
    Call<CommerceType> getBusinessType();

    String BASE_URL = "http://support-smartagenda.com/api/";

    class Fetcher {
        private static Service service = null;

        public static Service getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(Service.class);
            }
            return service;
        }
    }
}
