package com.bts.adamcrm.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;
import com.opensooq.supernova.gligar.GligarPicker;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btn_select_logo)
    ImageView btn_select_logo;
    @BindView(R.id.btn_select_logo2)
    ImageView btn_select_logo2;
    @BindView(R.id.edt_company)
    EditText edt_company;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    String logo_address1;
    String logo_address2;

    int logo_index = 1;
    @BindView(R.id.title_text)
    TextView title_text;

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), SettingsActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        this.title_text.setText(R.string.setting);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_select_logo.setOnClickListener(this);
        btn_select_logo2.setOnClickListener(this);
        if (!sharedPreferencesManager.getStringValue("logo").equals("")){
            String stringValue = this.sharedPreferencesManager.getStringValue("logo");
            logo_address1 = stringValue;
            btn_select_logo.setImageDrawable(Drawable.createFromPath(stringValue));
        }
        if (!sharedPreferencesManager.getStringValue("logo2").equals("")){
            String stringValue2 = this.sharedPreferencesManager.getStringValue("logo2");
            logo_address2 = stringValue2;
            btn_select_logo2.setImageDrawable(Drawable.createFromPath(stringValue2));
        }
        edt_email.setText(sharedPreferencesManager.getStringValue("email"));
        edt_mobile.setText(sharedPreferencesManager.getStringValue("mobile"));
        edt_company.setText(sharedPreferencesManager.getStringValue("address"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKER_REQUEST_CODE){
            String [] images = data.getExtras().getStringArray("images");
            if (images.length > 0 && images[0] != null) {
                if (logo_index == 1) {
                    logo_address1 = images[0];
                    btn_select_logo.setImageDrawable(Drawable.createFromPath(images[0]));
                } else {
                    logo_address2 = images[0];
                    btn_select_logo2.setImageDrawable(Drawable.createFromPath(images[0]));
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                sharedPreferencesManager.setStringValue("logo", logo_address1);
                sharedPreferencesManager.setStringValue("logo2", logo_address2);
                sharedPreferencesManager.setStringValue("email", edt_email.getText().toString());
                sharedPreferencesManager.setStringValue("mobile", edt_mobile.getText().toString());
                sharedPreferencesManager.setStringValue("address", edt_company.getText().toString());
                exit();
                break;
            case R.id.btn_select_logo:
                logo_index = 1;
                new GligarPicker().requestCode(PICKER_REQUEST_CODE).withActivity(this).limit(1).show();
                break;
            case R.id.btn_select_logo2:
                new GligarPicker().requestCode(PICKER_REQUEST_CODE).withActivity(this).limit(1).show();
                logo_index = 2;
                break;
            case R.id.btn_back:
                exit();
        }
    }
}
