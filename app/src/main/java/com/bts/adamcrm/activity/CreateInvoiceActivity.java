package com.bts.adamcrm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.InvoiceItemAdapter;
import com.bts.adamcrm.model.Invoice;
import com.bts.adamcrm.model.InvoiceItem;
import com.bts.adamcrm.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInvoiceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_add_item)
    Button btn_add_item;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_create_pdf)
    TextView btn_create_pdf;
    @BindView(R.id.btn_save)
    TextView btn_save;
    @BindView(R.id.btn_select_logo)
    ImageView btn_select_logo;
    @BindView(R.id.btn_select_logo2)
    ImageView btn_select_logo2;
    @BindView(R.id.edt_address)
    EditText edt_address;
    @BindView(R.id.edt_comment)
    EditText edt_comment;
    @BindView(R.id.edt_company)
    EditText edt_company;
    @BindView(R.id.edt_due_total)
    EditText edt_due_total;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_exclude_vat)
    EditText edt_exclude_vat;
    @BindView(R.id.edt_invoice_date)
    EditText edt_invoice_date;
    @BindView(R.id.edt_invoice_no)
    EditText edt_invoice_no;
    @BindView(R.id.edt_invoice_total)
    EditText edt_invoice_total;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_payed_amount)
    EditText edt_payed_amount;
    @BindView(R.id.edt_vat_amount)
    EditText edt_vat_amount;

    Invoice invoice;
    InvoiceItemAdapter invoiceItemAdapter;
    List<InvoiceItem> invoiceItemList = new ArrayList<>();
    int fileindex = 1;
    private Calendar calendar;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private int mYear;

    @BindView(R.id.invoice_recycler)
    RecyclerView invoice_recycler;
    File logoFile;
    File logoFile2;
    String str_invoice = "";
    @BindView(R.id.title_text)
    TextView title_text;

    public static void launch(Activity activity, String str){
        Intent intent = new Intent(activity.getBaseContext(), CreateInvoiceActivity.class);
        intent.putExtra("str_invoice", str);
        activity.startActivityForResult(intent, INVOICE_REQUEST_CODE);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            showFileChooser();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            showToast("Permission Denied\n" + deniedPermissions.toString());
        }
    };

    public void showFileChooser(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        ButterKnife.bind(this);
        title_text.setText(R.string.create_invoice);

        str_invoice = getIntent().getStringExtra("str_invoice");
        if (str_invoice.equals("")){
            invoice = new Invoice();
            edt_company.setText(sharedPreferencesManager.getStringValue("address"));
            edt_mobile.setText(sharedPreferencesManager.getStringValue("mobile"));
            edt_email.setText(sharedPreferencesManager.getStringValue("email"));
        } else {
            invoice = new Gson().fromJson(str_invoice, Invoice.class);
            if (invoice.getLogo1() != null)
                logoFile = new File(invoice.getLogo1());
            if (invoice.getLogo2() != null)
                logoFile2 = new File(invoice.getLogo2());

            if (logoFile != null && logoFile.exists()) {
                btn_select_logo.setImageDrawable(Drawable.createFromPath(logoFile.getAbsolutePath()));
            } else {
                invoice.setLogo1("");
                logoFile = null;
            }

            if (logoFile2 != null && logoFile2.exists()){
                btn_select_logo2.setImageDrawable(Drawable.createFromPath(logoFile2.getAbsolutePath()));
            } else {
                invoice.setLogo2("");
                logoFile2 = null;
            }

            if (!sharedPreferencesManager.getStringValue("logo").equals("")){
                logoFile = new File(sharedPreferencesManager.getStringValue("logo"));
                if (logoFile.exists()){
                    btn_select_logo.setImageDrawable(Drawable.createFromPath(logoFile.getAbsolutePath()));
                } else {
                    invoice.setLogo1("");
                    logoFile = null;
                }
            }
            if (!sharedPreferencesManager.getStringValue("logo2").equals("")){
                logoFile2 = new File(sharedPreferencesManager.getStringValue("logo2"));
                if (logoFile2.exists()){
                    btn_select_logo2.setImageDrawable(Drawable.createFromPath(logoFile2.getAbsolutePath()));
                } else {
                    invoice.setLogo2("");
                    logoFile2 = null;
                }
            }
            edt_invoice_no.setText(invoice.getInvoice_no());
            edt_email.setText(invoice.getEmail());
            edt_invoice_date.setText(invoice.getInvoice_date());
            edt_mobile.setText(invoice.getMobile_num());
            edt_address.setText(invoice.getTo());
            edt_company.setText(invoice.getFrom_address());
            Log("invoiceitem : " + invoice.getItems());
            //invoiceItemList = new Gson().fromJson(invoice.getItems(), InvoiceItem.class);
            Type type  = new TypeToken<ArrayList<InvoiceItem>>(){}.getType();
            invoiceItemList = new Gson().fromJson(invoice.getItems(), type);
            edt_exclude_vat.setText(invoice.getExclude_vat());
            edt_vat_amount.setText(invoice.getVat_amount());
            edt_invoice_total.setText(invoice.getInvoice_total());
            edt_payed_amount.setText(invoice.getPayed_amount());
            edt_due_total.setText(invoice.getDue_total());
            edt_comment.setText(invoice.getComment());
        }
        btn_back.setOnClickListener(this);
        btn_add_item.setOnClickListener(this);
        btn_create_pdf.setOnClickListener(this);
        btn_select_logo.setOnClickListener(this);
        btn_select_logo2.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_invoice_date.setOnClickListener(this);
        invoiceItemAdapter = new InvoiceItemAdapter(invoiceItemList);
        invoice_recycler.setAdapter(invoiceItemAdapter);
        invoiceItemAdapter.updateAdapter(invoiceItemList);
        invoice_recycler.addOnItemTouchListener(new RecyclerItemClickListener(CreateInvoiceActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.findViewById(R.id.action_remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(CreateInvoiceActivity.this);
                        dialog.requestWindowFeature(1);
                        dialog.setContentView(R.layout.dialog_delete_item);
                        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.delete_item);
                        ((TextView) dialog.findViewById(R.id.txt)).setText("Are you sure you want to delete invoice item " + invoiceItemList.get(position).getDescription() + " ?");
                        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                invoiceItemList.remove(position);
                                invoiceItemAdapter.updateAdapter(invoiceItemList);
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
                });
            }
        }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_item:
                addInvoiceItemDialog(this);
                break;
            case R.id.btn_back:
                exit();
                break;
            case R.id.btn_create_pdf:
                createPdf();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.edt_invoice_date:
                showInvoice_date();
                break;
            case R.id.btn_select_logo:
                fileindex = 1;
                TedPermission.create()
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage(R.string.permission_check_message)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.btn_select_logo2:
                fileindex = 2;
                TedPermission.create()
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage(R.string.permission_check_message)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
        }
    }

    private void createPdf() {
        if (str_invoice.equals("")){
            showToast("Please save invoice first!");
        } else {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(INVOICE_PDF_URI + invoice.getId()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void showInvoice_date() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1 + 1;
                mDay = i2;
                edt_invoice_date.setText(mYear + "-"
                        + String.format("%02d-%02d", Integer.valueOf(mMonth), Integer.valueOf(mDay)));
                showInvoiceTimeDialog();
            }
        }, mYear, mMonth, mDay).show();
    }

    private void showInvoiceTimeDialog() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                edt_invoice_date.append(String.format(" %02d:%02d", Integer.valueOf(i), Integer.valueOf(i1)));
            }
        }, mHour, mMinute, true).show();
    }


    public void addInvoiceItemDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_add_invoice_item);
        EditText edit_quantity = dialog.findViewById(R.id.edt_quantity);
        EditText edit_description = dialog.findViewById(R.id.edt_description);
        EditText edit_price = dialog.findViewById(R.id.edt_price);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(R.string.add_invoice_item);
        dialog.findViewById(R.id.btn_dialog_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoiceItemList.add(new InvoiceItem(edit_description.getText().toString(),
                        edit_price.getText().toString(), Integer.parseInt(edit_quantity.getText().toString())));
                invoiceItemAdapter.updateAdapter(invoiceItemList);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void save() {
        invoice.setInvoice_no(edt_invoice_no.getText().toString());
        invoice.setInvoice_date(edt_invoice_date.getText().toString());
        invoice.setInvoice_total(edt_invoice_total.getText().toString());
        invoice.setItems(new Gson().toJson(invoiceItemList));
        invoice.setMobile_num(edt_mobile.getText().toString());
        invoice.setEmail(edt_email.getText().toString());
        invoice.setTo(edt_address.getText().toString());
        invoice.setFrom_address(edt_company.getText().toString());
        if (logoFile == null){
            invoice.setLogo1("");
        }
        if (logoFile2 == null){
            invoice.setLogo2("");
        }
        invoice.setCustomer_id(String.valueOf(0));
        invoice.setExclude_vat(edt_exclude_vat.getText().toString());
        invoice.setVat_amount(edt_vat_amount.getText().toString());
        invoice.setDue_total(edt_due_total.getText().toString());
        invoice.setPayed_amount(edt_payed_amount.getText().toString());
        invoice.setComment(edt_comment.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("invoice", new Gson().toJson(invoice));
        intent.putExtra("update", !str_invoice.equals(""));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE){
            if (data != null) {
                String[] stringArray = data.getExtras().getStringArray("images");
                if (stringArray.length > 0 && stringArray[0] != null){
                    System.currentTimeMillis();
                    if (fileindex == 1) {
                        logoFile = new File(stringArray[0]);
                        uploadFile(logoFile);
                    } else {
                        logoFile2 = new File(stringArray[0]);
                        uploadFile(logoFile2);
                    }
                }
            }
        }
    }

    private void uploadFile(File file) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("*/*"),
                file
        );
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        apiRepository.getApiService().uploadInvoiceLogo(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Log("response : " + response.body());
                    String uploadFile = response.body();
                    if (fileindex == 1) {
                        invoice.setLogo1(uploadFile);
                        //sharedPreferencesManager.setStringValue("logo", logoFile.getAbsolutePath());
                        btn_select_logo.setImageDrawable(Drawable.createFromPath(logoFile.getAbsolutePath()));
                    } else {
                        invoice.setLogo2(uploadFile);
                        //sharedPreferencesManager.setStringValue("logo2", logoFile2.getAbsolutePath());
                        btn_select_logo2.setImageDrawable(Drawable.createFromPath(logoFile2.getAbsolutePath()));
                    }
                } else {
                    showToast("Upload Failed! please try again...");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("Please Activate internet connection!");
                progressDialog.show();
            }
        });
    }
}
