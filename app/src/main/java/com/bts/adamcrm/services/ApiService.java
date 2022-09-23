package com.bts.adamcrm.services;

import com.bts.adamcrm.model.Attachment;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.model.Customer;
import com.bts.adamcrm.model.Invoice;
import com.bts.adamcrm.model.StockItem;
import com.google.android.gms.common.internal.HideFirstParty;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
                                   @Field("data[type]") int code, @Field("data[is_shopping]") int type);

    @FormUrlEncoded
    @POST("updateParts")
    Call <ResponseBody> updatePart(@Field("id") int id, @Field("data[q]") String quantity, @Field("data[mq]") String min_quantity,
                                   @Field("data[description]") String des, @Field("data[pno]") String pno,
                                   @Field("data[type]") int code,  @Field("data[is_shopping]") int type);

    @GET("/deleteParts/{id}")
    Call <ResponseBody> deletePart(@Path("id") int id);

    @GET("/api/partsList/")
    Call <List<StockItem>> getAllPartItemList();

    @GET("/api/partsList/{type}/{is_shop}")
    Call <List<StockItem>> getPartItemList(@Path("is_shop") int is_shop, @Path("type") int type);

    // Customer
    @GET("customerList")
    Call <List<Customer>> getAllCustomerList();

//    @FormUrlEncoded
//    @POST("customerList")
//    Call <List<Customer>> getCustomerList(@Field("title") String title, @Field("state") int state, @Field("category") int category
//            , @Field("date_from") String date_from, @Field("date_to") String date_to);

    @FormUrlEncoded
    @POST("insertCustomer")
    Call <String> insertCustomer(@Field("data[title]") String title, @Field("data[mobile_phone]") String mobile,
                                       @Field("data[email]") String email, @Field("data[name]") String name, @Field("data[address]") String address,
                                       @Field("data[town]") String town, @Field("data[postal_code]") String postal, @Field("data[further_note]") String further_note,
                                       @Field("data[state]") int state, @Field("data[remind_date]") String remind, @Field("data[category_id]") int category,
                                       @Field("data[sms_sent]") int sms_sent, @Field("data[attached_files]") String attached_files, @Field("data[created_at]") String created_time);

    @FormUrlEncoded
    @POST("updateCustomer/{id}")
    Call <ResponseBody> updateCustomer(@Path("id")int id, @Field("data[title]") String title, @Field("data[mobile_phone]") String mobile,
                                       @Field("data[email]") String email, @Field("data[name]") String name, @Field("data[address]") String address,
                                       @Field("data[town]") String town, @Field("data[postal_code]") String postal, @Field("data[further_note]") String further_note,
                                       @Field("data[state]") int state, @Field("data[remind_date]") String remind, @Field("data[category_id]") int category,
                                       @Field("data[sms_sent]") int sms_sent, @Field("data[attached_files]") String attached_files);

    @Multipart
    @POST("customer/attach_file")
    Call <String> uploadCustomerAttach(@Part MultipartBody.Part file);

    @GET("deleteCustomer/{id}")
    Call <String> deleteCustomer(@Path("id") int id);

    @GET("getInvoiceList/")
    Call <List<Invoice>> getAllInvoiceList();

    @GET("getInvoiceList/{id}")
    Call <List<Invoice>> getInvoiceList(@Path("id") int customer_id);

    @Multipart
    @POST("/invoice/logo_file")
    Call <String> uploadInvoiceLogo(@Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("insertInvoice")
    Call  <ResponseBody> insertInvoice(@Field("data[invoice_no]") String invoice_no, @Field("data[email]") String email, @Field("data[invoice_date]") String date,
                                       @Field("data[mobile_num]") String mobile, @Field("data[to]") String to, @Field("data[from_address]") String address,
                                       @Field("data[items]") String items, @Field("data[excluding_vat]") String exclude_vat, @Field("data[vat_amount]") String amount,
                                       @Field("data[invoice_total]") String total, @Field("data[payed_amount]") String payed, @Field("data[due_total]") String due_total,
                                       @Field("data[comment]") String comment, @Field("data[customer_id]") int customer_id,
                                       @Field("data[preset1]") String preset1, @Field("data[preset2]") String preset2,
                                       @Field("id") int id, @Field("mode") String mode);

    @GET("deleteInvoice/{id}")
    Call <String> deleteInvoice(@Path("id") int id);

    // file attach in home page
    @Multipart
    @POST("/attach_file")
    Call <String> uploadAttach(@Part MultipartBody.Part file);

    @GET("/get_attach_files")
    Call <List<Attachment>> get_attach_files();

    @FormUrlEncoded
    @POST("/delete_attach_file")
    Call <String> deleteAttachFile(@Field("name") String fileName);
}