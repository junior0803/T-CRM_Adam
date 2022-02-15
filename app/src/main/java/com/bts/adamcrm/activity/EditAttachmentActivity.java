package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.bts.adamcrm.model.Attachment;
import com.google.gson.Gson;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAttachmentActivity extends BaseActivity implements View.OnClickListener {
    Attachment attachment;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_save)
    TextView btn_save;
    private Calendar calendar;
    @BindView(R.id.edt_date)
    EditText edt_date;
    private int mDay;
    private int mMonth;
    private int mYear;
    String str_attachment = "";
    @BindView(R.id.title_text)
    TextView title_text;

    public static void launch(Activity activity, String attach){
        Intent intent = new Intent(activity.getBaseContext(), EditAttachmentActivity.class);
        intent.putExtra("str_attachment", attach);
        activity.startActivityForResult(intent, PICKER_REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attachment);
        ButterKnife.bind(this);
        title_text.setText(R.string.edit_attach);
        str_attachment = getIntent().getStringExtra("str_attachment");
        attachment = new Gson().fromJson(str_attachment, Attachment.class);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        edt_date.setText("");
        getCurrentDate();
    }

    private void getCurrentDate() {
        calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                exit();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.edt_date:
                showAttachmentDateTimeDialog();

        }
    }

    private void save() {
        attachment.setDate_delete(edt_date.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("attachment", new Gson().toJson(attachment));
        intent.putExtra("update", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showAttachmentDateTimeDialog() {
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
                edt_date.setText(mDay + "/" + mMonth + "/" + mYear);
            }
        }, mYear, mMonth, mDay).show();
    }
}
