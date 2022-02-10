package com.bts.adamcrm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Invoice;

import java.io.File;

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

    int fileindex = 1;
    Invoice invoice;

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
        activity.startActivityForResult(intent, 9000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        ButterKnife.bind(this);
        title_text.setText(R.string.create_invoice);

        btn_back.setOnClickListener(this);
        btn_add_item.setOnClickListener(this);
        btn_create_pdf.setOnClickListener(this);
        btn_select_logo.setOnClickListener(this);
        btn_select_logo2.setOnClickListener(this);
        btn_save.setOnClickListener(this);
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
                showToast("btn_create_pdf");
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_select_logo:
                fileindex = 1;
                showToast("btn_select_logo");
                break;
            case R.id.btn_select_logo2:
                fileindex = 2;
                showToast("btn_select_logo2");
                break;
        }
    }
    public void addInvoiceItemDialog(Activity activity) {
        showToast("addInvoiceItemDialog()");
    }

    private void save() {
        showToast("save()");
    }
}
