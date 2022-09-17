package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Category;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    String category_str = "";
    @BindView(R.id.edt_title)
    EditText edt_title;
    Category category;
    ProgressDialog progressDialog;
    @BindView(R.id.title_text)
    TextView title_text;

    public static void launch(Activity activity){
        activity.startActivity(new Intent(activity.getBaseContext(), AddCategoryActivity.class));
    }

    public static void launch(Activity activity, Category category){
        Intent intent = new Intent(activity, AddCategoryActivity.class);
        intent.putExtra("category", new Gson().toJson(category));
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this);
        if (getIntent() != null){
            category_str = getIntent().getStringExtra("category");
            if (category_str != null && !category_str.equals("")){
                category = (Category) new Gson().fromJson(getIntent().getStringExtra("category"), Category.class);
                edt_title.setText(category.getName());
            }
        }
        progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.create_category);
        title_text.setText(R.string.new_category);
        btn_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                exit();
                break;
            case R.id.btn_add:
                if (edt_title.getText().toString().equals("")){
                    showToast("Please enter a name!");
                } else {
                    if (category_str == null || category_str.equals("")){
                        progressDialog.show();
                        //category = new Category(edt_title.getText().toString(), )
                        apiRepository.getApiService().createCategory(edt_title.getText().toString()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    showToast("Category Created!");
                                    sharedPreferencesManager.setBooleanValue("update", true);
                                }
                                progressDialog.dismiss();
                                exit();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                progressDialog.dismiss();
                                showToast("Please Activate internet connection!");
                            }
                        });
                    } else {
                        progressDialog.show();
                        apiRepository.getApiService().updateItem(String.valueOf(category.getId()), edt_title.getText().toString())
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null){
                                            showToast("Category Updated!");
                                            progressDialog.dismiss();
                                            sharedPreferencesManager.setBooleanValue("update", true);
                                            exit();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        progressDialog.dismiss();
                                        showToast("Please Activate internet connection!");
                                    }
                                });
                    }
                }
        }
    }
}
