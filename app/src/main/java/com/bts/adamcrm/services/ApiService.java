package com.bts.adamcrm.services;

import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.StockItem;
import com.bts.adamcrm.model.User;

import org.w3c.dom.Comment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    Call<User> logIn(@Field("data[email]") String email,@Field("data[password]") String password);

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
    Call <List<StockItem>> getItemList(@Path("type") int type,
                                       @Path("is_shop") int is_shop);
}