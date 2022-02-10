package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
                AddCategoryActivity.launch(getBaseContext(), categoryList.get(position));
            }
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(getBaseContext(), categoryList.get(position));
            }
        });
    }

    private void deleteItem(Context context, Category category) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete category " + category.getTitle() + " ?");
        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Delete Item more implement");
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
        // more implement
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
