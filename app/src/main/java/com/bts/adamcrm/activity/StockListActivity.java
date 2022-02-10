package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.model.StockItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add)
    ImageView btn_add;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    int code = 1;
    @BindView(R.id.edt_search)
    EditText edt_search;
    ProgressDialog progressDialog;
    //StockItemAdapter stockItemAdapter;
    List<StockItem> stockItemList = new ArrayList();
    //DatabaseReference stockRef;
    @BindView(R.id.stock_recycler)
    RecyclerView stock_recycler;
    @BindView(R.id.title_text)
    TextView title_text;
    int type = 1;

    public static void launch(Activity activity, int code, int type) {
        Intent intent = new Intent(activity.getBaseContext(), StockListActivity.class);
        intent.putExtra("code", code);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 1);
        code = getIntent().getIntExtra("code", 1);
        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        if (this.type == 1) {
            title_text.setText("Parts List " + code);
        } else {
            title_text.setText("Shopping List " + code);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                showCreateDialog(this);
                break;
            case R.id.btn_back:
                exit();
        }
    }

    public void showCreateDialog(Activity activity) {}
}
