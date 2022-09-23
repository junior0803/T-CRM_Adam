package com.bts.adamcrm.activity;

import static com.bts.adamcrm.MyApplication.mConnManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.StockItemAdapter;
import com.bts.adamcrm.database.QueryContract;
import com.bts.adamcrm.database.QueryResponse;
import com.bts.adamcrm.database.StockQueryImplementation;
import com.bts.adamcrm.model.StockItem;
import com.bts.adamcrm.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    int is_shopping = 1;
    int type = 1;

    public static void launch(Activity activity, int is_shopping, int type) {
        Intent intent = new Intent(activity.getBaseContext(), StockListActivity.class);
        intent.putExtra("is_shopping", is_shopping);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        ButterKnife.bind(this);
        is_shopping = getIntent().getIntExtra("is_shopping", 1);
        type = getIntent().getIntExtra("type", 1);
        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        if (is_shopping != 1) {
            title_text.setText("Parts List " + type);
        } else {
            title_text.setText("Shopping List " + type);
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
        stock_recycler.addOnItemTouchListener(new RecyclerItemClickListener(StockListActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log("stock_recycler position : " + position);
                showEditDialog(StockListActivity.this, stockItemList.get(position));
            }
        }));
        if (sharedPreferencesManager.getBooleanValue("part_update")){
            reloadAllStocks();
        } else {
            loadStockData();
        }
    }

    private void reloadAllStocks(){
        progressDialog.show();
        apiRepository.getApiService().getAllPartItemList().enqueue(new Callback<List<StockItem>>() {
            @Override
            public void onResponse(Call<List<StockItem>> call, Response<List<StockItem>> response) {
                Log(response.raw().toString());
                if (response.body() != null) {
                    Log("size : "+ response.body().size());
                    QueryContract.StockQuery stockQuery = new StockQueryImplementation();
                    stockQuery.deleteAllStocks(new QueryResponse<Boolean>() {
                        @Override
                        public void onSuccess(Boolean data) {
                            Log("delete stocks successfully");
                        }

                        @Override
                        public void onFailure(String message) {
                            Log("delete stocks failed");
                        }
                    });
                    for (StockItem stockItem : response.body()) {
                        stockQuery.insertStock(stockItem, new QueryResponse<StockItem>() {
                            @Override
                            public void onSuccess(StockItem data) {

                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });
                    }
                    sharedPreferencesManager.setBooleanValue("part_update", false);
                    loadStockData();
                }
            }

            @Override
            public void onFailure(Call<List<StockItem>> call, Throwable t) {
                showToast("Please Activate internet connection!");
            }
        });
    }

    private void loadStockData(){
        stockItemList = new ArrayList<>();
        QueryContract.StockQuery stockQuery = new StockQueryImplementation();
        stockQuery.getStocksInParts(is_shopping, type, new QueryResponse<List<StockItem>>() {
            @Override
            public void onSuccess(List<StockItem> data) {
                stockItemList = data;
                stockItemAdapter.updateAdapter(stockItemList);
                stock_recycler.setAdapter(stockItemAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                Log("stock load Failed");
                progressDialog.dismiss();
            }
        });

    }

    private void showEditDialog(Activity activity, StockItem stockItem) {
        Dialog dialog = new Dialog(activity);
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
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Network Status check
                NetworkInfo nInfo = mConnManager.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if (!connected) {
                    showToast("Please Activate internet connection!");
                    return;
                }
                apiRepository.getApiService().updatePart(stockItem.getId(), edtQuantity.getText().toString(), edtMinQuantity.getText().toString()
                        , edtDescription.getText().toString(), edtPno.getText().toString(), type, is_shopping).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null){
                            showToast("Saved!");
                            reloadAllStocks();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("Please Activate internet connection!");
                    }
                });
            }
        });
        dialog.show();
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
                NetworkInfo nInfo = mConnManager.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if (!connected) {
                    showToast("Please Activate internet connection!");
                    return;
                }
                apiRepository.getApiService().deletePart(stockItem.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null){
                            showToast("Deleted!");
                            reloadAllStocks();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("Please Activate internet connection!");
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
                //String strId;
                StockItem s = new StockItem();
                s.setDescription(editDescription.getText().toString());
                s.setPno(editPno.getText().toString());
                s.setQuantity(editQuantity.getText().toString());
                s.setMinimum_quantity(editMinQuantity.getText().toString());
                s.setType(type);
                s.setIs_shopping(is_shopping);
                // network status check
                NetworkInfo nInfo = mConnManager.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if (!connected) {
                    showToast("Please Activate internet connection!");
                    return;
                }
                apiRepository.getApiService().createPart(s.getQuantity(), s.getMinimum_quantity(), s.getDescription(),
                         s.getPno(), s.getType(), s.getIs_shopping()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null){
                            showToast("Created!");
                        }
                        reloadAllStocks();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("Please Activate internet connection!");
                    }
                });
            }
        });
        dialog.show();
    }
}
