package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.CategoryAdapter;
import com.bts.adamcrm.model.Category;
import com.bts.adamcrm.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add)
    TextView btn_add;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList = new ArrayList<>();
    @BindView(R.id.category_recycler)
    RecyclerView category_recycler;
    ProgressDialog progressDialog;
    @BindView(R.id.title_text)
    TextView title_text;

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), CategoryActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.load_category);
        progressDialog.show();
        title_text.setText(R.string.category_list);
        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        categoryAdapter = new CategoryAdapter(categoryList);
        category_recycler.setAdapter(categoryAdapter);
        category_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectCategoryItem(view, position);
            }
        }));
        showData();
    }

    private void selectCategoryItem(View view, int position) {
        view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryActivity.launch(CategoryActivity.this, categoryList.get(position));
            }
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(CategoryActivity.this, categoryList.get(position));
            }
        });
    }

    private void deleteItem(Activity activity, Category category) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete category " + category.getName() + " ?");
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showToast("Delete Item more implement");
                apiRepository.getApiService().deleteCategory(String.valueOf(category.getId())).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null){
                            Log("onResponse : " + response.body() + " position : " + category.getId());
                            showData();
                        }
                        sharedPreferencesManager.setBooleanValue("update", true);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log("onFailure");
                        showToast("Please Activate internet connection");
                    }
                });
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showData() {
        Log("showData");
        apiRepository.getApiService().categoryList().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log(response.raw().toString());
                if (response.body() != null) {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log("size : "+ response.body().size());
                    categoryList = response.body();
                    categoryAdapter.updateAdapter(categoryList);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_add){
            AddCategoryActivity.launch(this);
        } else if (id == R.id.btn_back){
            exit();
        }
    }
}
