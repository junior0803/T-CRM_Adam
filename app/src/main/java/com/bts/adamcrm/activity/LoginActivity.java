package com.bts.adamcrm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bts.adamcrm.BaseActivity;
import com.bts.adamcrm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    String TAG = "LoginActivity";
    @BindView(R.id.btn_login)
    Button btn_login;
    int counter = 0;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.name)
    TextView name;
    ProgressDialog progressDialog;


    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity.getBaseContext(), LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //updateUI();
    }

    private void updateUI() {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

//        if (firebaseUser != null) {
//            launchMain(firebaseUser);
//        }
        launchMain();
    }

    private void launchMain() {
        MainActivity.launch(this);
        exit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.progressDialog = new ProgressDialog(this, R.style.RedAppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.login_check);
        progressDialog.show();
        this.btn_login.setOnClickListener(this);
        this.name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_login:
                if (edt_password.getText().toString().equals("")
                        || edt_username.getText().toString().equals(""))
                    showToast(getString(R.string.invalid_input_userinfo));
                else {
                    this.progressDialog.setTitle(R.string.logging);
                    this.progressDialog.show();
                    Log.e("junior", "LoginActivity - btn_login");
                    login();
                }
                break;
            case R.id.name:
                int i = counter;
                if (i < 5){
                    counter = i + 1;
                    return;
                }
                counter = 0;
                RegisterActivity.launch(this);
            default:

        }
    }

    private void login() {
        Log.d(TAG, "signInWithEmail:success");
        updateUI();
    }
}
