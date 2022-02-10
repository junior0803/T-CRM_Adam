package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "LoginActivity";
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_username)
    EditText edt_username;
    ProgressDialog progressDialog;

    public static void launch(Activity activity){
        activity.startActivity(new Intent(activity.getBaseContext(), RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUI(){
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null && progressDialog.isShowing()){
            this.progressDialog.dismiss();
        }
        showToast("Added Successfully");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        this.btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
