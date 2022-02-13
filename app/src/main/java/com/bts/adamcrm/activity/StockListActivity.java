package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.BuildConfig;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.StockItemAdapter;
import com.bts.adamcrm.model.StockItem;
import com.bts.adamcrm.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add)
    ImageView btn_add;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.edt_search)
    EditText edt_search;
    ProgressDialog progressDialog;
    StockItemAdapter stockItemAdapter;
    List<StockItem> stockItemList = new ArrayList();
    //DatabaseReference stockRef;
    @BindView(R.id.stock_recycler)
    RecyclerView stock_recycler;
    @BindView(R.id.title_text)
    TextView title_text;
    int type = 1;
    int code = 1;

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
        if (type == 1) {
            title_text.setText("Parts List " + code);
        } else {
            title_text.setText("Shopping List " + code);
        }

        progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.load_data);
        stockItemAdapter = new StockItemAdapter(stockItemList);
        stock_recycler.setAdapter(stockItemAdapter);
        stockItemAdapter.updateAdapter(stockItemList);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stockItemAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        stock_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showEditDialog(getBaseContext(), stockItemList.get(position));
            }
        }));
        loadAllData();
    }

    private void loadAllData() {
        progressDialog.show();
    }

    private void showEditDialog(Context context, StockItem stockItem) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_edt_stock);
        TextView btnSave = dialog.findViewById(R.id.btn_save);
        EditText edtPno = dialog.findViewById(R.id.edt_pno);
        EditText edtDescription = dialog.findViewById(R.id.edt_description);
        EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        EditText edtMinQuantity = dialog.findViewById(R.id.edt_minimum_quantity);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.edit_part);
        edtPno.setText(stockItem.getPno() + "");
        edtDescription.setText(stockItem.getDescription() + "");
        edtQuantity.setText(stockItem.getQuantity() + "");
        edtMinQuantity.setText(stockItem.getMinimum_quantity() + "");
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(stockItem);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showDeleteDialog(StockItem stockItem) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_delete_item);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete part " + stockItem.getDescription() + " ?");
        ((TextView) dialog.findViewById(R.id.btn_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // more implement
                showToast("deleteItem more implement");
                dialog.dismiss();
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

    public void showCreateDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_add_stock);
        EditText editPno = dialog.findViewById(R.id.edt_pno);
        EditText editDescription = dialog.findViewById(R.id.edt_description);
        EditText editQuantity = dialog.findViewById(R.id.edt_quantity);
        EditText editMinQuantity = dialog.findViewById(R.id.edt_minimum_quantity);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.add_part);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strKey;
                StockItem stockItem = new StockItem();
                if (stockItemList.size() > 0){
                    strKey = ((stockItemList.get(stockItemList.size() - 1).getKey()) + 1) + "";
                } else {
                    strKey = BuildConfig.VERSION_NAME;
                }
                stockItem.setDescription(editDescription.getText().toString());
                stockItem.setPno(Long.valueOf(editPno.getText().toString()));
                stockItem.setQuantity(Long.valueOf(editQuantity.getText().toString()));
                stockItem.setMinimum_quantity(Long.valueOf(editMinQuantity.getText().toString()));
                stockItem.setKey(Long.valueOf(strKey));

            }
        });
        dialog.show();
    }
}
