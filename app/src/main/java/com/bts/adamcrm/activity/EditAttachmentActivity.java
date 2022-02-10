package com.bts.adamcrm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAttachmentActivity extends BaseActivity implements View.OnClickListener {


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
                break;
            case R.id.edt_date:

        }
    }
}
