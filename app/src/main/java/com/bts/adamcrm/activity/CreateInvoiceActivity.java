package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.adapter.InvoiceAdapter;
import com.bts.adamcrm.adapter.InvoiceItemAdapter;
import com.bts.adamcrm.model.Invoice;
import com.bts.adamcrm.model.InvoiceItem;
import com.bts.adamcrm.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.opensooq.supernova.gligar.GligarPicker;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            if (invoice.getFile_address() != null)
                logoFile = new File(invoice.getFile_address());
            if (invoice.getFile_address2() != null)
                logoFile2 = new File(invoice.getFile_address2());

            if (logoFile != null && logoFile.exists()) {
                btn_select_logo.setImageDrawable(Drawable.createFromPath(logoFile.getAbsolutePath()));
            } else {
                invoice.setFile("");
                invoice.setFile_address("");
                logoFile = null;
            }

            if (logoFile2 != null && logoFile2.exists()){
                btn_select_logo2.setImageDrawable(Drawable.createFromPath(logoFile2.getAbsolutePath()));
            } else {
                invoice.setFile2("");
                invoice.setFile_address2("");
                logoFile2 = null;
            }

            if (!sharedPreferencesManager.getStringValue("logo").equals("")){
                logoFile = new File(sharedPreferencesManager.getStringValue("logo"));
                if (logoFile.exists()){
                    btn_select_logo.setImageDrawable(Drawable.createFromPath(logoFile.getAbsolutePath()));
                } else {
                    invoice.setFile("");
                    invoice.setFile_address("");
                    logoFile = null;
                }
            }
            if (!sharedPreferencesManager.getStringValue("logo2").equals("")){
                logoFile2 = new File(sharedPreferencesManager.getStringValue("logo2"));
                if (logoFile2.exists()){
                    btn_select_logo2.setImageDrawable(Drawable.createFromPath(logoFile2.getAbsolutePath()));
                } else {
                    invoice.setFile2("");
                    invoice.setFile_address2("");
                    logoFile2 = null;
                }
            }
            edt_invoice_no.setText(invoice.getInvoice_no());
            edt_email.setText(invoice.getEmail());
            edt_invoice_date.setText(invoice.getInvoice_date());
            edt_mobile.setText(invoice.getMobile_no());
            edt_address.setText(invoice.getAddress());
            edt_company.setText(invoice.getEdt_company());
            invoiceItemList = invoice.getInvoiceItemList();
            edt_exclude_vat.setText(invoice.getExclude_vat());
            edt_vat_amount.setText(invoice.getVat_amount());
            edt_invoice_total.setText(invoice.getInclude_total());
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
        invoiceItemAdapter = new InvoiceItemAdapter((invoiceItemList));
        invoice_recycler.setAdapter(invoiceItemAdapter);
        invoiceItemAdapter.updateAdapter(invoiceItemList);
        invoice_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.findViewById(R.id.action_remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(getBaseContext());
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
                showToast("create pdf");
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_select_logo:
                fileindex = 1;
                new GligarPicker().requestCode(PICKER_REQUEST_CODE).withActivity(this).limit(1).show();
                break;
            case R.id.btn_select_logo2:
                fileindex = 2;
                new GligarPicker().requestCode(PICKER_REQUEST_CODE).withActivity(this).limit(1).show();
                break;
        }
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
        invoice.setInclude_total(edt_invoice_total.getText().toString());
        invoice.setInvoiceItemList(invoiceItemList);
        invoice.setMobile_no(edt_mobile.getText().toString());
        invoice.setEmail(edt_email.getText().toString());
        invoice.setAddress(edt_address.getText().toString());
        invoice.setEdt_company(edt_company.getText().toString());
        if (logoFile == null){
            invoice.setFile_address("");
            invoice.setLogo("");
        } else {
            invoice.setFile_address(logoFile.getAbsolutePath());
        }
        if (logoFile2 == null){
            invoice.setFile_address("");
            invoice.setLogo("");
        } else {
            invoice.setFile_address(logoFile2.getAbsolutePath());
        }
        
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
        if (resultCode == RESULT_OK && requestCode == PICKER_REQUEST_CODE){
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
        showToast("upload file name: " + file.getName());
    }
}
