package com.bts.adamcrm.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int FILE_SELECT_CODE = 1111;
    private static final int PICKER_REQUEST_CODE = 2000;

    @BindView(R.id.btn_add_invoice)
    Button btn_add_invoice;
    @BindView(R.id.btn_attach)
    Button btn_attach;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_category)
    Button btn_category;
    @BindView(R.id.btn_date)
    TextView btn_date;
    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_send_email)
    Button btn_send_email;
    @BindView(R.id.btn_send_sms)
    Button btn_send_sms;
    private Calendar calendar;
    @BindView(R.id.chk_remind_me)
    CheckBox chk_remind_me;
    String customer_str;
    Uri downloadUri;
    @BindView(R.id.edt_address)
    EditText edt_address;
    @BindView(R.id.edt_date_attachment)
    EditText edt_date_attachment;
    @BindView(R.id.edt_date_created)
    EditText edt_date_created;
    @BindView(R.id.edt_date_updated)
    EditText edt_date_updated;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_further_none)
    EditText edt_further_none;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_post_code)
    EditText edt_post_code;
    @BindView(R.id.edt_reminder_date)
    EditText edt_reminder_date;
    @BindView(R.id.edt_title)
    EditText edt_title;
    @BindView(R.id.edt_town)
    EditText edt_town;
    String fileName;
    //List<Invoice> invoiceList = new ArrayList();
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private int mYear;

    @BindView(R.id.recycler_attachments)
    RecyclerView recycler_attachments;
    @BindView(R.id.recycler_invoices)
    RecyclerView recycler_invoices;
    boolean reminder = false;
    int selectedIndex = -1;

    boolean sms_sent = false;
    @BindView(R.id.spn_state)
    Spinner spn_state;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.txt_no_attachment)
    TextView txt_no_attachment;
    @BindView(R.id.txt_no_invoice)
    TextView txt_no_invoice;
    @BindView(R.id.txt_sms_staus)
    TextView txt_sms_staus;

    public void showFileChooser(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    public static void launch(Activity activity){
        activity.startActivity(new Intent(activity.getBaseContext(), CustomerDetailActivity.class));
    }

    public void getCurrentDate(){
        calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
    }

    private void setupStateSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select state");
        arrayList.add("Active");
        arrayList.add("Completed");
        arrayList.add("Waiting");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spn_state.setAdapter(arrayAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.bind(this);
        setupStateSpinner();
        getCurrentDate();
        recycler_invoices.setVisibility(View.GONE);
        txt_no_invoice.setVisibility(View.VISIBLE);
        recycler_attachments.setVisibility(View.GONE);
        txt_no_attachment.setVisibility(View.VISIBLE);

        title_text.setText(R.string.customer_detail);
        edt_reminder_date.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_send_sms.setOnClickListener(this);
        btn_send_email.setOnClickListener(this);
        btn_add_invoice.setOnClickListener(this);
        btn_category.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_date_created.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        txt_sms_staus.setOnClickListener(this);
        edt_date_attachment.setOnClickListener(this);
        btn_attach.setOnClickListener(this);
        btn_date.setOnClickListener(this);
    }

    public void showSelecterDialog(Activity activity) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_invoice:
                CreateInvoiceActivity.launch(this, "");
                break;
            case R.id.btn_attach:
                if (edt_date_attachment.getText().toString().equals("")){
                    edt_date_attachment.setText(mDay + "/" + (mMonth + 1) + "/" + (mYear + 2));
                }
                showSelecterDialog(this);
            case R.id.btn_back:
                exit();
                break;
        }
    }
}
