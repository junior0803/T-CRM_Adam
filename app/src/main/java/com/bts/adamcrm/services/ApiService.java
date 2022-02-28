package com.bts.adamcrm.services;

import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.StockItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Log in
    @FormUrlEncoded
    @POST("auth/register")
    Call<ResponseBody> signUp(@Field("data[email]") String email,
                      @Field("data[password]") String password,
                      @Field("data[confirm_password]") String confirmPassword);

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> logIn(@Field("data[email]") String email,@Field("data[password]") String password);

    // Category Management
    @GET("api/categoryList")
    Call <List<Category>> categoryList();

    @FormUrlEncoded
    @POST("insertCategory")
    Call <ResponseBody> createCategory(@Field("data[name]") String name);

    @FormUrlEncoded
    @POST("updateCategory")
    Call <ResponseBody> updateItem(@Field("id") String id, @Field("data[name]") String name);

    @GET("deleteCategory/{id}")
    Call <ResponseBody> deleteCategory(@Path("id") String id);

    // Stock Part Management
    @FormUrlEncoded
    @POST("insertParts")
    Call <ResponseBody> createPart(@Field("data[q]") String quantity, @Field("data[mq]") String min_quantity,
                                   @Field("data[description]") String des, @Field("data[pno]") String pno,
                                   @Field("data[is_shopping]") int code,  @Field("data[type]") int type);

    @FormUrlEncoded
    @POST("updateParts")
    Call <ResponseBody> updatePart(@Field("id") int id, @Field("data[q]") String quantity, @Field("data[mq]") String min_quantity,
                                   @Field("data[description]") String des, @Field("data[pno]") String pno,
                                   @Field("data[is_shopping]") int code,  @Field("data[type]") int type);

    @GET("/deleteParts/{id}")
    Call <ResponseBody> deletePart(@Path("id") int id);

    @GET("/customer/attach_file")
    Call <ResponseBody> uploadCustomerAttach();

    @GET("/api/partsList/{type}/{is_shop}")
    Call <List<StockItem>> getPartItemList(@Path("type") int type,
                                           @Path("is_shop") int is_shop);

    // Customer
    @POST("customerList")
    Call <List<Customer>> getAllCustomerList();

    @FormUrlEncoded
    @POST("customerList")
    Call <List<Customer>> getCustomerList(@Field("title") String title, @Field("state") int state, @Field("category") int category
            , @Field("date_from") String date_from, @Field("date_to") String date_to);

    @FormUrlEncoded
    @POST("insertCustomer")
    Call <ResponseBody> insertCustomer(@Field("data[title]") String title, @Field("data[mobile_phone]") String mobile,
                                       @Field("data[email]") String email, @Field("data[name]") String name, @Field("data[address]") String address,
                                       @Field("data[town]") String town, @Field("data[postal_code]") String postal, @Field("data[further_note]") String further_note,
                                       @Field("data[state]") int state, @Field("data[remind_date]") String remind, @Field("data[category_id]") int category,
                                       @Field("data[sms_sent]") int sms_sent, @Field("data[attached_files]") String attached_files);

    @FormUrlEncoded
    @POST("updateCustomer/{id}")
    Call <ResponseBody> updateCustomer(@Path("id")int id, @Field("data[title]") String title, @Field("data[mobile_phone]") String mobile,
                                       @Field("data[email]") String email, @Field("data[name]") String name, @Field("data[address]") String address,
                                       @Field("data[town]") String town, @Field("data[postal_code]") String postal, @Field("data[further_note]") String further_note,
                                       @Field("data[state]") int state, @Field("data[remind_date]") String remind, @Field("data[category_id]") int category,
                                       @Field("data[sms_sent]") int sms_sent, @Field("data[attached_files]") String attached_files);
}