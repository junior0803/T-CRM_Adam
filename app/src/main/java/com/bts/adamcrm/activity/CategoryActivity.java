package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add)
    TextView btn_add;
    @BindView(R.id.btn_back)
    ImageView btn_back;

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

        title_text.setText(R.string.category_list);
        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
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
